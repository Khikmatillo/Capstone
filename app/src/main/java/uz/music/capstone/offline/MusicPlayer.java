package uz.music.capstone.offline;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

import uz.music.capstone.Music;
import uz.music.capstone.MusicAdapter;

/**
 * Created by Nemo on 11/12/2017.
 */

public class MusicPlayer {
    Activity activity;

    private ArrayList<String> folders;
    private ArrayList<ArrayList<Music>> folderMusics;

    private ArrayList<String> albums;
    private ArrayList<ArrayList<Music>> albumMusics;

    private ArrayList<String> artists;
    private ArrayList<ArrayList<Music>> artistMusics;

    public MusicPlayer(Activity activity){
        this.activity = activity;

        folders = new ArrayList<String >();
        folderMusics = new ArrayList<ArrayList<Music>>();
        albums = new ArrayList<String >();
        albumMusics = new ArrayList<ArrayList<Music>>();
        artists = new ArrayList<String >();
        artistMusics = new ArrayList<ArrayList<Music>>();
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
                    Music music = new Music(music_name, music_artist);
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
