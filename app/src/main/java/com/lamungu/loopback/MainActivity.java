package com.lamungu.loopback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{
    private static final String CLIENT_ID = "bbf7e9839272464ab661f0b3782edcbd";
    private static final String REDIRECT_URI = "lamungu-loopback://oauth-callback";
    final Runnable checkMetadata = new Runnable(){
        public void run() {
            long position = mPlayer.getPlaybackState().positionMs;
            Log.d("Playback spot", Long.toString(position));
            if (position > loopEnd * 1000 && mPlayer.getPlaybackState().isPlaying) {
                Log.d("Reached 10 seconds", Long.toString(position));
                mPlayer.pause(null);
                playbackHandler.removeCallbacks(this);
            } else {
                playbackHandler.postDelayed(this, TIME_DELAY);
            }
            // Set the text view to the current duration;
            mDurationTextView.setText(getTimeFromMillis(position));
        }
    };
    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;
    private static final int SONG_SELECTION = 7889;
    protected static final long TIME_DELAY = 1000;

    private RangeBar mRangeBar;

    private Player mPlayer;
    private int loopStart;
    private int loopEnd;

    ImageView mAlbumCoverImageView;
    ImageButton mPlayImageButton;
    TextView mDurationTextView;
    TextView mTrackNameTextView;
    TextView mArtistNameTextView;
    TextView mTotalDurationTextView;
    TextView mLoopTextView;
    Handler playbackHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_main);
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        initializeValues();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        switch (requestCode) {
            case REQUEST_CODE:
                AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
                if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                    final Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                    Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                        @Override
                        public void onInitialized(SpotifyPlayer spotifyPlayer) {
                            mPlayer = spotifyPlayer;
                            mPlayer.addConnectionStateCallback(MainActivity.this);
                            mPlayer.addNotificationCallback(MainActivity.this);
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
                    this.changeTrack(intent.getStringExtra("playlistTrackId"));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            case kSpPlaybackNotifyMetadataChanged:
                Metadata.Track currentTrack = mPlayer.getMetadata().currentTrack;

                loopStart = 0;
                loopEnd = (int)currentTrack.durationMs/1000;
                mRangeBar.setTickCount((int)currentTrack.durationMs/1000 + 2);
                mRangeBar.setThumbIndices(loopStart, loopEnd);
                mTotalDurationTextView.setText(getTimeFromMillis(currentTrack.durationMs));
                mTrackNameTextView.setText(currentTrack.name);
                mArtistNameTextView.setText(currentTrack.artistName);
                mLoopTextView.setText(getTimeFromMillis((long)loopStart*1000) + " - " + getTimeFromMillis((long)loopEnd*1000));
                Picasso.with(getApplicationContext()).load(currentTrack.albumCoverWebUrl).into(mAlbumCoverImageView);
                Log.d("TickCount", Integer.toString((int)currentTrack.durationMs/1000 + 2));
                Log.d("currentTrack", currentTrack.toString());
                break;
            case kSpPlaybackNotifyPlay:
                Log.d("Playing", "Changing to pause image");
                mPlayImageButton.setImageDrawable(getDrawable(android.R.drawable.ic_media_pause));
                playbackHandler.removeCallbacks(checkMetadata);
                playbackHandler.post(checkMetadata);
                break;
            case kSpPlaybackNotifyPause:
                Log.d("Playing", "Changing to play image");
                mPlayImageButton.setImageDrawable(getDrawable(android.R.drawable.ic_media_play));
                break;
            case kSpPlaybackNotifyAudioDeliveryDone:
                playbackHandler.removeCallbacks(checkMetadata);
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
        loopStart = 5;
        loopEnd = 20;

        this.viewMyPlaylist();
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error e) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    public void togglePlay(View view) {
        Log.d("togglePlay", Long.toString(mPlayer.getPlaybackState().positionMs));
        Log.d("MainActivity", Integer.toString(loopEnd*1000));
        if (mPlayer.getPlaybackState().isPlaying) {
            mPlayer.pause(null);
        } else if (mPlayer.getPlaybackState().positionMs < loopEnd*1000){
            mPlayer.resume(null);
        } else {
            Log.d("WTF", "BOOM");
            Log.d("is playing?" , Boolean.toString(mPlayer.getPlaybackState().isPlaying));
            mPlayer.seekToPosition(null, loopStart*1000);
            mPlayer.resume(null);
        }
    }

    public void toggleReset(View view) {
        mPlayer.seekToPosition(null, loopStart*1000);
        if (!mPlayer.getPlaybackState().isPlaying) {
            mPlayer.resume(null);
        }
    }

    public void changeTrack(String uri) {
        mPlayer.playUri(null, uri, 0, 0);
    }

    public static String getTimeFromMillis(long millis) {
        return String.format(Locale.CANADA, "%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    public void viewMyPlaylist()
    {
        Intent intent = new Intent(MainActivity.this, SongListActivity.class);
        startActivityForResult(intent, SONG_SELECTION);
    }
    public void viewMyPlaylist(View view)
    {
        this.viewMyPlaylist();
    }

    protected void initializeValues() {
        mRangeBar = (RangeBar) findViewById(R.id.rangeBar);
        mDurationTextView = (TextView) findViewById(R.id.duration);
        mAlbumCoverImageView = (ImageView) findViewById(R.id.albumCover);
        mPlayImageButton = (ImageButton) findViewById(R.id.playButton);
        mTotalDurationTextView = (TextView) findViewById(R.id.totalDuration);
        mTrackNameTextView = (TextView) findViewById(R.id.trackName);
        mArtistNameTextView = (TextView) findViewById(R.id.artistName);
        mLoopTextView = (TextView) findViewById(R.id.loopStart);
        mRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int start, int end) {
                Log.d("RangeBar", Integer.toString(start) + " - " + Integer.toString(end));
                if (start != loopStart || end != loopEnd) {
                    loopStart = start;
                    loopEnd = end;
                    mLoopTextView.setText(getTimeFromMillis((long)loopStart*1000) + " - " + getTimeFromMillis((long)loopEnd*1000));
                    mPlayer.seekToPosition(null, start*1000);
                }
            }
        });
    }
}