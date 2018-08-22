package com.loopback;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
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
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpotifyModule extends ReactContextBaseJavaModule implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback {

    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_PICKER_CANCELLED = "E_PICKER_CANCELLED";
    private static final String E_FAILED_TO_SHOW_PICKER = "E_FAILED_TO_SHOW_PICKER";
    private static final String E_NO_IMAGE_DATA_FOUND = "E_NO_IMAGE_DATA_FOUND";

    private static final String CLIENT_ID = "bbf7e9839272464ab661f0b3782edcbd";
    private static final String REDIRECT_URI = "lamungu-loopback://oauth-callback";
    private static final String TAG = "SpotifyModule";
    private static final int REQUEST_CODE = 1337;
    private static final int SONG_SELECTION = 7889;
    protected static final long TIME_DELAY = 1000;
    private Pager<PlaylistSimple> playlists;

    SpotifyApi mSpotifyApi;
    private Promise mSpotifyPromise;
    private Player mPlayer;
    private String mAccessToken;
    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            // Check if result comes from the correct activity

            switch (requestCode) {
                case REQUEST_CODE:
                    AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
                    if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                        mAccessToken = response.getAccessToken();
                        mSpotifyApi = new SpotifyApi();
                        mSpotifyApi.setAccessToken(mAccessToken);
                        Log.d("Access Token", mAccessToken);
                        final Config playerConfig = new Config(getReactApplicationContext(), mAccessToken, CLIENT_ID);
                        Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                            @Override
                            public void onInitialized(SpotifyPlayer spotifyPlayer) {
                                mPlayer = spotifyPlayer;
                                Log.e(TAG, "I think it worked dude");
                                mPlayer.addConnectionStateCallback(SpotifyModule.this);
                                mPlayer.addNotificationCallback(SpotifyModule.this);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                Log.e(TAG, "Could not initialize player: " + throwable.getMessage());
                            }
                        });
                    }
                    break;
                case SONG_SELECTION:
                    if (intent != null && intent.hasExtra("playlistTrackId")) {
                        Log.d("IM BACK", intent.getStringExtra("playlistTrackId"));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public SpotifyModule(ReactApplicationContext reactContext) {
        super(reactContext);
        // Add the listener for `onActivityResult`
        reactContext.addActivityEventListener(mActivityEventListener);
    }


    @Override
    public void onConnectionMessage(String message) {
        Log.d(TAG, "Received connection message: " + message);
    }

    @Override
    public void onLoggedOut() {
        Log.d(TAG, "User logged out");
    }

    @Override
    public void onLoginFailed(Error e) {
        Log.d(TAG, "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d(TAG, "Temporary error occurred");
    }

    @Override
    public void onLoggedIn() {
        Log.d(TAG, "User logged in");
        this.getPlaylists();
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d(TAG, "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    private void getPlaylists() {
        if (mAccessToken != null) {
            SpotifyService spotify = mSpotifyApi.getService();
            spotify.getMyPlaylists(new Callback<Pager<PlaylistSimple>>() {
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
            });
            spotify.getPlaylist("12185327086", "6f0EyIDpBiPlrNYjZk2Zzt", new Callback<Playlist>() {
                @Override
                public void success(Playlist playlist, Response response) {
                    Pager<PlaylistTrack> tracks = playlist.tracks;
                    Log.d(TAG, tracks.toString());
                    for (int i = 0; i < tracks.items.size(); i++) {
                        PlaylistTrack track = tracks.items.get(i);
                        Log.d("PlaylistSimple", track.track.name);
                    }
                    //tracks.items;
                }
                @Override
                public void failure(RetrofitError error) {
                    Log.d(" playlist", "feafae");
                }
            });
        }
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d(TAG, "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
//            // Handle event type as necessary
//            case kSpPlaybackNotifyMetadataChanged:
//                Metadata.Track currentTrack = mPlayer.getMetadata().currentTrack;
//
//                loopStart = 0;
//                loopEnd = (int)currentTrack.durationMs/1000;
////                mRangeBar.setTickCount((int)currentTrack.durationMs/1000 + 2);
////                mRangeBar.setThumbIndices(loopStart, loopEnd);
//                mTotalDurationTextView.setText(getTimeFromMillis(currentTrack.durationMs));
//                mTrackNameTextView.setText(currentTrack.name);
//                mArtistNameTextView.setText(currentTrack.artistName);
//                mLoopTextView.setText(getTimeFromMillis((long)loopStart*1000) + " - " + getTimeFromMillis((long)loopEnd*1000));
//                Picasso.with(getApplicationContext()).load(currentTrack.albumCoverWebUrl).into(mAlbumCoverImageView);
//                Log.d("TickCount", Integer.toString((int)currentTrack.durationMs/1000 + 2));
//                Log.d("currentTrack", currentTrack.toString());
//                break;
//            case kSpPlaybackNotifyPlay:
//                Log.d("Playing", "Changing to pause image");
//                mPlayImageButton.setImageDrawable(getDrawable(android.R.drawable.ic_media_pause));
//                playbackHandler.removeCallbacks(checkMetadata);
//                playbackHandler.post(checkMetadata);
//                break;
//            case kSpPlaybackNotifyPause:
//                Log.d("Playing", "Changing to play image");
//                mPlayImageButton.setImageDrawable(getDrawable(android.R.drawable.ic_media_play));
//                break;
//            case kSpPlaybackNotifyAudioDeliveryDone:
//                playbackHandler.removeCallbacks(checkMetadata);
            default:
                break;
        }
    }

    @Override
    public String getName() {
        return "SpotifyModule";
    }

    @ReactMethod
    public void login(final Promise promise) {
        Log.e(TAG, "Logging in ");
        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }
        mSpotifyPromise = promise;

        try {
            AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                    AuthenticationResponse.Type.TOKEN,
                    REDIRECT_URI);
            builder.setScopes(new String[]{"user-read-private", "streaming"});
            AuthenticationRequest request = builder.build();
            AuthenticationClient.openLoginActivity(getCurrentActivity(), REQUEST_CODE, request);
        } catch (Exception e) {
            mSpotifyPromise.reject(E_FAILED_TO_SHOW_PICKER, e);
            mSpotifyPromise = null;
        }
    }
}
