package uz.music.capstone;

/**
 * Created by Maestro on 9/6/2017.
 */

import android.app.Activity;
import android.os.Bundle;
//package com.example.androidtablayout;

        import android.app.Activity;
        import android.os.Bundle;
import android.widget.ListView;

public class WeeklyActivity extends Activity {

    ListView listview2;
    ListView listview3;
    ListViewAdapter adapter2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);

        listview2  = (ListView)findViewById(R.id.listviewweekly); // listview of memo items

        adapter2 = new ListViewAdapter();
        listview2.setAdapter(adapter2);

        adapter2.addItem("Weekly Music 1");
        adapter2.addItem("Weekly Music 2");
        adapter2.addItem("Weekly Music 3");
        adapter2.addItem("Weekly Music 4");
        adapter2.addItem("Weekly Music 5");


    }
}