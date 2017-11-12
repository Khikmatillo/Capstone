package uz.music.capstone.offline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import uz.music.capstone.MusicAdapter;
import uz.music.capstone.R;

/**
 * Created by Nemo on 11/12/2017.
 */

public class FragmentTracks extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_offline_tracks, container, false);
        MusicAdapter adapter = new MusicAdapter();
        ListView lw = (ListView) view.findViewById(R.id.offline_track_list);
        lw.setAdapter(adapter);
        MusicPlayer musicPlayer = new MusicPlayer(getActivity());
        musicPlayer.getAllSongs(adapter, 0);

        return view;
    }
}