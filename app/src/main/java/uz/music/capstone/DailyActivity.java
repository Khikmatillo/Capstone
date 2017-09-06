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


public class DailyActivity extends Activity {

    ListView listview;
    ListViewAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        listview  = (ListView)findViewById(R.id.listviewdaily); // listview of memo items

        adapter = new ListViewAdapter();
        listview.setAdapter(adapter);

        adapter.addItem("Daily Music 1");
        adapter.addItem("Daily Music 2");
        adapter.addItem("Daily Music 3");


    }
}