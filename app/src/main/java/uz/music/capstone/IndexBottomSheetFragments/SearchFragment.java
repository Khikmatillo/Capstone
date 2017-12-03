package uz.music.capstone.IndexBottomSheetFragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import uz.music.capstone.IndexActivity;
import uz.music.capstone.ProfileActivity;
import uz.music.capstone.R;
import uz.music.capstone.json.JSONParserSearchResult;
import uz.music.capstone.profile.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    LinearLayout containerLL;
    EditText editText;
    ImageButton btnSearch;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_search, container, false);

       containerLL = (LinearLayout)view.findViewById(R.id.containerSearch);
       editText = (EditText)view.findViewById(R.id.searchEditTextXML);
       btnSearch = (ImageButton)view.findViewById(R.id.searchButtonXML);




       btnSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(editText.getText().toString() != ""){
                   try {
                       JSONObject jsonObject = new JSONObject();
                       jsonObject.put("q", editText.getText().toString());
                       new SendData().execute(jsonObject);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
           }
       });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    private void inflateForSearchResult(View view, ArrayList<String> list){
        TextView txt = (TextView) view.findViewById(R.id.searchResultCategoryXML);
        TextView txt1 = (TextView) view.findViewById(R.id.txtSearchResultName1);
        TextView txt2 = (TextView) view.findViewById(R.id.txtSearchResultName2);
        View seperator1 = (View) view.findViewById(R.id.firstSeperator);
        View seperator2 = (View) view.findViewById(R.id.secondSeperator);
        Button viewAll = (Button)view.findViewById(R.id.viewAllDiscover);

        txt.setText("User Results");
        if(list.size() > 1){
            txt1.setText(list.get(0));
            txt2.setText(list.get(1));
        }else if(list.size() == 1){
            txt1.setText(list.get(0));
            txt2.setVisibility(View.GONE);
            seperator2.setVisibility(View.GONE);
        }else{
            txt1.setText("No Data");
            txt2.setVisibility(View.GONE);
            seperator2.setVisibility(View.GONE);
            viewAll.setVisibility(View.GONE);
        }
    }

    private class SendData extends AsyncTask<JSONObject, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(JSONObject... jsonData) {

            try {
                URL url = new URL("http://moozee.pythonanywhere.com/api/search/"); // here is your URL path
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

            JSONParserSearchResult jsonParserSearchResult = new JSONParserSearchResult(result);
            ArrayList<String> users = jsonParserSearchResult.getUserResultsArray();
            ArrayList<String> musics = jsonParserSearchResult.getMusicResultsArray();
            ArrayList<String> albums = jsonParserSearchResult.getAlbumsResultsArray();
            ArrayList<String> playlists = jsonParserSearchResult.getPlaylistResultsArray();

            containerLL.removeAllViews();

            //inflate for users
            LayoutInflater inflater =
                    (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View userResult = inflater.inflate(R.layout.searchresultitem, null);
            View musicResult = inflater.inflate(R.layout.searchresultitem, null);
            View playlistResult = inflater.inflate(R.layout.searchresultitem, null);
            View albumResult = inflater.inflate(R.layout.searchresultitem, null);


            TextView txt = (TextView) userResult.findViewById(R.id.searchResultCategoryXML);
            final TextView txt1 = (TextView) userResult.findViewById(R.id.txtSearchResultName1);
            TextView txt2 = (TextView) userResult.findViewById(R.id.txtSearchResultName2);
            View seperator1 = (View) userResult.findViewById(R.id.firstSeperator);
            View seperator2 = (View) userResult.findViewById(R.id.secondSeperator);
            Button viewAll = (Button)userResult.findViewById(R.id.viewAllDiscover);

            txt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("username", txt1.getText().toString());
                        new GetDataUserInfo().execute(jsonObject1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            txt.setText("User Results");
            if(users.size() > 1){
                txt1.setText(users.get(0));
                txt2.setText(users.get(1));
            }else if(users.size() == 1){
                txt1.setText(users.get(0));
                txt2.setVisibility(View.GONE);
                seperator2.setVisibility(View.GONE);
            }else{
                txt1.setText("No Data");
                txt2.setVisibility(View.GONE);
                seperator2.setVisibility(View.GONE);
            }

            if(users.size() < 3){
                viewAll.setVisibility(View.GONE);
            }


            //inflate for musics

//            LayoutInflater inflaterMusic =
//                    (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView txtM = (TextView) musicResult.findViewById(R.id.searchResultCategoryXML);
            TextView txtM1 = (TextView) musicResult.findViewById(R.id.txtSearchResultName1);
            TextView txtM2 = (TextView) musicResult.findViewById(R.id.txtSearchResultName2);
            View seperatorM1 = (View) musicResult.findViewById(R.id.firstSeperator);
            View seperatorM2 = (View) musicResult.findViewById(R.id.secondSeperator);
            Button viewAllM = (Button)musicResult.findViewById(R.id.viewAllDiscover);

            txtM.setText("Music Results");
            if(musics.size() > 1){
                txtM1.setText(musics.get(0));
                txtM2.setText(musics.get(1));
            }else if(musics.size() == 1){
                txtM1.setText(musics.get(0));
                txtM2.setVisibility(View.GONE);
                seperatorM2.setVisibility(View.GONE);
            }else{
                txtM1.setText("No Data");
                txtM2.setVisibility(View.GONE);
                seperatorM2.setVisibility(View.GONE);
            }
            if(musics.size() < 3){
                viewAllM.setVisibility(View.GONE);
            }



            //inflate for Playlists

//            LayoutInflater inflaterPlaylist =
//                    (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            TextView txtP = (TextView) playlistResult.findViewById(R.id.searchResultCategoryXML);
            TextView txtP1 = (TextView) playlistResult.findViewById(R.id.txtSearchResultName1);
            TextView txtP2 = (TextView) playlistResult.findViewById(R.id.txtSearchResultName2);
            View seperatorP1 = (View) playlistResult.findViewById(R.id.firstSeperator);
            View seperatorP2 = (View) playlistResult.findViewById(R.id.secondSeperator);
            Button viewAllP = (Button)playlistResult.findViewById(R.id.viewAllDiscover);

            txtP.setText("Playlist Results");
            if(playlists.size() > 1){
                txtP1.setText(playlists.get(0));
                txtP2.setText(playlists.get(1));
            }else if(playlists.size() == 1){
                txtP1.setText(playlists.get(0));
                txtP2.setVisibility(View.GONE);
                seperatorP2.setVisibility(View.GONE);
            }else{
                txtP1.setText("No Data");
                txtP2.setVisibility(View.GONE);
                seperatorP2.setVisibility(View.GONE);
            }
            if(playlists.size() < 3){
                viewAllP.setVisibility(View.GONE);
            }



            //inflate for Albums
//            LayoutInflater inflaterAlbums =
//                    (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView txtA = (TextView) albumResult.findViewById(R.id.searchResultCategoryXML);
            TextView txtA1 = (TextView) albumResult.findViewById(R.id.txtSearchResultName1);
            TextView txtA2 = (TextView) albumResult.findViewById(R.id.txtSearchResultName2);
            View seperatorA1 = (View) albumResult.findViewById(R.id.firstSeperator);
            View seperatorA2 = (View) albumResult.findViewById(R.id.secondSeperator);
            Button viewAllA = (Button)albumResult.findViewById(R.id.viewAllDiscover);

            txtA.setText("Album Results");
            if(albums.size() > 1){
                txtA1.setText(albums.get(0));
                txtA2.setText(albums.get(1));
            }else if(albums.size() == 1){
                txtA1.setText(albums.get(0));
                txtA2.setVisibility(View.GONE);
                seperatorA2.setVisibility(View.GONE);
            }else{
                txtA1.setText("No Data");
                txtA2.setVisibility(View.GONE);
                seperatorA2.setVisibility(View.GONE);
            }
            if(albums.size() < 3){
                viewAllA.setVisibility(View.GONE);
            }



            containerLL.addView(userResult);
            containerLL.addView(musicResult);
            containerLL.addView(playlistResult);
            containerLL.addView(albumResult);
//            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
//            Log.e("SEARCH RESULT:::", result);

        }
    }



    private class GetDataUserInfo extends AsyncTask<JSONObject, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(JSONObject... jsonData) {

            try {

                URL url = new URL("http://moozee.pythonanywhere.com/auth/profile-detail/"); // here is your URL path
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

            if(!result.contains("false")){
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("json", result);
                startActivity(intent);
            }

            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            Log.e("SEARCH RESULT:::", result);

        }
    }


}
