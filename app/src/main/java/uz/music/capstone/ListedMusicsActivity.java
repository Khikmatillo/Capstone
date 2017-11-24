package uz.music.capstone;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import uz.music.capstone.IndexBottomSheetFragments.ProfileFragment;
import uz.music.capstone.profile.User;


public class ListedMusicsActivity extends AppCompatActivity
        {

    private ListView listview1;
    private MusicAdapter adapter1;
    private MediaPlayer current_playing = null, mp = null;
    private ImageButton btn_play_pause, btn_next, btn_prev, option_menu;
    private ImageView imgbar;
    private SeekBar music_seek_bar;
    private Handler music_handler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton startPlay;
    private LinearLayout currentTrack;

    private int music_paused_position;
    private ArrayList<Music> ordered_musics;
    private Music music_current = null, music_prev = null, music_next = null;
    private int music_current_position;
    private File json_file = null;
    private boolean paused = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_musics);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Favorite");

        listview1  = (ListView)findViewById(R.id.list_view); // listview of memo items

        adapter1 = new MusicAdapter();
        listview1.setAdapter(adapter1);
        btn_play_pause = (ImageButton) findViewById(R.id.btn_pause);
        btn_next = (ImageButton) findViewById(R.id.btn_next);
        btn_prev = (ImageButton) findViewById(R.id.btn_prev);
        imgbar = (ImageView) findViewById(R.id.imgbar);
        adapter1.notifyDataSetChanged();
        //music_seek_bar = (SeekBar) findViewById(R.id.music_seek_bar);
        music_handler = new Handler();
        ordered_musics = new ArrayList<Music>();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        startPlay = (FloatingActionButton) findViewById(R.id.fab);
        currentTrack = (LinearLayout)findViewById(R.id._currenttrackstatusXML);





        startPlay.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#0074b2")));

        //get JSON and parse JSON starts ------------------------------------
//        SharedPreferences sp1 = getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
//        String savedJson = sp1.getString(User.KEY_JSON, "");
//        if (savedJson.equals("")) {
//            new GetJson().execute("http://moozee.pythonanywhere.com/daily/?format=json");
//        }else{
//            Log.i("JSON", savedJson);
//            parseJson(savedJson);
//            Toast.makeText(ListedMusicsActivity.this, "Reading from SharedPreferences", Toast.LENGTH_SHORT).show();
//        }

        //get JSON and parse JSON ends --------------------------------------

//        final String[] option = {"Hello", "Adele", "Add to playlist", "Share"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, option);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Option menu");
//        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        final AlertDialog alertDialog = builder.create();
//        option_menu = (ImageButton)findViewById(R.id.menu_button);
//        option_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.show();
//            }
//        });

        //ooffline mode starts

//        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            if (shouldShowRequestPermissionRationale(
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            }
//
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);
//
//            return;
//        }
//        getAllSongs();


        //ooffline mode ends


        /////////////////////////////////////////



        currentTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Nowplaying.class);
                startActivity(i);
            }
        });



        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                music_current_position = position;
                music_current = (Music) listview1.getItemAtPosition(music_current_position);
                Toast.makeText(ListedMusicsActivity.this, music_current.getMusic_name(), Toast.LENGTH_SHORT).show();
                if(music_current.getLinks().get(0) != null) {
                    mp = MediaPlayer.create(ListedMusicsActivity.this, Uri.parse(music_current.getLinks().get(0)));
                }
                if(current_playing != null){
                    current_playing.stop();
                }
                current_playing = mp;
                current_playing.start();

                music_next = ordered_musics.get((music_current_position + 1) % ordered_musics.size());
                if(music_current_position == 0){
                    music_prev = ordered_musics.get(ordered_musics.size() - 1);
                }else{
                    music_prev = ordered_musics.get(music_current_position - 1);
                }

            }
        });

        //listview1.setOnTouchListener();

        btn_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_playing != null){
                    if(!paused){
                        music_paused_position = current_playing.getCurrentPosition();
                        current_playing.pause();
                        paused = true;
                    }else{
                        current_playing.seekTo(music_paused_position);
                        current_playing.start();
                        paused = false;
                    }

                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_playing != null && music_next != null){

                    music_prev = music_current;
                    music_current = music_next;
                    music_next = ordered_musics.get((music_current_position + 1) % ordered_musics.size());
                    mp = MediaPlayer.create(ListedMusicsActivity.this, Uri.parse(music_current.getLinks().get(0)));
                    current_playing.stop();
                    current_playing = mp;
                    current_playing.start();

                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_playing != null && music_prev != null){

                    music_next = music_current;
                    music_current = music_prev;
                    if(music_current_position == 0){
                        music_prev = ordered_musics.get(ordered_musics.size() - 1);
                    }else{
                        music_prev = ordered_musics.get(music_current_position - 1);
                    }
                    mp = MediaPlayer.create(ListedMusicsActivity.this, Uri.parse(music_current.getLinks().get(0)));
                    current_playing.stop();
                    current_playing = mp;
                    current_playing.start();

                }

            }
        });
        //control seekbar starts -----------------------------------------
        ListedMusicsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mp != null){
                    int music_current_position = mp.getCurrentPosition()/1000;
                    music_seek_bar.setProgress(music_current_position);
                }
                music_handler.postDelayed(this, 0);
            }
        });

//        music_seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int seek_bar_position = 0;
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if(fromUser){
//                    seek_bar_position = progress * 1000;
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                if(mp != null){
//                    mp.seekTo(seek_bar_position);
//                }else{
//                    music_seek_bar.setProgress(0);
//                }
//
//            }
//        });

        //control seekbar ends -------------------------------------------

        ////////////////////////////////////////////////




        //OnRefreshListener Implementations starts ---------------------------------
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter1.clearAllData();
//                new GetJson().execute("http://moozee.pythonanywhere.com/daily/?format=json");
                getAllSongs();
            }
        });

        //OnRefreshListener Implementations ends ---------------------------------
    }


    //offline mode functions starts ---------------------------------------------------------


    public void getAllSongs() {

        Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] STAR=null;
        Cursor cursor = managedQuery(allsongsuri, STAR, selection, null, null);


        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String music_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String music_artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    int song_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String Duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    //String content_type = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.CONTENT_TYPE));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    //Music music = new Music(music_name, music_artist);
                    //adapter1.addItem(music);
                    Log.e("Data", "Path :: " + fullpath);

                } while (cursor.moveToNext());
                adapter1.notifyDataSetChanged();
            }
            cursor.close();
            swipeRefreshLayout.setRefreshing(false);
        }
    }



    public void showMenu (View view)
    {
        PopupMenu menu = new PopupMenu (this, view);
        menu.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener ()
        {
            @Override
            public boolean onMenuItemClick (MenuItem item)
            {
                int id = item.getItemId();
                switch (id)
                {
                    case R.id.item_settings: Toast.makeText(getApplicationContext(), "Setting", Toast.LENGTH_LONG).show(); break;
                    case R.id.item_about: Toast.makeText(getApplicationContext(), "Setting", Toast.LENGTH_LONG).show(); break;
                }
                return true;
            }
        });
        menu.inflate (R.menu.music_item_option);
        menu.show();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1234) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.

                Toast.makeText(ListedMusicsActivity.this, "grant", Toast.LENGTH_LONG).show();
                getAllSongs();
            } else {
                // User refused to grant permission.
                Toast.makeText(ListedMusicsActivity.this, "denied", Toast.LENGTH_LONG).show();
            }
        }
    }



    //offline mode functions starts ---------------------------------------------------------






    private void parseJson(String result){
        JSONParser jsonParser = new JSONParser(result);
        ordered_musics = jsonParser.getMusicsArray();
        Log.e("", "Array size " + ordered_musics.size());
        Log.e("", "JSON" + result);
        for(int i = 0; i < ordered_musics.size(); i++){
            adapter1.addItem(ordered_musics.get(i));
        }
        adapter1.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }


    //class to GET and Parse from server starts ---------------------------------
    private class GetJson extends AsyncTask<String, String, String> {
        private ProgressDialog pd;
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ListedMusicsActivity.this, "Downloading JSON data", Toast.LENGTH_SHORT).show();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                SharedPreferences sp = getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
                String token = sp.getString(User.KEY_TOKEN, "");
                connection.setRequestProperty("Authorization", "Token " + token);
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.e("Response: ", "> " + line);
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                Log.e("MafformedURLException", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(ListedMusicsActivity.this, "Download process finish", Toast.LENGTH_SHORT).show();

            //Parsing the JSON starts --------------------------------------
            parseJson(result);

            SharedPreferences shp = getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shp.edit();
            editor.putString(User.KEY_JSON, result);
            editor.commit();
            //Parsing the JSON ends --------------------------------------
        }
    }
    //class to GET and Parse from server ends ---------------------------------


    //POST changes to server starts --------------------------------------

    //POST changes to server ends ----------------------------------------


    //navigation bar begins


//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Intent intent = new Intent(ListedMusicsActivity.this, ProfileFragment.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}
