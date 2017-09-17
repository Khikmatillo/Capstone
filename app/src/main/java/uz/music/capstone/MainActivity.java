package uz.music.capstone;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listview1;
    MusicAdapter adapter1;
    String selected_song = "";
    MediaPlayer current_playing = null, mp = null;
    Button btn_play_pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview1  = (ListView)findViewById(R.id.listviewmonthly); // listview of memo items

        adapter1 = new MusicAdapter();
        listview1.setAdapter(adapter1);


        /////////////////////////////////////////

        HTTPHandler httpHandler = new HTTPHandler();
        String url = "192.168.173.1:8000/daily/";
        String json_object = httpHandler.makeServiceCall(url);
        JSONParser jsonParser = new JSONParser(json_object);
        ArrayList<Music> responded_musics = jsonParser.getMusicsArray();
        for(int i = 0; i < responded_musics.size(); i++){
            adapter1.addItem(responded_musics.get(i).getMusic_name());
        }


        btn_play_pause = (Button) findViewById(R.id.btn_pause);

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music item = (Music) listview1.getItemAtPosition(position);
                selected_song = item.getMusic_name();
                Toast.makeText(MainActivity.this, selected_song, Toast.LENGTH_SHORT).show();
                int music_id = getResources().getIdentifier(selected_song, "raw", getPackageName());
                mp = MediaPlayer.create(MainActivity.this,
                        Uri.parse("http://vprbbc.streamguys.net/vprbbc24.mp3"));
                if(current_playing != null){
                    current_playing.stop();
                }
                mp.start();
                current_playing = mp;
            }
        });

        btn_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp != null){
                    if(mp.isPlaying()) {
                        mp.pause();
                    }
                }
            }
        });

        ////////////////////////////////////////////////




    }
}
