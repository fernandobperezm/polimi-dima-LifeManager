// TutorialApp
// Created by Spotify on 25/02/14.
// Copyright (c) 2014 Spotify. All rights reserved.
package fernandoperez.lifemanager.spotifyapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.utils.constants;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpotifyMainActivity extends AppCompatActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "7499847a03c440caa753f45305762a56";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "lifemanager-login://callback";

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;

    private List<PlaylistSimple> userPlaylists;
    private String selectedPlaylistUri;
    private int selectedPlaylistIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_main);


        // TODO: The index should be accessed in another way.
        selectedPlaylistIndex = getIntent().getIntExtra(constants.SPOTIFIY_INDEX_INTENT, 0);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {

                final String accessToken = response.getAccessToken();

                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(SpotifyMainActivity.this);
                        mPlayer.addNotificationCallback(SpotifyMainActivity.this);

                        SpotifyService spotifyService = setWebApiEndpoint(accessToken);
                        spotifyService.getMyPlaylists(new SimplePlaylistCallback());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("SpotifyMainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });


            }
        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("SpotifyMainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("SpotifyMainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("SpotifyMainActivity", "User logged in");

        mPlayer.playUri(null, selectedPlaylistUri, 0, 0);
    }

    @Override
    public void onLoggedOut() {
        Log.d("SpotifyMainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("SpotifyMainActiviy", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("SpotifyMainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("SpotifyMainActivity", "Received connection message: " + message);
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
            userPlaylists = playlistSimplePager.items;

            selectedPlaylistUri = userPlaylists.get(selectedPlaylistIndex).uri;
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d ("SpotifyMainActivity", "Could not get playlist.");
        }

    }
}