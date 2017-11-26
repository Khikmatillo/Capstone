package uz.music.capstone;

/**
 * Created by Nemo on 11/26/2017.
 */

public class Genre {
    private String name;
    private String photoLink;

    public Genre(String name, String photoLink) {
        this.name = name;
        this.photoLink = photoLink;
    }

    public String getName() {
        return name;
    }

    public String getPhotoLink() {
        return photoLink;
    }
}
