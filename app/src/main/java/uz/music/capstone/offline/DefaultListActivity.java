package uz.music.capstone.offline;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import uz.music.capstone.Music;
import uz.music.capstone.MusicAdapter;
import uz.music.capstone.R;

/**
 * Created by Nemo on 11/19/2017.
 */

public class DefaultListActivity extends AppCompatActivity {

    LinearLayout layout;
    TextView txt_name, txt_artist;
    ImageButton btn_prev, btn_play, btn_next;
    SeekBar seek_bar;
    ListView list_view;
    MusicPlayer musicPlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_offline_tracks);

        MusicAdapter adapter = new MusicAdapter();
        layout = (LinearLayout) findViewById(R.id.container_offline_tracks);
        txt_name = (TextView)findViewById(R.id.txt_title_offline_tracks);
        txt_artist = (TextView)findViewById(R.id.txt_artist_offline_tracks);
        btn_prev = (ImageButton)findViewById(R.id.btn_prev_offline_tracks);
        btn_play = (ImageButton)findViewById(R.id.btn_pause_offline_tracks);
        btn_next = (ImageButton)findViewById(R.id.btn_next_offline_tracks);
        seek_bar = (SeekBar)findViewById(R.id.seek_bar_offline_tracks);
        musicPlayer = new MusicPlayer(DefaultListActivity.this);

        list_view = (ListView) findViewById(R.id.offline_track_list);
        list_view.setAdapter(adapter);

        final MusicPlayer musicPlayer = new MusicPlayer(DefaultListActivity.this);


        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        for(int i = 0; i < musicPlayer.getFolderMusics().get(position).size(); i++){
            adapter.addItem(musicPlayer.getFolderMusics().get(position).get(i));
        }
        adapter.notifyDataSetChanged();

        musicPlayer.seekBarControl(seek_bar);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music music = (Music) list_view.getItemAtPosition(position);
                layout.setVisibility(View.VISIBLE);
                txt_name.setText(music.getMusic_name());
                txt_artist.setText(music.getArtist());
                musicPlayer.playMusic(list_view, position);
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.playPauseButtonAction();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.nextButtonAction();
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.prevButtonAction();
            }
        });

    }
}
