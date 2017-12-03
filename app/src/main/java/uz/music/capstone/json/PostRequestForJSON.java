package uz.music.capstone.json;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import uz.music.capstone.profile.User;

/**
 * Created by Nemo on 11/26/2017.
 */

public class PostRequestForJSON {
    public PostRequestForJSON(){}

    public String PostRequest(Context context, String param, boolean post){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(param);
            connection = (HttpURLConnection) url.openConnection();

            SharedPreferences sp = context.getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
            String token = sp.getString(User.KEY_TOKEN, "");
            connection.setRequestProperty("Authorization", "Token " + token);
            if(post){
                connection.setRequestMethod("POST");
            }else{
                connection.setRequestMethod("GET");
            }
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
}
