package uz.music.capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Maestro on 7/31/2017.
 */

public class Music {

    private String title;
    private String author;
    private int daily_plays;
    private int weekly_plays;
    private int monthly_plays;

    public Music(String title) {
        this.title = title;
    }

    public Music(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Music(String title, String author, int daily_plays, int weekly_plays, int monthly_plays) {
        this.title = title;
        this.author = author;
        this.daily_plays = daily_plays;
        this.weekly_plays = weekly_plays;
        this.monthly_plays = monthly_plays;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDaily_plays(int daily_plays) {
        this.daily_plays = daily_plays;
    }

    public void setWeekly_plays(int weekly_plays) {
        this.weekly_plays = weekly_plays;
    }

    public void setMonthly_plays(int monthly_plays) {
        this.monthly_plays = monthly_plays;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getDaily_plays() {
        return daily_plays;
    }

    public int getWeekly_plays() {
        return weekly_plays;
    }

    public int getMonthly_plays() {
        return monthly_plays;
    }
}
