package uz.music.capstone.offline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import uz.music.capstone.Music;
import uz.music.capstone.R;

/**
 * Created by Nemo on 11/12/2017.
 */

public class FragmentFolders extends Fragment {

    MusicPlayer musicPlayer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_offline_folders, container, false);


        musicPlayer = new MusicPlayer(getActivity());
        musicPlayer.getAllSongs(null, 1);
        Log.e("FolDERS ARRAY :: ", musicPlayer.getFolders().size() + "");
        CustomAdapter adapter = new CustomAdapter();
        ListView lw = (ListView) view.findViewById(R.id.offline_folders_list);
        lw.setAdapter(adapter);
        for(int i = 0; i < musicPlayer.getFolders().size(); i++){
            adapter.addItem(musicPlayer.getFolders().get(i), musicPlayer.getFolderMusics().get(i));
        }
        adapter.notifyDataSetChanged();

        return view;
    }
}