package uz.music.capstone;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import app.minimize.com.seek_bar_compat.SeekBarCompat;
import uz.music.capstone.json.JSONParserPlaylistMusics;

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
                    .load("http://moozee.pythonanywhere.com" + musics.get(music_position))
                    .into(image);
        }

        txtFullTime.setText(getTimeFromDuration(mediaPlayer.getDuration()));

        seekBarCompat.setMax(mediaPlayer.getDuration());
        seekUpdation();

        txtCurrentMusic.setText(musics.get(music_position).getName());

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
                    btnPause.setBackgroundResource(R.drawable.pausebutton);
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
                    btnPause.setBackgroundResource(R.drawable.pausebutton);
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        btnPause.setBackgroundResource(R.drawable.playbutton);
                    }else{
                        mediaPlayer.start();
                        btnPause.setBackgroundResource(R.drawable.pausebutton);
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

}
