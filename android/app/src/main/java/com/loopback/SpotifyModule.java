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
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class SpotifyModule extends ReactContextBaseJavaModule {

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
    private Promise mSpotifyPromise;
    private Player mPlayer;
    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            // Check if result comes from the correct activity
            Log.d(TAG, "MYYYYYY MAAAAN");
            switch (requestCode) {
                case REQUEST_CODE:
                    AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
                    if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                        final Config playerConfig = new Config(getReactApplicationContext(), response.getAccessToken(), CLIENT_ID);
                        Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                            @Override
                            public void onInitialized(SpotifyPlayer spotifyPlayer) {
                                mPlayer = spotifyPlayer;
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
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
