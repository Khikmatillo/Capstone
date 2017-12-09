package uz.music.capstone;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uz.music.capstone.profile.User;

/**
 * Created by Nemo on 11/26/2017.
 */

public class PlaylistMusicsAdapter  extends BaseAdapter {

    private ArrayList<PlaylistMusic> listviewitemsList = new ArrayList<PlaylistMusic>();

    @Override
    public int getCount() {
        return listviewitemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return listviewitemsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos =i ;
        final Context context = viewGroup.getContext();
        if(view==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view  = inflater.inflate(R.layout.music_list_item, viewGroup, false);

        }
        PlaylistMusic music  = listviewitemsList.get(i);

        ImageView image = (ImageView) view.findViewById(R.id.imageView2);
        TextView text_name = (TextView) view.findViewById(R.id.music_title);
        TextView text_artist = (TextView) view.findViewById(R.id.music_artist);
        text_name.setText(music.getName());
//        if(music.getFile() != null){
//            text_artist.setText(music.getFile());
//        }else{
            text_artist.setText("");
        //}

        if(music.getPhotoLink() != null){
            Picasso.with(context).load(User.VARIABLE_URL + music.getPhotoLink()).into(image);
        }

        return view;
    }
    /*ImageView image = (ImageView) view.findViewById(R.id.imageView2);
        TextView text_name = (TextView) view.findViewById(R.id.music_title);
        TextView text_artist = (TextView) view.findViewById(R.id.music_artist);
        text_name.setText(listviewitemsList.get(i));
        text_artist.setText(folderMusics.get(i).size() + " songs");*/


    public void clearAllData(){
        listviewitemsList.clear();
        notifyDataSetChanged();
    }

    public  void addItem(PlaylistMusic m){
        listviewitemsList.add(m);
        Log.e("", "ITEM ADDED ");
    }
}
