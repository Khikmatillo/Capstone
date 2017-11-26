package uz.music.capstone;

import java.util.ArrayList;

/**
 * Created by Nemo on 11/26/2017.
 */

public class PlaylistMusic {
    private String name;
    private ArrayList<String> links;
    private String photoLink;
    private String file;
    private int pk;

    public PlaylistMusic(String name, ArrayList<String> links, String photoLink, String file, int pk) {
        this.name = name;
        this.links = links;
        this.photoLink = photoLink;
        this.file = file;
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public String getFile() {
        return file;
    }

    public int getPk() {
        return pk;
    }
}
