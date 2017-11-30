package uz.music.capstone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import uz.music.capstone.IndexBottomSheetFragments.ProfileFragment;
import uz.music.capstone.json.JSONParserMusics;
import uz.music.capstone.json.JSONParserPlaylistMusics;
import uz.music.capstone.profile.User;


public class ListedMusicsActivity extends AppCompatActivity {

    private FloatingActionButton fab_start_play;
    private LinearLayout container_current_music;
    private ListView list_view;
    private TextView txt_name, txt_artist;
    private ImageButton btn_prev, btn_pause, btn_next;


    private Intent intent;


    private int music_position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_musics);
//
        intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(intent.getStringExtra("name"));


        fab_start_play = (FloatingActionButton) findViewById(R.id.fab);
        container_current_music = (LinearLayout) findViewById(R.id._currenttrackstatusXML);
        txt_name = (TextView) findViewById(R.id.music_title);
        txt_artist = (TextView) findViewById(R.id.music_artist);
        btn_prev = (ImageButton) findViewById(R.id.btn_prev);
        btn_pause = (ImageButton) findViewById(R.id.btn_pause);
        btn_next = (ImageButton) findViewById(R.id.btn_next);
        list_view = (ListView) findViewById(R.id.list_view);


        PlaylistMusicsAdapter adapter = new PlaylistMusicsAdapter();
        list_view.setAdapter(adapter);

        final String jsonText = intent.getStringExtra("json");
        Log.e("RECEIVED JSON DATA", jsonText);
        JSONParserPlaylistMusics jsonParserPlaylistMusics = new JSONParserPlaylistMusics(jsonText);
        ArrayList<PlaylistMusic> playlistMusics = jsonParserPlaylistMusics.getMusicsArray();

        for (int i = 0; i < playlistMusics.size(); i++) {
            adapter.addItem(playlistMusics.get(i));
        }
        adapter.notifyDataSetChanged();


        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                music_position = i;
                PlaylistMusic music = (PlaylistMusic) list_view.getItemAtPosition(i);
                if (Nowplaying.mediaPlayer != null) {
                    Nowplaying.mediaPlayer.stop();
                }
                try {
                    Nowplaying.mediaPlayer = MediaPlayer.create(ListedMusicsActivity.this, Uri.parse(music.getLinks().get(0)));
                    container_current_music.setVisibility(View.VISIBLE);
                    txt_name.setText(music.getName());
                    txt_artist.setText(music.getFile());
                    Nowplaying.mediaPlayer.start();
                } catch (Exception e) {
                    Toast.makeText(ListedMusicsActivity.this, "Url error", Toast.LENGTH_SHORT).show();
                    Log.e("MediaPlayer ERROR", e.getMessage());
                }
                btn_pause.setBackgroundResource(R.drawable.ic_pause_black_24dp);

            }
        });


        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Nowplaying.mediaPlayer != null) {
                    PlaylistMusic music;
                    if (music_position != 0) {
                        music = (PlaylistMusic) list_view.getItemAtPosition(music_position - 1);
                        music_position = music_position - 1;
                    } else {
                        music = (PlaylistMusic) list_view.getItemAtPosition(music_position);
                    }
                    try {
                        Nowplaying.mediaPlayer.stop();
                        Nowplaying.mediaPlayer = MediaPlayer.create(ListedMusicsActivity.this, Uri.parse(music.getLinks().get(0)));
                        Nowplaying.mediaPlayer.start();
                    } catch (Exception e) {
                        Log.e("MediaPlayer ERROR", e.getMessage());
                    }
                    txt_name.setText(music.getName());
                    txt_artist.setText(music.getFile());
                    btn_pause.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                }

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Nowplaying.mediaPlayer != null) {
                    PlaylistMusic music;
                    if (music_position != list_view.getChildCount()) {
                        music = (PlaylistMusic) list_view.getItemAtPosition(music_position + 1);
                        music_position = music_position + 1;
                    } else {
                        music = (PlaylistMusic) list_view.getItemAtPosition(music_position);
                    }
                    try {
                        Nowplaying.mediaPlayer.stop();
                        Nowplaying.mediaPlayer = MediaPlayer.create(ListedMusicsActivity.this, Uri.parse(music.getLinks().get(0)));
                        Nowplaying.mediaPlayer.start();
                    } catch (Exception e) {
                        Log.e("MediaPlayer ERROR", e.getMessage());
                    }
                    txt_name.setText(music.getName());
                    txt_artist.setText(music.getFile());
                    btn_pause.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                }

            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Nowplaying.mediaPlayer != null) {
                    if (Nowplaying.mediaPlayer.isPlaying()) {
                        Nowplaying.mediaPlayer.pause();
                        btn_pause.setBackgroundResource(R.drawable.ic_play_black_24dp);
                    } else {
                        Nowplaying.mediaPlayer.start();
                        btn_pause.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    }
                }
            }
        });

        container_current_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Nowplaying.class);
                i.putExtra("json", jsonText);
                i.putExtra("from", "PlaylistMusics");
                i.putExtra("position", music_position);
                startActivity(i);
            }
        });

        fab_start_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music_position = 0;
                PlaylistMusic music = (PlaylistMusic) list_view.getItemAtPosition(0);
                if (Nowplaying.mediaPlayer != null) {
                    Nowplaying.mediaPlayer.stop();
                }
                try {
                    Nowplaying.mediaPlayer = MediaPlayer.create(ListedMusicsActivity.this, Uri.parse(music.getLinks().get(0)));
                    container_current_music.setVisibility(View.VISIBLE);
                    txt_name.setText(music.getName());
                    txt_artist.setText(music.getFile());
                    Nowplaying.mediaPlayer.start();
                } catch (Exception e) {
                    Toast.makeText(ListedMusicsActivity.this, "Url error", Toast.LENGTH_SHORT).show();
                    Log.e("MediaPlayer ERROR", e.getMessage());
                }
                btn_pause.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            }
        });
        fab_start_play.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0074b2")));
    }


    public void showMenu(View view) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.item_download:
                        Toast.makeText(getApplicationContext(), "Download", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.item_add:
                        Toast.makeText(getApplicationContext(), "Add", Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
        menu.inflate(R.menu.music_item_option);
        menu.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
//            Intent intent = new Intent(ListedMusicsActivity.this, ProfileFragment.class);
//            startActivity(intent);
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
