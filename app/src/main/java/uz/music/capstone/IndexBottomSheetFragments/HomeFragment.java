package uz.music.capstone.IndexBottomSheetFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;


import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import uz.music.capstone.CustomAdapterForRecyclerItem;
import uz.music.capstone.Genre;
import uz.music.capstone.ListedMusicsActivity;
import uz.music.capstone.Playlist;
import uz.music.capstone.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import uz.music.capstone.ViewAllActivity;
import uz.music.capstone.json.JSONParserAlbums;
import uz.music.capstone.json.JSONParserGenres;
import uz.music.capstone.json.JSONParserPlaylists;
import uz.music.capstone.json.PostRequestForJSON;
import uz.music.capstone.profile.Album;
import uz.music.capstone.profile.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment {

    @BindView(R.id.recyclerViewTopPlaylist) RecyclerView topPlaylistrecyclerView;
    @BindView(R.id.recyclerViewTopGenres) RecyclerView topGenresrecyclerView;
    @BindView(R.id.recyclerViewDiscover) RecyclerView topAlbumsrecyclerView;

    @BindView(R.id.viewAllPlaylist) Button viewAllPlaylists;
    @BindView(R.id.viewAllGenres) Button viewAllGenres;
    @BindView(R.id.viewAllAlbums) Button viewAllAlbums;

    String txtJsonPlaylists = "", txtJsonGenres = "", txtJsonAlbums = "";

    private boolean parsing_playlsts = false, parsing_genres = false, parsing_albums = false;

    ArrayList musicNames = new ArrayList<>(Arrays.asList("This Is The Name", "This Is The Name", "This Is The Name", "Music 4", "Music 5", "Music 6", "Music 7"));

    ArrayList artistNames = new ArrayList<>(Arrays.asList("Dua Lipa", "Dua Lipa", "Dua Lipa", "Artist 4", "Artist 5", "Artist 6", "Artist 7"));

    ArrayList musicImages = new ArrayList<>(Arrays.asList(R.drawable.a6, R.drawable.a9, R.drawable.a12,
            R.drawable.a19, R.drawable.a2, R.drawable.a4, R.drawable.a12 ));


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        SharedPreferences sp = getActivity().getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
//        String json_playlists = sp.getString(User.KEY_JSON_PLAYLISTS, "");
//        String json_genres = sp.getString(User.KEY_JSON_GENRES, "");
//
//        if(!json_playlists.equals("") && !json_genres.equals("")){
//            parsePlaylists(json_playlists);
//            parseGenres(json_genres);
//        }else{
        parsing_playlsts = true;
        new GetJson().execute(User.VARIABLE_URL + "/top-playlists/", "true");
        parsing_genres = true;
        new GetJson().execute(User.VARIABLE_URL + "/top-genres/", "true");
//        }
        parsing_albums = true;
        new GetJson().execute(User.VARIABLE_URL + "/api/top-albums/", "false");

        viewAllPlaylists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), ViewAllActivity.class);
                intent1.putExtra("type", 1);
                intent1.putExtra("json", txtJsonPlaylists);
                startActivity(intent1);
            }
        });

        viewAllGenres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), ViewAllActivity.class);
                intent1.putExtra("type", 2);
                intent1.putExtra("json", txtJsonGenres);
                startActivity(intent1);
            }
        });

        viewAllAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getActivity(), ViewAllActivity.class);
                intent1.putExtra("type", 3);
                intent1.putExtra("json", txtJsonAlbums);
                startActivity(intent1);
            }
        });
    }


    private void parseAlbums(String result){
        JSONParserAlbums jsonParserAlbums = new JSONParserAlbums(result);
        ArrayList<Album> albums = jsonParserAlbums.getAlbumsArray();
        CustomAdapterForRecyclerItem customAdapterRecycler = new CustomAdapterForRecyclerItem(getContext(), null, null, albums, false, 3);
        topAlbumsrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topAlbumsrecyclerView.setAdapter(customAdapterRecycler);
    }

    private void parsePlaylists(String result){
        JSONParserPlaylists parser = new JSONParserPlaylists(result);
        ArrayList<Playlist> playlists = parser.getPlaylistsArray();
        CustomAdapterForRecyclerItem customAdapterRecycler = new CustomAdapterForRecyclerItem(getContext(), playlists, null, null, false, 1);
        topPlaylistrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topPlaylistrecyclerView.setAdapter(customAdapterRecycler);
    }

    private void parseGenres(String result){
        JSONParserGenres parser = new JSONParserGenres(result);
        ArrayList<Genre> genres = parser.getGenresArray();
        CustomAdapterForRecyclerItem customAdapterRecycler = new CustomAdapterForRecyclerItem(getContext(), null, genres, null, false, 2);
        topGenresrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topGenresrecyclerView.setAdapter(customAdapterRecycler);
    }

    private class GetJson extends AsyncTask<String, String, String> {
        private ProgressDialog pd;
        protected void onPreExecute() {
            super.onPreExecute();
//            Toast.makeText(getActivity(), "Downloading JSON data", Toast.LENGTH_SHORT).show();
        }

        protected String doInBackground(String... params) {
            PostRequestForJSON requestForJSON = new PostRequestForJSON();
            if(params[1].equals("false")){
                return  requestForJSON.PostRequest(getActivity(), params[0], false);
            }else{
                return  requestForJSON.PostRequest(getActivity(), params[0], true);
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Toast.makeText(getActivity(), "Download process finish", Toast.LENGTH_SHORT).show();

            //Parsing the JSON starts --------------------------------------
            //parseJson(result);

//            SharedPreferences shp = getActivity().getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = shp.edit();
            if(parsing_playlsts){
                txtJsonPlaylists = result;
                parsePlaylists(result);
//                editor.putString(User.KEY_JSON_PLAYLISTS, result);
//                editor.commit();
                parsing_playlsts = false;
            }else if(parsing_genres){
                txtJsonGenres = result;
                parseGenres(result);
//                editor.putString(User.KEY_JSON_GENRES, result);
//                editor.commit();
                parsing_genres = false;
            }else if(parsing_albums){
                txtJsonAlbums = result;
                parseAlbums(result);
                parsing_albums = false;
            }


            //Parsing the JSON ends --------------------------------------
        }
    }


}
