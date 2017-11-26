package uz.music.capstone.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uz.music.capstone.Music;
import uz.music.capstone.Playlist;
import uz.music.capstone.PlaylistMusic;

/**
 * Created by Nemo on 11/26/2017.
 */

public class JSONParserPlaylistMusics {
    private String name;
    private ArrayList<String> get_links;
    private String photo;
    private String file;
    private int pk;

    private String JSONText;
    private ArrayList<PlaylistMusic> parsedPlaylistMusics;

    public JSONParserPlaylistMusics(String text){
        this.JSONText = text;
        parsedPlaylistMusics = new ArrayList<PlaylistMusic>();
    }

    public ArrayList<PlaylistMusic> getMusicsArray(){
        if(JSONText != null){
            try{
                JSONArray json_arr = new JSONArray(JSONText);

                for (int i = 0; i < json_arr.length(); i++){
                    get_links = new ArrayList<String>();
                    JSONObject m = json_arr.getJSONObject(i);
                    name = m.getString("name");
                    JSONArray jsonlinks = m.getJSONArray("get_links");
                    for(int j = 0; j < jsonlinks.length(); j++){
                        get_links.add(jsonlinks.getString(j));
                        Log.i(name, jsonlinks.getString(j));
                    }
                    photo = m.getString("photo");
                    file = m.getString("file");
                    pk = m.getInt("pk");

                    PlaylistMusic playlistMusic = new PlaylistMusic(name, get_links, photo, file, pk);
                    parsedPlaylistMusics.add(playlistMusic);
                    Log.e("", "JSON PARSING SUCCESS ");
                }
            }catch (final JSONException e){
                Log.e("", "JSON PARSING ERROR " + e.getMessage());
            }
        }else{
            Log.e("", "Couldn't get json from server.");
        }
        return parsedPlaylistMusics;
    }

}
