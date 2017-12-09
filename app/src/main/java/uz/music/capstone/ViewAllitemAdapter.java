package uz.music.capstone;

/**
 * Created by Maestro on 11/30/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.List;

import uz.music.capstone.profile.User;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class ViewAllitemAdapter extends RecyclerView.Adapter<ViewAllitemAdapter.MyViewHolder> {

    private Context mContext;
    private List<ViewAllSingleItem> albumList;

    private int type, currentPk;
    private String currentName;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, detail;
        public ImageView thumbnail;
        //public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            detail = (TextView) view.findViewById(R.id.detail);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
           // overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public ViewAllitemAdapter(Context mContext, List<ViewAllSingleItem> albumList, int type) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_view_all_single_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ViewAllSingleItem album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.detail.setText(album.getDetail());

        // loading album cover using Glide library
//        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        if(!album.getThumbnail().equals("null")){
            Picasso.with(mContext)
                    .load(User.VARIABLE_URL + album.getThumbnail())
                    .into(holder.thumbnail);
        }else{
            Glide.with(mContext).load(R.drawable.a19).into(holder.thumbnail);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(type == 1){

                        currentName = album.getName();
                        currentPk = album.getPk();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("pk", currentPk);
                        jsonObject.put("link", User.VARIABLE_URL + "/api/playlist/");
                        new GetJson().execute(jsonObject);
                    }else if(type == 2){
                        currentName = album.getName();
//                        currentPk = musicPks.get(position);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("genre_name", currentName);
                        jsonObject.put("link", User.VARIABLE_URL + "/api/genre-detail/");
                        new GetJson().execute(jsonObject);
                    }else if(type == 3){
                        currentName = album.getName();
                        currentPk = album.getPk();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("pk", currentPk);
                        jsonObject.put("link", User.VARIABLE_URL + "/api/album-detail/");
                        new GetJson().execute(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow);
//            }
//        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
//    private void showPopupMenu(View view) {
//        // inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
//    }

    /**
     * Click listener for popup menu items
     */
//    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
//
//        public MyMenuItemClickListener() {
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.action_add_favourite:
//                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.action_play_next:
//                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
//                    return true;
//                default:
//            }
//            return false;
//        }
//    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


    private class GetJson extends AsyncTask<JSONObject, String, String> {
        private ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();
//            Toast.makeText(context, "Downloading Playlist data", Toast.LENGTH_SHORT).show();
        }

        protected String doInBackground(JSONObject... jsonData) {
            try {
                String urlString = jsonData[0].getString("link");
                jsonData[0].remove("link");
                URL url = new URL(urlString); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                SharedPreferences sp = mContext.getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
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
//            Toast.makeText(context, "Downloading musics finish", Toast.LENGTH_SHORT).show();
//            jsonResult = result;
            Intent intent  = new Intent(mContext, ListedMusicsActivity.class);
            intent.putExtra("name", currentName);
            intent.putExtra("pk", currentPk);
            intent.putExtra("json", result);
            Log.e("DOWNLOADED JSON ", result );
            mContext.startActivity(intent);
        }
    }

}