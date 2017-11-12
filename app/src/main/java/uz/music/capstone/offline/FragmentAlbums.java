package uz.music.capstone.offline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import uz.music.capstone.R;

/**
 * Created by Nemo on 11/12/2017.
 */

public class FragmentAlbums extends Fragment {

    MusicPlayer musicPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_offline_albums, container, false);

        musicPlayer = new MusicPlayer(getActivity());
        musicPlayer.getAllSongs(null, 2);
        Log.e("ALBUMS ARRAY :: ", musicPlayer.getAlbums().size() + "");
        CustomAdapter adapter = new CustomAdapter();
        ListView lw = (ListView) view.findViewById(R.id.offline_albums_list);
        lw.setAdapter(adapter);
        for(int i = 0; i < musicPlayer.getAlbums().size(); i++){
            adapter.addItem(musicPlayer.getAlbums().get(i), musicPlayer.getAlbumMusics().get(i));
        }
        adapter.notifyDataSetChanged();

        return view;
    }
}