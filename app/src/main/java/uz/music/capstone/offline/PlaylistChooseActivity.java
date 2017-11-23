package uz.music.capstone.offline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import uz.music.capstone.Music;
import uz.music.capstone.MusicAdapter;
import uz.music.capstone.Playlist;
import uz.music.capstone.R;
import uz.music.capstone.profile.User;

/**
 * Created by Nemo on 11/17/2017.
 */

public class PlaylistChooseActivity extends AppCompatActivity {
    private RadioButton btn_select_all;
    private ListView list_view;
    private Button btn_add;
    private TextView txt_number;
    private Playlist playlist;
    private boolean selected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_playlist_choose);

        btn_select_all = (RadioButton)findViewById(R.id.radio_offline_playlist_select_all);
        list_view = (ListView) findViewById(R.id.list_offline_playlist_choose);
        btn_add = (Button) findViewById(R.id.btn_offline_playlist_add);
        txt_number = (TextView)findViewById(R.id.txt_offline_playlist_number);

        //list_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        MusicAdapter adapter = new MusicAdapter(1);
        list_view.setAdapter(adapter);

        MusicPlayer player = new MusicPlayer(this);
        player.getAllSongs(adapter, 0);

        final Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        playlist = new Playlist(name);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music music = (Music) list_view.getItemAtPosition(position);
                CheckBox checkBox  = (CheckBox) view.findViewById(R.id.check_playlist_choose);
                if(checkBox.isChecked()){
                    //uncheck
                    checkBox.setChecked(false);
                    playlist.deleteMusic(music);
                }else{
                    //check
                    checkBox.setChecked(true);
                    playlist.addMusic(music);
                }
                txt_number.setText(playlist.getMusics().size() + " selected");
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.PLAYLISTS.add(playlist);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        btn_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected){
                    //unselect
                    btn_select_all.setChecked(false);
                    selected = false;
                    for(int i = 0; i < list_view.getChildCount(); i++){
                        LinearLayout layout = (LinearLayout) list_view.getChildAt(i);
                        CheckBox cb = (CheckBox) layout.findViewById(R.id.check_playlist_choose);
                        cb.setChecked(false);
                        Music music = (Music) list_view.getItemAtPosition(i);
                        playlist.deleteMusic(music);
                    }
                }else{
                    //select
                    btn_select_all.setChecked(true);
                    selected = true;
                    for(int i = 0; i < list_view.getChildCount(); i++){
                        LinearLayout layout = (LinearLayout) list_view.getChildAt(i);
                        CheckBox cb = (CheckBox) layout.findViewById(R.id.check_playlist_choose);
                        cb.setChecked(true);
                        Music music = (Music) list_view.getItemAtPosition(i);
                        playlist.addMusic(music);
                    }
                }
                txt_number.setText(playlist.getMusics().size() + " selected");
            }
        });
    }
}
