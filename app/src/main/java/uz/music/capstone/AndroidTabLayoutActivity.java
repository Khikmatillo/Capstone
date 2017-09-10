package uz.music.capstone;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class AndroidTabLayoutActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();


        TabHost.TabSpec dailyspec = tabHost.newTabSpec("Daily");

        dailyspec.setIndicator("Daily");
        Intent photosIntent = new Intent(this, DailyActivity.class);
        dailyspec.setContent(photosIntent);


        TabHost.TabSpec weeklyspec = tabHost.newTabSpec("Weekly");
        weeklyspec.setIndicator("Weekly");
        Intent songsIntent = new Intent(this, WeeklyActivity.class);
        weeklyspec.setContent(songsIntent);


        TabHost.TabSpec monthlyspec = tabHost.newTabSpec("Monthly");
        monthlyspec.setIndicator("Monthly");
        Intent videosIntent = new Intent(this, MonthlyActivity.class);
        monthlyspec.setContent(videosIntent);

        tabHost.addTab(monthlyspec);
        tabHost.addTab(weeklyspec);
        tabHost.addTab(dailyspec);




    }
}
