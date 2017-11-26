package uz.music.capstone;

/**
 * Created by Maestro on 11/5/2017.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import uz.music.capstone.json.JSONParserPlaylistMusics;
import uz.music.capstone.json.PostRequestForJSON;
import uz.music.capstone.profile.User;


public class CustomAdapterForRecyclerItem extends RecyclerView.Adapter<CustomAdapterForRecyclerItem.MyViewHolder> {

    ArrayList<String> musicNames;
    ArrayList<String> musicDescriptions;
    ArrayList<String> musicImages;
    ArrayList<Integer> musicPks;
    String currentName = "";

    Boolean upOrdown;
    Context context;
    int type = 0;
    String jsonResult = null;

    public CustomAdapterForRecyclerItem(Context context, ArrayList<Playlist> playlists, ArrayList<Genre> genres, Boolean upOrdown, int type)
    {
        /*

        type 1: playlists
        type 2: genres
        type 3:

         */
        this.upOrdown = upOrdown;
        this.type = type;
        this.context = context;

        this.musicNames = new ArrayList<String>();
        this.musicDescriptions = new ArrayList<String>();
        this.musicImages = new ArrayList<String>();
        this.musicPks = new ArrayList<Integer>();

        if(type == 1){
            for(int i = 0; i < playlists.size(); i++){
                musicNames.add(playlists.get(i).getName());
                musicDescriptions.add(playlists.get(i).getDescription());
                musicImages.add(playlists.get(i).getPhotoLink());
                musicPks.add(playlists.get(i).getPk());
            }
        }else if(type == 2){
            for(int i = 0; i < genres.size(); i++) {
                musicNames.add(genres.get(i).getName());
                musicImages.add(genres.get(i).getPhotoLink());
            }
        }



    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(!upOrdown) {
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        } else
        {
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayoutupper, parent, false);
        }
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.name.setText(musicNames.get(position));
        if(type == 1){
            holder.artist.setText(musicDescriptions.get(position));
        }

        if(musicImages.get(position) != null){
            Picasso.with(context)
                    .load("http://moozee.pythonanywhere.com" + musicImages.get(position))
                    .into(holder.image);
        }else{
            holder.image.setImageResource(R.drawable.a19);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, musicNames.get(position), Toast.LENGTH_SHORT).show();

                if(type == 1){
                    try {
                        currentName = musicNames.get(position);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("pk", musicPks.get(position));
                        new GetJson().execute(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



            }
        });

    }


    @Override
    public int getItemCount() {
        return musicNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView name;
        TextView artist;
        ImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);


            name = (TextView) itemView.findViewById(R.id.name);
            artist = (TextView) itemView.findViewById(R.id.artist);
            image = (ImageView) itemView.findViewById(R.id.image);


        }
    }

//    private void parsePlaylistMusicsJson(String result){
//        JSONParserPlaylistMusics jsonParserPlaylistMusics = new JSONParserPlaylistMusics(result);
//        ArrayList<PlaylistMusic> playlistMusics = jsonParserPlaylistMusics.getMusicsArray();
//    }

    private class GetJson extends AsyncTask<JSONObject, String, String> {
        private ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context, "Downloading Playlist data", Toast.LENGTH_SHORT).show();
        }

        protected String doInBackground(JSONObject... jsonData) {
            try {

                URL url = new URL("http://moozee.pythonanywhere.com/api/playlist/"); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                SharedPreferences sp = context.getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
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
            Toast.makeText(context, "Downloading musics finish", Toast.LENGTH_SHORT).show();
            jsonResult = result;
            Intent intent  = new Intent(context, ListedMusicsActivity.class);
            intent.putExtra("name", currentName);
            intent.putExtra("json", jsonResult);
            Log.e("DOWNLOADED JSON ", jsonResult );
            context.startActivity(intent);
        }
    }
}
