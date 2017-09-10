package uz.music.capstone;

/**
 * Created by Maestro on 9/6/2017.
 */

import android.app.Activity;
//package com.example.androidtablayout;

        import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MonthlyActivity extends Activity {

    ListView listview1;
    ListViewAdapter adapter1;
    String selected_song = "";
    MediaPlayer current_playing = null, mp = null;
    Button btn_play_pause;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);


        listview1  = (ListView)findViewById(R.id.listviewmonthly); // listview of memo items

        adapter1 = new ListViewAdapter();
        listview1.setAdapter(adapter1);


        /////////////////////////////////////////

        btn_play_pause = (Button) findViewById(R.id.btn_pause);

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem) listview1.getItemAtPosition(position);
                selected_song = item.getTitleStr();
                Toast.makeText(MonthlyActivity.this, selected_song, Toast.LENGTH_SHORT).show();
                int music_id = getResources().getIdentifier(selected_song, "raw", getPackageName());
                mp = MediaPlayer.create(MonthlyActivity.this, music_id);
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

        Field[] fields = R.raw.class.getFields();
        for(int i = 0; i < fields.length; i++){
            if(i == 0 || i == 3){
                continue;
            }
            adapter1.addItem(fields[i].getName());
        }
        ////////////////////////////////////////////////



    }
}