package uz.music.capstone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import uz.music.capstone.IndexBottomSheetFragments.ProfileFragment;
import uz.music.capstone.profile.User;

/**
 * Created by Nemo on 12/2/2017.
 */

public class ProfileActivity extends AppCompatActivity {
    Intent intent;
    TextView txtName, txtFollow, txt_following, txt_followers;;
    LinearLayout followUser, containerExtra, seeFollowers, seeFollowings;;
    ImageView imageFollow, imgProfile;
    String txtJsonFollowers = "", txtJsonFollowings = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        intent = getIntent();
        txtName = (TextView)findViewById(R.id.profile_name);
        txtFollow = (TextView)findViewById(R.id.txtFollowUser);
        followUser = (LinearLayout)findViewById(R.id.followUser);
        containerExtra = (LinearLayout) findViewById(R.id.containerExtra);
        imageFollow = (ImageView) findViewById(R.id.imgFollowUser);
        imgProfile = (ImageView)findViewById(R.id.profile_img);
        txt_followers = (TextView) findViewById(R.id.profile_followers);
        txt_following = (TextView) findViewById(R.id.profile_following);
        seeFollowers = (LinearLayout) findViewById(R.id.containerSeeFollowers);
        seeFollowings = (LinearLayout) findViewById(R.id.containerSeeFollowings);



        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("json"));
            containerExtra.setVisibility(View.GONE);
            txtName.setText(jsonObject.getString("username"));
            Picasso.with(getApplicationContext())
                    .load("http://moozee.pythonanywhere.com" + jsonObject.getString("photo"))
                    .into(imgProfile);


            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("username", txtName.getText().toString());
            new SendDataFollowers().execute(jsonObject1);
            new SendDataFollowings().execute(jsonObject1);




            followUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        JSONObject jsonObject1 = new JSONObject();
                        if(txtFollow.getText().toString().equals("FOLLOW")){
                            jsonObject1.put("username", txtName.getText().toString());
                            jsonObject1.put("action", "follow");
                            new FollowUser().execute(jsonObject1);
                        }else{
                            jsonObject1.put("username", txtName.getText().toString());
                            jsonObject1.put("action", "unfollow");
                            new FollowUser().execute(jsonObject1);
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


        seeFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONArray jsonArray = new JSONArray(txtJsonFollowers);
                    CharSequence playlistNames[] = new CharSequence[jsonArray.length()];
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        playlistNames[i] = jsonObject.getString("username");
                    }


                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Followers");
                    builder.setItems(playlistNames, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            try{
//                                PlaylistMusic music = (PlaylistMusic) list_view.getItemAtPosition(music_position);
//                                JSONObject jsonObject = new JSONObject();
//                                jsonObject.put("link", "http://moozee.pythonanywhere.com/api/add-to-playlist/");
//                                jsonObject.put("music_id", music.getPk());
//                                jsonObject.put("playlist_id", playlists.get(which).getPk());
//                                new ListedMusicsActivity.SendData().execute(jsonObject);
//                            }catch (JSONException e){
//
//                            }
                        }
                    });
                    builder.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        seeFollowings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONArray jsonArray = new JSONArray(txtJsonFollowings);
                    CharSequence playlistNames[] = new CharSequence[jsonArray.length()];
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        playlistNames[i] = jsonObject.getString("username");
                    }


                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Followings");
                    builder.setItems(playlistNames, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            try{
//                                PlaylistMusic music = (PlaylistMusic) list_view.getItemAtPosition(music_position);
//                                JSONObject jsonObject = new JSONObject();
//                                jsonObject.put("link", "http://moozee.pythonanywhere.com/api/add-to-playlist/");
//                                jsonObject.put("music_id", music.getPk());
//                                jsonObject.put("playlist_id", playlists.get(which).getPk());
//                                new ListedMusicsActivity.SendData().execute(jsonObject);
//                            }catch (JSONException e){
//
//                            }
                        }
                    });
                    builder.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    private class FollowUser extends AsyncTask<JSONObject, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(JSONObject... jsonData) {

            try {

                URL url = new URL("http://moozee.pythonanywhere.com/auth/api/users/follow/"); // here is your URL path
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

            if(txtFollow.getText().toString().equals("FOLLOW")){
                txtFollow.setText("UNFOLLOW");
                imageFollow.setVisibility(View.GONE);
            }else{
                txtFollow.setText("FOLLOW");
                imageFollow.setVisibility(View.VISIBLE);
            }


            try {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("username", txtName.getText().toString());
                new SendDataFollowers().execute(jsonObject2);
                new SendDataFollowings().execute(jsonObject2);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            Toast.makeText(ProfileActivity.this, result, Toast.LENGTH_LONG).show();
            Log.e("SEARCH RESULT:::", result);

        }
    }


    private class SendDataFollowers extends AsyncTask<JSONObject, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(JSONObject... jsonData) {

            try {

                URL url = new URL("http://moozee.pythonanywhere.com/auth/user-followers/"); // here is your URL path
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
                jsonData[0].remove("link");
                String data = jsonData[0].toString();
                Log.e("Sending data", data);

                writer.write(data);
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

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


            // Toast.makeText(ListedMusicsActivity.this, result, Toast.LENGTH_LONG).show();

            try {
                JSONArray jsonArray = new JSONArray(result);
                txtJsonFollowers = result;
                txt_followers.setText(jsonArray.length() + "");
                    Log.e("FOLLOWERS RESULT:::", result);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    private class SendDataFollowings extends AsyncTask<JSONObject, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(JSONObject... jsonData) {

            try {

                URL url = new URL("http://moozee.pythonanywhere.com/auth/user-followings/"); // here is your URL path
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
                jsonData[0].remove("link");
                String data = jsonData[0].toString();
                Log.e("Sending data", data);

                writer.write(data);
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

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


            // Toast.makeText(ListedMusicsActivity.this, result, Toast.LENGTH_LONG).show();

            try {
                JSONArray jsonArray = new JSONArray(result);

                txtJsonFollowings = result;
                txt_following.setText(jsonArray.length() + "");
                Log.e("FOLLOWINGS SRESULT:::", result);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
