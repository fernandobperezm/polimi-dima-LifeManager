package fernandoperez.lifemanager.spotifyapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import java.util.List;

import fernandoperez.lifemanager.R;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainSpotifyActivity extends AppCompatActivity implements Search.View {

    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";

    private Search.ActionListener mActionListener;

    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    private ScrollListener mScrollListener = new ScrollListener(mLayoutManager);
    private SearchResultsAdapter mAdapter;

    private PreviewPlayer mPreviewPlayer = null;

    private class ScrollListener extends ResultListScrollListener {

        public ScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore() {
            mActionListener.loadMoreResults();
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, MainSpotifyActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_main);

        Intent intent = getIntent();
        String token = intent.getStringExtra(EXTRA_TOKEN);

        SpotifyService spotify = setWebApiEndpoint(token);

        mActionListener = new SearchPresenter(this, this);
        mActionListener.init(token);

        mPreviewPlayer = new PreviewPlayer();
        getPlaylistFromUser(spotify, "kametoo", "3ILN4nBKp4BpslP7ZsnO2l");

        // Setup search field
        final SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mActionListener.search(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        // Setup search results list
        mAdapter = new SearchResultsAdapter(this, new SearchResultsAdapter.ItemSelectedListener() {
            @Override
            public void onItemSelected(View itemView, Track item) {
                mActionListener.selectTrack(item);
            }
        });

        RecyclerView resultsList = (RecyclerView) findViewById(R.id.search_results);
        resultsList.setHasFixedSize(true);
        resultsList.setLayoutManager(mLayoutManager);
        resultsList.setAdapter(mAdapter);
        resultsList.addOnScrollListener(mScrollListener);

        // If Activity was recreated wit active search restore it
        if (savedInstanceState != null) {
            String currentQuery = savedInstanceState.getString(KEY_CURRENT_QUERY);
            mActionListener.search(currentQuery);
        }
    }

    @Override
    public void reset() {
        mScrollListener.reset();
        mAdapter.clearData();
    }

    @Override
    public void addData(List<Track> items) {
        mAdapter.addData(items);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActionListener.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActionListener.resume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActionListener.getCurrentQuery() != null) {
            outState.putString(KEY_CURRENT_QUERY, mActionListener.getCurrentQuery());
        }
    }

    @Override
    protected void onDestroy() {
        mActionListener.destroy();
        mPreviewPlayer.release();
        super.onDestroy();
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

    /**
     * This method get' all the playlists of the user.
     * @param spotifyService the spotify build in order to access all the API services.
     */
    private void getAllMyPlaylists(SpotifyService spotifyService) {
        // TODO: Handle the failure when retrieving the playlist.

        spotifyService.getMyPlaylists(new Callback<Pager<PlaylistSimple>>() {
            @Override
            public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                // Get all the playlists.
                List<PlaylistSimple> playlists = playlistSimplePager.items;
                mPreviewPlayer.play(playlists.get(0).uri);
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println(error);
            }
        });
    }

    /**
     * This method get' all the playlists of the user.
     * @param spotifyService the spotify build in order to access all the API services.
     */
    private void getPlaylistFromUser(SpotifyService spotifyService, String userId, String playlistId) {
        // TODO: Handle the failure when retrieving the playlist.
        spotifyService.getPlaylist(userId, playlistId, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                mPreviewPlayer.playPlaylist(playlist);
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("error");
            }
        });
    }
}
