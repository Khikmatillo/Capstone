package uz.music.capstone.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uz.music.capstone.Genre;
import uz.music.capstone.profile.Album;

/**
 * Created by Nemo on 12/2/2017.
 */

public class JSONParserAlbums {

    private String JSONText = null;
    private ArrayList<Album> parsed_albums;

    private String name;
    private String photoLink;
    private String artist;
    private int pk;

    public JSONParserAlbums(String jsonText){
        this.JSONText = jsonText;
        this.parsed_albums = new ArrayList<Album>();
    }

    public ArrayList<Album> getAlbumsArray(){
        if(JSONText != null){
            try{

                JSONArray json_arr = new JSONArray(JSONText);

                for (int i = 0; i < json_arr.length(); i++){
                    JSONObject p = json_arr.getJSONObject(i);
                    name = p.getString("name");
                    artist = p.getString("artist");
                    photoLink = p.getString("photo");
                    pk = p.getInt("pk");
                    Album album = new Album(name, artist, photoLink, pk);
                    parsed_albums.add(album);

                    Log.e("", "JSON PARSING SUCCESS ");
                }
            }catch (final JSONException e){
                Log.e("", "JSON PARSING ERROR " + e.getMessage());
            }
        }else{
            Log.e("", "JSON data is null.");
        }
        return parsed_albums;
    }

}
