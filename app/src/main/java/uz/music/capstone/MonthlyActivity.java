package uz.music.capstone;

/**
 * Created by Maestro on 9/6/2017.
 */

import android.app.Activity;
//package com.example.androidtablayout;

        import android.app.Activity;
        import android.os.Bundle;
import android.widget.ListView;

public class MonthlyActivity extends Activity {

    ListView listview1;
    ListViewAdapter adapter1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);


        listview1  = (ListView)findViewById(R.id.listviewmonthly); // listview of memo items

        adapter1 = new ListViewAdapter();
        listview1.setAdapter(adapter1);

        adapter1.addItem("Monthly Music 1");
        adapter1.addItem("Monthly Music 2");
        adapter1.addItem("Monthly Music 3");
        adapter1.addItem("Monthly Music 4");

    }
}