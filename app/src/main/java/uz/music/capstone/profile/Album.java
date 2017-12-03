package uz.music.capstone.profile;

/**
 * Created by Nemo on 12/2/2017.
 */

public class Album {
    String name;
    String artist;
    String photoLink;
    int pk;

    public Album(String name, String artist, String photoLink, int pk) {
        this.name = name;
        this.artist = artist;
        this.photoLink = photoLink;
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public int getPk() {
        return pk;
    }
}
