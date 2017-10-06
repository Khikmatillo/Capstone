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
    private String link;
    private String artist;
    private String genre;
    private ArrayList<String> additional_links;
    private int music_pk;



    public Music(String music_name, int position, int num_of_views, int best_position_ever, String link,
                 String artist, String genre, ArrayList<String> additional_links, int music_pk) {
        this.music_name = music_name;
        this.position = position;
        this.num_of_views = num_of_views;
        this.best_position_ever = best_position_ever;
        this.link = link;
        this.artist = artist;
        this.genre = genre;
        this.additional_links = additional_links;
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

    public String getLink() {
        return link;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public ArrayList<String> getAdditional_links() {
        return additional_links;
    }

    public int getMusic_pk() {
        return music_pk;
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

    public void setLink(String link) {
        this.link = link;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setAdditional_links(ArrayList<String> additional_links) {
        this.additional_links = additional_links;
    }

    public void setMusic_pk(int music_pk) {
        this.music_pk = music_pk;
    }
}
