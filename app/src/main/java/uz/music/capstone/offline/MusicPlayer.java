package uz.music.capstone.offline;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;

import uz.music.capstone.MainActivity;
import uz.music.capstone.Music;
import uz.music.capstone.MusicAdapter;

/**
 * Created by Nemo on 11/12/2017.
 */

public class MusicPlayer {
    Activity activity;

    private static ArrayList<String> folders;
    private static ArrayList<ArrayList<Music>> folderMusics;

    private ArrayList<String> albums;
    private ArrayList<ArrayList<Music>> albumMusics;

    private ArrayList<String> artists;
    private ArrayList<ArrayList<Music>> artistMusics;

    private MediaPlayer current_playing = null, mp = null;
    private SeekBar music_seek_bar;
    private Handler music_handler;
    private Music music_current = null, music_prev = null, music_next = null;
    private ArrayList<Music> ordered_musics;

    private int music_paused_position, music_current_position;
    private boolean paused = false;

    public MusicPlayer(Activity activity){
        this.activity = activity;

        folders = new ArrayList<String >();
        folderMusics = new ArrayList<ArrayList<Music>>();
        albums = new ArrayList<String >();
        albumMusics = new ArrayList<ArrayList<Music>>();
        artists = new ArrayList<String >();
        artistMusics = new ArrayList<ArrayList<Music>>();
        music_handler = new Handler();
        ordered_musics = new ArrayList<Music>();
    }

    public void getAllSongs(MusicAdapter adapter, int mode) {

        Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] STAR=null;
        Cursor cursor = activity.getContentResolver().query(allsongsuri, STAR, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String music_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String music_artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String artist_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                    String link = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    ArrayList<String> links = new ArrayList<String>();
                    links.add(link);
                    Music music = new Music(music_name, music_artist, artist_id, links);
                    ordered_musics.add(music);
                    if(adapter != null)
                        adapter.addItem(music);

                    //mode 0 ==> tracks
                    //mode 1 ==> folders
                    //mode 2 ==> albums
                    //mode 3 ==> artists
                    //mode 4 ==> playlists

                    if(mode == 0){

                    }else if(mode == 1){
                        String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        String[] paths = fullpath.split("/");
                        String folder = paths[paths.length - 2];
                        addMusicToGroup(folder, music, folders, folderMusics);
                    }else if(mode == 2){
                        String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                        addMusicToGroup(album, music, albums, albumMusics);
                    }else if(mode == 3){
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        addMusicToGroup(artist, music, artists, artistMusics);
                    }else if(mode == 4){

                    }


                } while (cursor.moveToNext());
                if(adapter != null)
                    adapter.notifyDataSetChanged();
            }
            cursor.close();
        }
    }

    public void playPauseButtonAction(){
        if(current_playing != null){
            if(!paused){
                music_paused_position = current_playing.getCurrentPosition();
                current_playing.pause();
                paused = true;
            }else{
                current_playing.seekTo(music_paused_position);
                current_playing.start();
                paused = false;
            }

        }
    }

    public void nextButtonAction(){
        if(current_playing != null && music_next != null){

            music_prev = music_current;
            music_current = music_next;
            music_next = ordered_musics.get((music_current_position + 1) % ordered_musics.size());
            mp = MediaPlayer.create(activity, Uri.parse(music_current.getLinks().get(0)));
            current_playing.stop();
            current_playing = mp;
            current_playing.start();

        }
    }

    public void prevButtonAction(){
        if(current_playing != null && music_prev != null){

            music_next = music_current;
            music_current = music_prev;
            if(music_current_position == 0){
                music_prev = ordered_musics.get(ordered_musics.size() - 1);
            }else{
                music_prev = ordered_musics.get(music_current_position - 1);
            }
            mp = MediaPlayer.create(activity, Uri.parse(music_current.getLinks().get(0)));
            current_playing.stop();
            current_playing = mp;
            current_playing.start();

        }
    }

    public void addMusicToGroup(String folder, Music music, ArrayList<String> group, ArrayList<ArrayList<Music>> groupMusics){
        int index = -1;
        for(int i = 0; i < group.size(); i++){
            if(group.get(i).equals(folder)){
                index = i;
            }
        }
        if(index == -1){
            groupMusics.add(new ArrayList<Music>());
            group.add(folder);
            index = groupMusics.size() - 1;
        }
        groupMusics.get(index).add(music);
    }

    public void playMusic(ListView listView, int position){
        music_current_position = position;
        music_current = (Music) listView.getItemAtPosition(position);
        Toast.makeText(activity, music_current.getMusic_name(), Toast.LENGTH_SHORT).show();
        if(music_current.getLinks().get(0) != null) {
            mp = MediaPlayer.create(activity, Uri.parse(music_current.getLinks().get(0)));
        }
        if(current_playing != null){
            current_playing.stop();
        }
        current_playing = mp;
        current_playing.start();

        music_next = ordered_musics.get((position + 1) % ordered_musics.size());
        if(position == 0){
            music_prev = ordered_musics.get(ordered_musics.size() - 1);
        }else{
            music_prev = ordered_musics.get(position - 1);
        }
    }

    public void seekBarControl(SeekBar seek_bar){
        music_seek_bar = seek_bar;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mp != null){
                    int music_current_position = mp.getCurrentPosition()/1000;
                    music_seek_bar.setProgress(music_current_position);
                }
                music_handler.postDelayed(this, 0);
            }
        });

        music_seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seek_bar_position = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    seek_bar_position = progress * 1000;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mp != null){
                    mp.seekTo(seek_bar_position);
                }else{
                    music_seek_bar.setProgress(0);
                }

            }
        });

    }


    public ArrayList<String> getFolders() {
        return folders;
    }

    public ArrayList<ArrayList<Music>> getFolderMusics() {
        return folderMusics;
    }

    public ArrayList<String> getAlbums() {
        return albums;
    }

    public ArrayList<ArrayList<Music>> getAlbumMusics() {
        return albumMusics;
    }

    public ArrayList<String> getArtists() {
        return artists;
    }

    public ArrayList<ArrayList<Music>> getArtistMusics() {
        return artistMusics;
    }
}
