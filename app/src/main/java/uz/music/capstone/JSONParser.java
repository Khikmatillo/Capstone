package uz.music.capstone;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nemo on 9/13/2017.
 */

public class JSONParser {
    private String JSONText = null;
    private ArrayList<Music> parsed_musics;

    private String music_name;
    private int position;
    private int num_of_views;
    private int best_position_ever;
    private String link;
    private String artist;
    private String genre;
    private ArrayList<String> additional_links;
    private int music_pk;

    public JSONParser(String JSONText) {
        parsed_musics = new ArrayList<Music>();
        additional_links = new ArrayList<String>();
        this.JSONText = JSONText;
    }

    public ArrayList<Music> getMusicsArray(){
        if(JSONText != null){
            try{
                JSONObject json_obj = new JSONObject(JSONText);

                JSONArray musics = json_obj.getJSONArray("results");
                for (int i = 0; i < musics.length(); i++){
                    JSONObject m = musics.getJSONObject(i);
                    music_name = m.getString("music_name");
                    position = m.getInt("position");
                    num_of_views = m.getInt("num_of_views");
                    best_position_ever = m.getInt("best_position_ever");
                    link = m.getString("link");
                    artist = m.getString("artist");
                    genre = m.getString("genre");

                    JSONArray m_add_links = m.getJSONArray("additional_links");
                    for(int j = 0; j < m_add_links.length(); j++){
                        additional_links.add(m_add_links.getString(j));
                    }

                    music_pk = m.getInt("music_pk");

                    Music music = new Music(music_name, position, num_of_views, best_position_ever,
                            link, artist, genre, additional_links, music_pk);
                    parsed_musics.add(music);
                }
            }catch (final JSONException e){
                Log.e("", "JSON PARSING ERROR " + e.getMessage());
            }
        }else{
            Log.e("", "Couldn't get json from server.");
        }
        return parsed_musics;
    }

}

