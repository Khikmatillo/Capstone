package uz.music.capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Maestro on 7/31/2017.
 */

public class Music {

    private String music_name;
    private int position;
    private int num_of_views;
    private int best_position_ever;
    private String artist;
    private String genre;
    private ArrayList<String> links;
    private int music_pk;
    private int artist_id;

    public Music(String name, String artist){
        this.music_name = name;
        this.artist = artist;
    }
    public Music(String name, String artist, int artist_id){
        this.music_name = name;
        this.artist = artist;
        this.artist_id = artist_id;
    }

    public Music(String music_name, int position, int num_of_views, int best_position_ever,
                 String artist, String genre, ArrayList<String> links, int music_pk) {
        this.music_name = music_name;
        this.position = position;
        this.num_of_views = num_of_views;
        this.best_position_ever = best_position_ever;
        this.artist = artist;
        this.genre = genre;
        this.links = links;
        this.music_pk = music_pk;
    }

    public String getMusic_name() {
        return music_name;
    }

    public int getPosition() {
        return position;
    }

    public int getNum_of_views() {
        return num_of_views;
    }

    public int getBest_position_ever() {
        return best_position_ever;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public int getMusic_pk() {
        return music_pk;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setNum_of_views(int num_of_views) {
        this.num_of_views = num_of_views;
    }

    public void setBest_position_ever(int best_position_ever) {
        this.best_position_ever = best_position_ever;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }

    public void setMusic_pk(int music_pk) {
        this.music_pk = music_pk;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }
}
