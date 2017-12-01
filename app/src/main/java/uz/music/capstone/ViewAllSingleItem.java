package uz.music.capstone;

/**
 * Created by Maestro on 11/30/2017.
 */

public class ViewAllSingleItem {

    private String name;
    private String detail;
    private int thumbnail;

    public ViewAllSingleItem() {
    }

    public ViewAllSingleItem(String name, String detail, int thumbnail) {
        this.name = name;
        this.detail = detail;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
