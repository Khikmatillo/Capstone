package uz.music.capstone.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uz.music.capstone.Music;
import uz.music.capstone.Playlist;

/**
 * Created by Nemo on 11/25/2017.
 */



public class JSONParserPlaylists {

    private String JSONText = null;
    private ArrayList<Playlist> parsed_playlists;

    private String name;
    private String description;
    private String photoLink;
    private int pk;

    public JSONParserPlaylists(String jsonText){
        this.JSONText = jsonText;
        this.parsed_playlists = new ArrayList<Playlist>();
    }

    public ArrayList<Playlist> getPlaylistsArray(){
        if(JSONText != null){
            try{

                JSONArray json_arr = new JSONArray(JSONText);

                for (int i = 0; i < json_arr.length(); i++){
                    JSONObject p = json_arr.getJSONObject(i);
                    name = p.getString("name");
                    description = p.getString("description");
                    photoLink = p.getString("photo");
                    pk = p.getInt("pk");
                    Playlist playlist = new Playlist(name, description, photoLink, pk);
                    parsed_playlists.add(playlist);

                    Log.e("", "JSON PARSING SUCCESS ");
                }
            }catch (final JSONException e){
                Log.e("", "JSON PARSING ERROR " + e.getMessage());
            }
        }else{
            Log.e("", "JSON data is null.");
        }
        return parsed_playlists;
    }
}
