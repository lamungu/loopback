package com.lamungu.loopback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lamungu.loopback.adapters.SongListAdapter;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SongListActivity extends Activity {

    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "bbf7e9839272464ab661f0b3782edcbd";
    private static final String REDIRECT_URI = "lamungu-loopback://oauth-callback";
    private Pager<PlaylistSimple> playlists;

    SpotifyApi mSpotifyApi;
    ListView mPlaylistTracksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private","playlist-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        mPlaylistTracksListView = (ListView) findViewById(R.id.playlistTracks);

        Log.d("SongListActivityCreate", "yuuupp");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        Log.d("request code", Integer.toString(requestCode));
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                mSpotifyApi = new SpotifyApi();
                mSpotifyApi.setAccessToken(response.getAccessToken());
                Log.d("Access Token", response.getAccessToken());
                SpotifyService spotify = mSpotifyApi.getService();
            /*    spotify.getMyPlaylists(new Callback<Pager<PlaylistSimple>>() {
                    @Override
                    public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                        playlists = playlistSimplePager;
                        Log.d("Size playlists", Integer.toString(playlists.items.size()));
                        for (int i = 0; i < playlists.items.size(); i++) {
                            PlaylistSimple item = playlists.items.get(i);
                            Log.d("PlaylistSimple", item.name);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });*/
                spotify.getPlaylist("12185327086", "6f0EyIDpBiPlrNYjZk2Zzt", new Callback<Playlist>() {
                    @Override
                    public void success(Playlist playlist, Response response) {
                        Pager<PlaylistTrack> tracks = playlist.tracks;
                        SongListAdapter songListAdapter = new SongListAdapter(SongListActivity.this, 0, tracks.items);
                        mPlaylistTracksListView.setAdapter(songListAdapter);
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(" playekalist", "feafae");
                    }
                });
            }
        }
    }
}
