package uz.music.capstone;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import uz.music.capstone.IndexBottomSheetFragments.ProfileFragment;
import uz.music.capstone.json.JSONParserMusics;
import uz.music.capstone.json.JSONParserPlaylistMusics;
import uz.music.capstone.json.JSONParserPlaylists;
import uz.music.capstone.profile.User;


public class ListedMusicsActivity extends AppCompatActivity {

    private FloatingActionButton fab_start_play;
    private LinearLayout container_current_music;
    private ListView list_view;
    private TextView txt_name, txt_artist;
    private ImageButton btn_prev, btn_pause, btn_next;
    private ArrayList<Playlist> followedPlaylists;
    Toolbar toolbar;
    private Intent intent;


    private int music_position;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_musics);
//
        intent = getIntent();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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



        //check if playlist already followed
        new GetFollowingPlaylists().execute();

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
                    txt_artist.setText("");
                    Nowplaying.mediaPlayer.start();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("music_id", music.getPk());
                    new UpdateCharts().execute(jsonObject);

                } catch (Exception e) {
                    Toast.makeText(ListedMusicsActivity.this, "Url error", Toast.LENGTH_SHORT).show();
                    Log.e("MediaPlayer ERROR", e.getMessage());
                }
                btn_pause.setBackgroundResource(R.drawable.ic_pause_black_24dp);

            }
        });

        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                music_position = i;
                new GetUserPlaylists().execute(User.VARIABLE_URL + "/api/user-playlists/");

                return true;
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
                if(list_view.getChildCount() >= 1){
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
            }
        });
        fab_start_play.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0074b2")));
    }


    public void showMenu(View view) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                new GetUserPlaylists().execute(User.VARIABLE_URL + "/api/user-playlists/");


//                int id = item.getItemId();
//                switch (id) {
//                    case R.id.item_add:
//
//
//
//
//                        break;
//                }
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
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_follow) {
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("link", User.VARIABLE_URL + "/follow-playlist/");
                int playlistPk = intent.getIntExtra("pk", 0);
                if(playlistPk != 0){
                    if(!isInList()){
                        jsonObject.put("playlist_pk", playlistPk);
                        jsonObject.put("action", "follow");
                        new SendData().execute(jsonObject);
                        item.setIcon(R.drawable.ic_playlist_add_check_black_24dp);
                        followedPlaylists.add(new Playlist(getTitle().toString(), "", "", 0));
                    }else{
                        jsonObject.put("playlist_pk", playlistPk);
                        jsonObject.put("action", "unfollow");
                        new SendData().execute(jsonObject);
                        item.setIcon(R.drawable.ic_playlist_add_black_24dp);
                        Playlist p = new Playlist(getTitle().toString(), "", "", 0);
                        for(int i = 0; i < followedPlaylists.size(); i++){
                            if(getTitle().toString().equals(followedPlaylists.get(i).getName())){
                                p = followedPlaylists.get(i);
                            }
                        }
                        followedPlaylists.remove(p);
                    }
                }else{
                    Toast.makeText(ListedMusicsActivity.this, "You can only follow a playlist", Toast.LENGTH_SHORT).show();
                }


            }catch (JSONException e){

            }
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isInList(){
        for(int i = 0; i < followedPlaylists.size(); i++){
            if(getTitle().toString().equals(followedPlaylists.get(i).getName())){
                return true;
            }
        }
        return false;
    }

    private class GetUserPlaylists extends AsyncTask<String, String, String> {
        private ProgressDialog pd;
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                SharedPreferences sp = getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
                String token = sp.getString(User.KEY_TOKEN, "");
                connection.setRequestProperty("Authorization", "Token " + token);
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.e("Response: ", "> " + line);
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                Log.e("MafformedURLException", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Toast.makeText(ListedMusicsActivity.this, result, Toast.LENGTH_SHORT).show();

            JSONParserPlaylists jsonParserPlaylists = new JSONParserPlaylists(result);
            final ArrayList<Playlist> playlists = jsonParserPlaylists.getPlaylistsArray();

            CharSequence playlistNames[] = new CharSequence[playlists.size()];
            for(int i = 0; i < playlists.size(); i++){
                playlistNames[i] = playlists.get(i).getName();
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(ListedMusicsActivity.this);
            builder.setTitle("Choose a Playlis");
            builder.setItems(playlistNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                        PlaylistMusic music = (PlaylistMusic) list_view.getItemAtPosition(music_position);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("link", User.VARIABLE_URL + "/api/add-to-playlist/");
                        jsonObject.put("music_id", music.getPk());
                        jsonObject.put("playlist_id", playlists.get(which).getPk());
                        new SendData().execute(jsonObject);
                    }catch (JSONException e){

                    }
                }
            });
            builder.show();


        }

    }


    private class GetFollowingPlaylists extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(User.VARIABLE_URL + "/followed-playlist/");
                connection = (HttpURLConnection) url.openConnection();

                SharedPreferences sp = getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
                String token = sp.getString(User.KEY_TOKEN, "");
                connection.setRequestProperty("Authorization", "Token " + token);
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.e("Response: ", "> " + line);
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                Log.e("MafformedURLException", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONParserPlaylists jsonParserPlaylists = new JSONParserPlaylists(result);
            followedPlaylists = jsonParserPlaylists.getPlaylistsArray();
            if(isInList()){
                menu.getItem(0).setIcon(R.drawable.ic_playlist_add_check_black_24dp);
            }
        }
    }

    private class SendData extends AsyncTask<JSONObject, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(JSONObject... jsonData) {

            try {
                String urlString = jsonData[0].getString("link");
                URL url = new URL(urlString); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                SharedPreferences sp = getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
                String token = sp.getString(User.KEY_TOKEN, "");
                conn.setRequestProperty("Authorization", "Token " + token);

                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                jsonData[0].remove("link");
                String data = jsonData[0].toString();
                Log.e("Sending data", data);

                writer.write(data);
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(ListedMusicsActivity.this, result, Toast.LENGTH_LONG).show();
            Log.e("SEARCH RESULT:::", result);

        }
    }


    public class UpdateCharts extends AsyncTask<JSONObject, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(JSONObject... jsonData) {

            try {

                URL url = new URL(User.VARIABLE_URL + "/update-charts/"); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                SharedPreferences sp = getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
                String token = sp.getString(User.KEY_TOKEN, "");
                conn.setRequestProperty("Authorization", "Token " + token);

                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                String data = jsonData[0].toString();
                Log.e("Sending data", data);

                writer.write(data);
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("UPDATE RESULT:::", result);
//            if(!result.contains("false")){
//                Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                intent.putExtra("json", result);
//                startActivity(intent);
//            }

//            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();


        }
    }

}
