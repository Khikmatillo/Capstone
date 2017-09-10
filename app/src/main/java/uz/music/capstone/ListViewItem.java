package uz.music.capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Maestro on 7/31/2017.
 */

public class ListViewItem {

    private String titleStr;
    //private String dayStr;
    //private String monthYearStr;
    // date string array containing month, day, year

    public ListViewItem(String titleStr) {
        this.titleStr = titleStr;

    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public String getTitleStr() {
        return titleStr;
    }


}
