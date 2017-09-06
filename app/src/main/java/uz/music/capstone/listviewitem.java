package uz.music.capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Maestro on 7/31/2017.
 */

public class listviewitem {

    private String titleStr;
    // date string array containing month, day, year

    public listviewitem(String titleStr) {
        this.titleStr = titleStr;

    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public String getTitleStr() {
        return titleStr;
    }


}
