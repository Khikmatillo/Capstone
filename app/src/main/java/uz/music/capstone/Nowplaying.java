package uz.music.capstone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import app.minimize.com.seek_bar_compat.SeekBarCompat;
import uz.music.capstone.IndexBottomSheetFragments.ProfileFragment;
import uz.music.capstone.json.JSONParserPlaylistMusics;
import uz.music.capstone.profile.User;

public class Nowplaying extends AppCompatActivity {

    public static MediaPlayer mediaPlayer = null;
    private Handler handler;
    private  ArrayList<PlaylistMusic> musics;
    private Intent intent;
    private int music_position;

    private RoundedImageView image;
    private SeekBarCompat seekBarCompat;
    private TextView txtCurrentTime, txtFullTime, txtCurrentMusic, txtCurrentArtist;
    private ImageButton btnPrev, btnPause, btnNext, btnLove, btnShuffle, btnRepeat, btnOptions;
    private boolean shuffle = false;
    private String likedMusicsJson = "";
    private ArrayList<PlaylistMusic> likedMusics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowplaying);

        handler = new Handler();
        intent = getIntent();

        music_position = intent.getIntExtra("position", 0);
        String callType = intent.getStringExtra("from");
        final String jsonText = intent.getStringExtra("json");
        if(callType.equals("PlaylistMusics")){
            JSONParserPlaylistMusics jsonParserPlaylistMusics = new JSONParserPlaylistMusics(jsonText);
            musics = jsonParserPlaylistMusics.getMusicsArray();
        }

        image = (RoundedImageView) findViewById(R.id.roundedImageView);
        seekBarCompat = (SeekBarCompat) findViewById(R.id.seekBarCompat);
        txtCurrentTime = (TextView) findViewById(R.id.txtCurrentTimeMusic);
        txtFullTime = (TextView) findViewById(R.id.txtFullTimeMusic);
        txtCurrentMusic = (TextView) findViewById(R.id.currentMusicName);
        txtCurrentArtist = (TextView) findViewById(R.id.currentMusicArtist);
        btnPrev = (ImageButton)findViewById(R.id.btnPrevNowPlaying);
        btnPause = (ImageButton)findViewById(R.id.btnPauseNowPlaying);
        btnNext = (ImageButton)findViewById(R.id.btnNextNowPlaying);
        btnLove = (ImageButton)findViewById(R.id.btnLove);
        btnShuffle = (ImageButton)findViewById(R.id.btnShuffle);
        btnRepeat = (ImageButton)findViewById(R.id.btnRepeat);
        btnOptions = (ImageButton)findViewById(R.id.btnOptions);

        if(musics.get(music_position).getPhotoLink() != null){
            Picasso.with(this)
                    .load(User.VARIABLE_URL + musics.get(music_position))
                    .into(image);
        }

        txtFullTime.setText(getTimeFromDuration(mediaPlayer.getDuration()));

        seekBarCompat.setMax(mediaPlayer.getDuration());
        seekUpdation();

        txtCurrentMusic.setText(musics.get(music_position).getName());

        new GetFavourites().execute(User.VARIABLE_URL + "/api/get-liked-musics/");

        if(btnLove.getTag().toString().equals("love")){
            btnLove.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
        }else{
            btnLove.setBackgroundResource(R.drawable.ic_fill_favorite_black_24dp);
        }

        btnLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", musics.get(music_position).getPk());
                    if(!isInList()){
                        jsonObject.put("action", "like");
                        likedMusics.add(new PlaylistMusic(txtCurrentMusic.getText().toString(), new ArrayList<String>(), "","", 0));
                        btnLove.setBackgroundResource(R.drawable.ic_fill_favorite_black_24dp);
                    }else{
                        jsonObject.put("action", "unlike");
                        PlaylistMusic music = new PlaylistMusic(txtCurrentMusic.getText().toString(), new ArrayList<String>(), "","", 0);
                        for(int i = 0; i < likedMusics.size(); i++){
                            if(likedMusics.get(i).getName().equals(txtCurrentMusic.getText().toString())){
                                music = likedMusics.get(i);
                            }
                        }
                        likedMusics.remove(music);
                        btnLove.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                    }
                    new LoveMusic().execute(jsonObject);
                }catch(JSONException e){

                }



            }
        });


        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null){
                    PlaylistMusic music;
                    int prevPosition;
                    if(!shuffle){
                        prevPosition = functionforPrev();
                    }else{
                        prevPosition = functionForShuffle();
                    }
                    music = musics.get(prevPosition);
                    try{
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(Nowplaying.this, Uri.parse(music.getLinks().get(0)));
                        mediaPlayer.start();
                        txtFullTime.setText(getTimeFromDuration(mediaPlayer.getDuration()));
                    }catch (Exception e){
                        Log.e("MediaPlayer ERROR", e.getMessage());
                    }
                    txtCurrentMusic.setText(music.getName());
                    btnPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null){
                    PlaylistMusic music;
                    int nextPosition;
                    if(!shuffle){
                        nextPosition = functionforNext();
                    }else{
                        nextPosition = functionForShuffle();
                    }
                    music = musics.get(nextPosition);
                    try{
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(Nowplaying.this, Uri.parse(music.getLinks().get(0)));
                        mediaPlayer.start();
                        txtFullTime.setText(getTimeFromDuration(mediaPlayer.getDuration()));
                    }catch (Exception e){
                        Log.e("MediaPlayer ERROR", e.getMessage());
                    }
                    txtCurrentMusic.setText(music.getName());
                    btnPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        btnPause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                    }else{
                        mediaPlayer.start();
                        btnPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    }
                }
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffle = true;
            }
        });

        seekBarCompat.setOnSeekBarChangeListener(new SeekBarCompat.OnSeekBarChangeListener() {
            int seek_bar_position = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    seek_bar_position = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer != null){
                    mediaPlayer.seekTo(seek_bar_position);
                }else{
                    seekBarCompat.setProgress(0);
                }

            }
        });


    }

    private int functionforPrev(){
        if(music_position != 0)
            music_position = music_position - 1;
        return music_position;
    }

    private int functionforNext(){
        if(music_position != musics.size() - 1)
            music_position = music_position + 1;
        return music_position;
    }

    private int functionForShuffle(){
        Random rand = new Random();
        return rand.nextInt(musics.size());
    }

    private String getTimeFromDuration(int duration){
        String time = "";
        time = duration / 60000 + ":" + (duration % 60000) / 1000;
        return time;
    }

    Runnable run = new Runnable() {

        @Override
        public void run() {
            seekUpdation();
        }
    };

    public void seekUpdation() {
        seekBarCompat.setProgress(mediaPlayer.getCurrentPosition());
        txtCurrentTime.setText(getTimeFromDuration(mediaPlayer.getCurrentPosition()));
        handler.postDelayed(run, 1000);
    }

    private boolean isInList(){
        for(int i = 0; i < likedMusics.size(); i++){
            if(txtCurrentMusic.getText().toString().equals(likedMusics.get(i).getName())){
                return true;
            }
        }
        return false;
    }


    private class LoveMusic extends AsyncTask<JSONObject, String, String> {
        private ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(JSONObject... jsonData) {
            try {

                URL url = new URL(User.VARIABLE_URL + "/api/music-like/"); // here is your URL path
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

                    User.USER_ACCEPTED = true;

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
            super.onPostExecute(result);
//            Toast.makeText(Nowplaying.this, result, Toast.LENGTH_SHORT).show();
            Log.e("DOWNLOADED JSON ", result);
        }
    }



    private class GetFavourites extends AsyncTask<String, String, String> {
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

            likedMusicsJson = result;
            JSONParserPlaylistMusics jsonParserPlaylistMusics = new JSONParserPlaylistMusics(result);
            likedMusics = jsonParserPlaylistMusics.getMusicsArray();
            if(isInList()){
                btnLove.setBackgroundResource(R.drawable.ic_fill_favorite_black_24dp);
            }
        }
    }


}
