// TutorialApp
// Created by Spotify on 25/02/14.
// Copyright (c) 2014 Spotify. All rights reserved.
package fernandoperez.lifemanager.spotifyapi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.adapters.PlaylistCardAdapter;
import fernandoperez.lifemanager.models.Playlist;
import fernandoperez.lifemanager.utils.constants;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpotifyPlaybackFragment extends Fragment implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{
    private static final String CLIENT_ID = "7499847a03c440caa753f45305762a56";
    private static final String REDIRECT_URI = "lifemanager-login://callback";

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;

    private List<PlaylistSimple> userPlaylists;
    private String selectedPlaylistUri;
    private int selectedPlaylistIndex;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static SpotifyPlaybackFragment create() {
        SpotifyPlaybackFragment fragment = new SpotifyPlaybackFragment();
        return fragment;
    }

    public SpotifyPlaybackFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_spotify_main_playback, container, false);

        selectedPlaylistIndex = constants.SPOTIFY_DEFAULT_INDEX_PLAYLIST;

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        Intent intent = AuthenticationClient.createLoginActivityIntent(getActivity(), request);
        startActivityForResult(intent, REQUEST_CODE);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_spotify_playlists);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a grid layout manager
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {

                final String accessToken = response.getAccessToken();

                Config playerConfig = new Config(getContext(), response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(SpotifyPlaybackFragment.this);
                        mPlayer.addNotificationCallback(SpotifyPlaybackFragment.this);

                        SpotifyService spotifyService = setWebApiEndpoint(accessToken);
                        spotifyService.getMyPlaylists(new SimplePlaylistCallback());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("SpotifyPlaybackFragment", "Could not initialize player: " + throwable.getMessage());
                    }
                });


            }
        }
    }

    @Override
    public void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        //Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("SpotifyPlaybackFragment", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("SpotifyPlaybackFragment", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("SpotifyPlaybackFragment", "User logged in");

        mPlayer.playUri(null, selectedPlaylistUri, 0, 0);
    }

    @Override
    public void onLoggedOut() {
        Log.d("SpotifyPlaybackFragment", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("SpotifyPlaybackFragment", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("SpotifyPlaybackFragment", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("SpotifyPlaybackFragment", "Received connection message: " + message);
    }

    /**
     * This function sets all the necessary endpoints to the spotify Web API and return
     * @param authToken Is the token that spotify gave to authorized users.
     * @return We return the spotify service build, ready to use with the API.
     */
    private SpotifyService setWebApiEndpoint(String authToken) {
        final String accessToken = authToken;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SpotifyApi.SPOTIFY_WEB_API_ENDPOINT)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Authorization", "Bearer " + accessToken);
                    }
                })
                .build();

        return restAdapter.create(SpotifyService.class);
    }


    class SimplePlaylistCallback implements Callback<Pager<PlaylistSimple>> {

        @Override
        public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
            String playlistUri;
            String playlistName;
            String playlistId;
            List<Playlist> playlistList = new ArrayList<>();

            userPlaylists = playlistSimplePager.items;

            for (Iterator<PlaylistSimple> iterator = userPlaylists.iterator(); iterator.hasNext(); ){
                PlaylistSimple playlist = iterator.next();
                playlistId = playlist.id;
                playlistName = playlist.name;
                playlistUri = playlist.uri;
                List<String> playlistImagesUrls = new ArrayList<>();

                for (Iterator<Image> imageIterator = playlist.images.iterator(); imageIterator.hasNext();) {
                    Image image = imageIterator.next();
                    playlistImagesUrls.add(image.url);
                }

                playlistList.add(new Playlist(playlistId, playlistName, playlistUri, playlistImagesUrls));
            }

            selectedPlaylistUri = userPlaylists.get(selectedPlaylistIndex).uri;

            mAdapter = new PlaylistCardAdapter(playlistList);

            if (mRecyclerView != null) {
                mRecyclerView.setAdapter(mAdapter);
            }

        }

        @Override
        public void failure(RetrofitError error) {
            Log.d ("SpotifyPlaybackFragment", "Could not get playlist.");
        }

    }
}