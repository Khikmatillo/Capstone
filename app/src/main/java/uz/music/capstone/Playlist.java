package uz.music.capstone;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import uz.music.capstone.profile.User;

/**
 * Created by Nemo on 11/19/2017.
 */

public class Playlist {
    private ArrayList<Music> musics;
    private String name;
    private String description;
    private String photoLink;
    public Playlist(String name){
        this.name = name;
        musics = new ArrayList<Music>();
    }

    public Playlist(String name, String description, String photoLink) {
        this.name = name;
        this.description = description;
        this.photoLink = photoLink;
    }


    public void addMusic(Music m){
        musics.add(m);
    }
    public void deleteMusic(Music m){
        musics.remove(m);
    }

    public ArrayList<Music> getMusics(){
        return musics;
    }

    public String getName() {
        return name;
    }

    public void saveMusics(Activity activity){
        String result=name;

        for(int i = 0; i < musics.size(); i++){
            result += musics.get(i).getArtist_id() + ",";
        }

        SharedPreferences sp = activity.getSharedPreferences(User.FILE_PLAYLISTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(name, result);
        editor.commit();
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoLink() {
        return photoLink;
    }
}
