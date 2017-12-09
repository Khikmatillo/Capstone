package uz.music.capstone;

/**
 * Created by Maestro on 11/30/2017.
 */

public class ViewAllSingleItem {

    private String name;
    private String detail;
    private String thumbnail;
    private int pk;

    public ViewAllSingleItem() {
    }

    public ViewAllSingleItem(String name, String detail, String thumbnail, int pk) {
        this.name = name;
        this.detail = detail;
        this.thumbnail = thumbnail;
        this.pk = pk;
    }

    public int getPk() {
        return pk;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
