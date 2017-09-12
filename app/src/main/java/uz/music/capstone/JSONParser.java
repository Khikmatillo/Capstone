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
    private String music_title, music_author;
    private int music_daily, music_weekly, music_monthly;

    public JSONParser(String JSONText) {
        parsed_musics = new ArrayList<Music>();
        this.JSONText = JSONText;
    }

    public ArrayList<Music> getMusicsArray(){
        if(JSONText != null){
            try{
                JSONObject json_obj = new JSONObject(JSONText);
                JSONArray musics = json_obj.getJSONArray("info");
                for (int i = 0; i < musics.length(); i++){
                    JSONObject m = musics.getJSONObject(i);
                    music_title = m.getString("title");
                    music_author = m.getString("author");
                    music_daily = m.getInt("daily");
                    music_weekly = m.getInt("weekly");
                    music_monthly = m.getInt("monthly");

                    Music music = new Music(music_title, music_author, music_daily, music_weekly, music_monthly);
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
