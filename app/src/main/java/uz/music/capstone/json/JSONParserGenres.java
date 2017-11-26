package uz.music.capstone.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uz.music.capstone.Genre;
import uz.music.capstone.Playlist;

/**
 * Created by Nemo on 11/25/2017.
 */

public class JSONParserGenres {

    private String JSONText = null;
    private ArrayList<Genre> parsed_playlists;

    private String name;
    private String photoLink;

    public JSONParserGenres(String jsonText){
        this.JSONText = jsonText;
        this.parsed_playlists = new ArrayList<Genre>();
    }

    public ArrayList<Genre> getGenresArray(){
        if(JSONText != null){
            try{

                JSONArray json_arr = new JSONArray(JSONText);

                for (int i = 0; i < json_arr.length(); i++){
                    JSONObject p = json_arr.getJSONObject(i);
                    name = p.getString("genre_name");
                    photoLink = p.getString("photo");

                    Genre playlist = new Genre(name, photoLink);
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
