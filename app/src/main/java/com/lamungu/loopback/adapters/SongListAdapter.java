package com.lamungu.loopback.adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lamungu.loopback.R;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.models.PlaylistTrack;

public class SongListAdapter extends ArrayAdapter<PlaylistTrack> {

    public SongListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SongListAdapter(Context context, int resource, List<PlaylistTrack> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_playlist_track, null);
        }

        PlaylistTrack playlistTrack = getItem(position);

        if (playlistTrack != null) {
            TextView name = (TextView) v.findViewById(R.id.playlistTrackName);
            TextView artist = (TextView) v.findViewById(R.id.playlistTrackArtist);
            TextView id = (TextView) v.findViewById(R.id.playlistTrackId);
            TextView duration = (TextView) v.findViewById(R.id.playlistTrackDuration);

            if (name != null) {
                name.setText(playlistTrack.track.name);
            }

            if (artist != null) {
                artist.setText(playlistTrack.track.artists.get(0).name);
            }

            if (id != null) {
                id.setText(playlistTrack.track.uri);
            }

            if (duration != null) {
                duration.setText(getTimeFromMillis(playlistTrack.track.duration_ms));
            }
        }

        return v;
    }


    public static String getTimeFromMillis(long millis) {
        return String.format(Locale.CANADA, "%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
}
