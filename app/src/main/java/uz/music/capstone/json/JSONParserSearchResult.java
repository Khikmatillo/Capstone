package uz.music.capstone.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uz.music.capstone.Playlist;
import uz.music.capstone.PlaylistMusic;

/**
 * Created by Nemo on 12/2/2017.
 */

public class JSONParserSearchResult {
    private String jsonText;
    private ArrayList<String> musicResults;
    private ArrayList<String> albumResults;
    private ArrayList<String> userResults;

    public JSONParserSearchResult(String json){
        this.jsonText = json;
        musicResults = new ArrayList<String>();
        albumResults = new ArrayList<String>();
        userResults = new ArrayList<String>();
    }

    public ArrayList<PlaylistMusic> getMusicResultsArray(){
        if(jsonText != null){
            try{

                JSONObject jsonObject = new JSONObject(jsonText);

                JSONArray json_arr = jsonObject.getJSONArray("music_results");

                JSONParserPlaylistMusics jsonParserPlaylists = new JSONParserPlaylistMusics(json_arr.toString());
                return jsonParserPlaylists.getMusicsArray();

            }catch (final JSONException e){
                Log.e("", "JSON PARSING ERROR " + e.getMessage());
            }
        }else{
            Log.e("", "JSON data is null.");
        }

        return new ArrayList<PlaylistMusic>();
    }

    public ArrayList<String> getUserResultsArray(){
        if(jsonText != null){
            try{

                JSONObject jsonObject = new JSONObject(jsonText);

                JSONArray json_arr = jsonObject.getJSONArray("user_results");

                for (int i = 0; i < json_arr.length(); i++){
                    JSONObject p = json_arr.getJSONObject(i);
                    String name = p.getString("username");
                    userResults.add(name);
                    Log.e("", "JSON PARSING SUCCESS ");
                }
            }catch (final JSONException e){
                Log.e("", "JSON PARSING ERROR " + e.getMessage());
            }
        }else{
            Log.e("", "JSON data is null.");
        }

        return userResults;
    }

    public ArrayList<String> getAlbumsResultsArray(){
        if(jsonText != null){
            try{

                JSONObject jsonObject = new JSONObject(jsonText);

                JSONArray json_arr = jsonObject.getJSONArray("album_results");

                for (int i = 0; i < json_arr.length(); i++){
                    JSONObject p = json_arr.getJSONObject(i);
                    String name = p.getString("name");
                    albumResults.add(name);
                    Log.e("", "JSON PARSING SUCCESS ");
                }
            }catch (final JSONException e){
                Log.e("", "JSON PARSING ERROR " + e.getMessage());
            }
        }else{
            Log.e("", "JSON data is null.");
        }

        return albumResults;
    }

    public ArrayList<Playlist> getPlaylistResultsArray(){
        if(jsonText != null){
            try{

                JSONObject jsonObject = new JSONObject(jsonText);

                JSONArray json_arr = jsonObject.getJSONArray("playlist_results");

                JSONParserPlaylists jsonParserPlaylists = new JSONParserPlaylists(json_arr.toString());
                return jsonParserPlaylists.getPlaylistsArray();

//                for (int i = 0; i < json_arr.length(); i++){
//                    JSONObject p = json_arr.getJSONObject(i);
//                    String name = p.getString("name");
//                    playlistResults.add(name);
//                    Log.e("", "JSON PARSING SUCCESS ");
//                }
            }catch (final JSONException e){
                Log.e("", "JSON PARSING ERROR " + e.getMessage());
            }
        }else{
            Log.e("", "JSON data is null.");
        }

        return new ArrayList<Playlist>();
    }

    public String getPlaylistJson(){
        if(jsonText != null){
            try{

                JSONObject jsonObject = new JSONObject(jsonText);
                JSONArray json_arr = jsonObject.getJSONArray("playlist_results");
                return json_arr.toString();
            }catch (final JSONException e){
                Log.e("", "JSON PARSING ERROR " + e.getMessage());
            }
        }else{
            Log.e("", "JSON data is null.");
        }
        return "";
    }

    public String getMusicJson(){
        if(jsonText != null){
            try{

                JSONObject jsonObject = new JSONObject(jsonText);
                JSONArray json_arr = jsonObject.getJSONArray("music_results");
                return json_arr.toString();
            }catch (final JSONException e){
                Log.e("", "JSON PARSING ERROR " + e.getMessage());
            }
        }else{
            Log.e("", "JSON data is null.");
        }
        return "";
    }


}
