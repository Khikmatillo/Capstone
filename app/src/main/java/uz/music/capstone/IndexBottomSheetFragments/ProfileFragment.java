package uz.music.capstone.IndexBottomSheetFragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import uz.music.capstone.IndexActivity;
import uz.music.capstone.ListedMusicsActivity;
import uz.music.capstone.Playlist;
import uz.music.capstone.PlaylistCreateActivity;
import uz.music.capstone.PlaylistMusic;
import uz.music.capstone.R;
import uz.music.capstone.UserPlaylistsActivity;
import uz.music.capstone.authentication.WelcomeActivity;
import uz.music.capstone.json.JSONParserPlaylists;
import uz.music.capstone.profile.EditProfile;
import uz.music.capstone.profile.User;


/**
 * Created by Nemo on 10/13/2017.
 */

public class ProfileFragment extends Fragment {

    LinearLayout ll_favourites, ll_playlists, ll_settings, ll_conatiner_info, ll_follow, seeFollowers, seeFollowings;
    TextView txt_name, txt_following, txt_followers;
    ImageView image;

    String txtJsonFollowers = "", txtJsonFollowings = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        ll_favourites = (LinearLayout) view.findViewById(R.id.profile_favourites);
        ll_playlists = (LinearLayout) view.findViewById(R.id.profile_playlists);
        ll_settings = (LinearLayout) view.findViewById(R.id.profile_settings);
        ll_conatiner_info = (LinearLayout) view.findViewById(R.id.profile_container_info);
        ll_follow = (LinearLayout) view.findViewById(R.id.followUser) ;
        ll_follow.setVisibility(View.GONE);
        seeFollowers = (LinearLayout) view.findViewById(R.id.containerSeeFollowers);
        seeFollowings = (LinearLayout) view.findViewById(R.id.containerSeeFollowings);

        txt_name = (TextView) view.findViewById(R.id.profile_name);
        txt_followers = (TextView) view.findViewById(R.id.profile_followers);
        txt_following = (TextView) view.findViewById(R.id.profile_following);
        image = (ImageView) view.findViewById(R.id.profile_img);


        SharedPreferences shp = getActivity().getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
        String username = shp.getString(User.KEY_USERNAME, "");
        txt_name.setText(username);

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);

            new SendDataFollowers().execute(jsonObject);
            new SendDataFollowings().execute(jsonObject);
        }catch (JSONException e){

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


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Followers");
                    builder.setItems(playlistNames, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Followings");
                    builder.setItems(playlistNames, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ll_favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetFavourites().execute(User.VARIABLE_URL + "/api/get-liked-musics/");
            }
        });
        ll_playlists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), UserPlaylistsActivity.class);
                startActivity(intent1);
            }
        });

        ll_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] deleteOption = new CharSequence[]{"Log Out"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("You want to log out?");
                builder.setItems(deleteOption, new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sp = getActivity().getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.remove(User.KEY_TOKEN);
                        editor.remove(User.KEY_USERNAME);
                        editor.commit();
                        IndexActivity.CURRENT_USER = null;
                        Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                builder.show();



//                Intent intent = new Intent(getActivity(), EditProfile.class);
//                getActivity().startActivity(intent);
            }
        });


        return view;
    }

    private class GetFavourites extends AsyncTask<String, String, String> {
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

                SharedPreferences sp = getActivity().getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
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
//            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            Intent intent  = new Intent(getActivity(), ListedMusicsActivity.class);
            intent.putExtra("name", "Favourites");
            intent.putExtra("json", result);
            getActivity().startActivity(intent);
        }

    }


    private class SendDataFollowers extends AsyncTask<JSONObject, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(JSONObject... jsonData) {

            try {

                URL url = new URL(User.VARIABLE_URL + "/auth/user-followers/"); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                SharedPreferences sp = getActivity().getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
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

                URL url = new URL(User.VARIABLE_URL + "/auth/user-followings/"); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                SharedPreferences sp = getActivity().getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
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
