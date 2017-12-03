package uz.music.capstone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import uz.music.capstone.json.JSONParserPlaylistMusics;
import uz.music.capstone.json.JSONParserPlaylists;
import uz.music.capstone.json.PostRequestForJSON;
import uz.music.capstone.profile.User;

/**
 * Created by Nemo on 12/1/2017.
 *
 */

public class UserPlaylistsActivity extends AppCompatActivity {

    FloatingActionButton txtCreate;
    LinearLayout containerMyP, containerFollowedP;
    boolean parsingMy = false, parsingFollowed = false, callForFollowed = false;
    private String currentName;
    private int currentPk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_playlist);
        txtCreate = (FloatingActionButton) findViewById(R.id.fab1);
        containerMyP = (LinearLayout)findViewById(R.id.containerMyPlaylists);
        containerFollowedP = (LinearLayout)findViewById(R.id.containerFollowedPlaylists);

        txtCreate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0074b2")));

        txtCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(UserPlaylistsActivity.this, PlaylistCreateActivity.class);
                startActivityForResult(intent1, 999);
            }
        });

        parsingMy = true;
        new GetPlaylists().execute("http://moozee.pythonanywhere.com/api/user-playlists/");

        parsingFollowed = true;
        new GetPlaylists().execute("http://moozee.pythonanywhere.com/followed-playlist/");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 999){
            if(resultCode == RESULT_OK){
                parsingMy = true;
                new GetPlaylists().execute("http://moozee.pythonanywhere.com/api/user-playlists/");

                parsingFollowed = true;
                new GetPlaylists().execute("http://moozee.pythonanywhere.com/followed-playlist/");
            }
        }
    }


    private class GetPlaylists extends AsyncTask<String, String, String> {
        private ProgressDialog pd;
        protected void onPreExecute() {
            super.onPreExecute();
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
                connection.setRequestMethod("GET");
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
           // Toast.makeText(UserPlaylistsActivity.this, result, Toast.LENGTH_SHORT).show();
            JSONParserPlaylists jsonParserPlaylists = new JSONParserPlaylists(result);
            ArrayList<Playlist> playlists = jsonParserPlaylists.getPlaylistsArray();
            if(parsingMy){
                containerMyP.removeAllViews();
                for(int i = 0; i < playlists.size(); i++){
                    final TextView textView = new TextView(UserPlaylistsActivity.this);
                    textView.setText(playlists.get(i).getName());
                    textView.setTag(playlists.get(i).getPk());
                    textView.setTextSize(25);
                    textView.setPadding(50,20,0,0);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                currentName = textView.getText().toString();
                                //currentPk = musicPks.get(position);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("pk", Integer.parseInt(textView.getTag().toString()));
                                new GetJson().execute(jsonObject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    containerMyP.addView(textView);
                }
                parsingMy = false;
            }else if(parsingFollowed){
                containerFollowedP.removeAllViews();
                for(int i = 0; i < playlists.size(); i++){
                    final TextView textView = new TextView(UserPlaylistsActivity.this);
                    textView.setText(playlists.get(i).getName());
                    textView.setTag(playlists.get(i).getPk());
                    textView.setTextSize(25);
                    textView.setPadding(50,20,0,0);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                currentName = textView.getText().toString();
                                currentPk = Integer.parseInt(textView.getTag().toString());
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("pk", Integer.parseInt(textView.getTag().toString()));
                                callForFollowed = true;
                                new GetJson().execute(jsonObject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    containerFollowedP.addView(textView);
                }
                parsingFollowed = false;
            }



        }
    }

    private class GetJson extends AsyncTask<JSONObject, String, String> {
        private ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(JSONObject... jsonData) {
            try {

                URL url = new URL("http://moozee.pythonanywhere.com/api/playlist/"); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                SharedPreferences sp = getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
                String token = sp.getString(User.KEY_TOKEN, "");
                conn.setRequestProperty("Authorization", "Token " + token);

                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                String data = jsonData[0].toString();
                Log.e("Sending data", data);

                writer.write(data);
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    User.USER_ACCEPTED = true;

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(UserPlaylistsActivity.this, "Downloading musics finish", Toast.LENGTH_SHORT).show();
            Intent intent  = new Intent(UserPlaylistsActivity.this, ListedMusicsActivity.class);
            intent.putExtra("name", currentName);
            if(callForFollowed)
                intent.putExtra("pk", currentPk);
            intent.putExtra("json", result);
            Log.e("DOWNLOADED JSON ", result );
            startActivity(intent);
        }
    }
    
    
}
