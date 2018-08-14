package com.loopback;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class SpotifyModule extends ReactContextBaseJavaModule {
    private static final String CLIENT_ID = "bbf7e9839272464ab661f0b3782edcbd";
    private static final String REDIRECT_URI = "lamungu-loopback://oauth-callback";
    private static final String TAG = "SpotifyModule";
    private static final int REQUEST_CODE = 1337;
    private static final int SONG_SELECTION = 7889;
    protected static final long TIME_DELAY = 1000;

    public SpotifyModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "SpotifyModule";
    }


    @ReactMethod
    public void login(String contact) {
//        Log.e(TAG, "Calling: " + contact);
//            AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
//                AuthenticationResponse.Type.TOKEN,
//                REDIRECT_URI);
//        builder.setScopes(new String[]{"user-read-private", "streaming"});
//        AuthenticationRequest request = builder.build();
//        AuthenticationClient.openLoginActivity(getCurrentActivity(), REQUEST_CODE, request);

    }
}
