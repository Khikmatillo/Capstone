package uz.music.capstone.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import uz.music.capstone.IndexActivity;
import uz.music.capstone.ListedMusicsActivity;
import uz.music.capstone.profile.User;

/**
 * Created by Nemo on 10/8/2017.
 */

public class SendJSON {

    Context context;

    public SendJSON(Context context, JSONObject data) {
        this.context = context;
        new SendData().execute(data);
    }

    private class SendData extends AsyncTask<JSONObject, Void, String> {

        private String url_string = null;
        private int accepted_response;

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(JSONObject... jsonData) {

            try {
                int type = jsonData[0].getInt(User.KEY_TYPE);
                if(type == User.TYPE_CREATE){
                    accepted_response = HttpsURLConnection.HTTP_CREATED;
                    url_string = "https://moozee.pythonanywhere.com/auth/api/register";
                }else if(type == User.TYPE_LOGIN){
                    accepted_response = HttpsURLConnection.HTTP_OK;
                    url_string = "https://moozee.pythonanywhere.com/auth/api/login";
                }else if(type == User.TYPE_CHANGE){
                    accepted_response = HttpsURLConnection.HTTP_OK;
                    url_string = "https://moozee.pythonanywhere.com/auth/api/change";
                }else if(type == User.TYPE_FORGET){
                    accepted_response = HttpsURLConnection.HTTP_OK;
                    url_string = "https://moozee.pythonanywhere.com/auth/api/forget";
                }else if(type == User.TYPE_RESET){
                    accepted_response = HttpsURLConnection.HTTP_OK;
                    url_string = "https://moozee.pythonanywhere.com/auth/api/reset";
                }else{
                    url_string = null;
                    accepted_response = -1;
                }
                jsonData[0].remove(User.KEY_TYPE);

                URL url = new URL(url_string); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                Log.e("Sending Link", url_string);

                writer.write(data);
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == accepted_response) {

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
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();


            try {
                JSONObject jsonObject = new JSONObject(result);
                String token = jsonObject.getString(User.KEY_TOKEN);
                SharedPreferences shp = context.getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shp.edit();
                editor.putString(User.KEY_TOKEN, token);
                editor.commit();
                if (User.USER_ACCEPTED) {
                    Intent intent = new Intent(context, IndexActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    User.USER_ACCEPTED = false;
                    context.startActivity(intent);
                }
            } catch (JSONException e) {
                Log.e("Something", e.getMessage());
                //e.printStackTrace();
            }

        }
    }

    public String getPostDataString(JSONObject jsonData) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = jsonData.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = jsonData.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
