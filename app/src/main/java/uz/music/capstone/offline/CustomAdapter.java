package uz.music.capstone.offline;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucasurbas.listitemview.ListItemView;

import java.util.ArrayList;

import uz.music.capstone.Music;
import uz.music.capstone.R;

/**
 * Created by Nemo on 11/12/2017.
 */

public class CustomAdapter extends BaseAdapter {

    private ArrayList<String> listviewitemsList=new ArrayList<String>();
    private ArrayList<ArrayList<Music>> folderMusics = new ArrayList<ArrayList<Music>>();


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

        ImageView image = (ImageView) view.findViewById(R.id.imageView2);
        TextView text_name = (TextView) view.findViewById(R.id.music_title);
        TextView text_artist = (TextView) view.findViewById(R.id.music_artist);
        text_name.setText(listviewitemsList.get(i));
        text_artist.setText(folderMusics.get(i).size() + " songs");

        image.setBackground(ContextCompat.getDrawable(context, R.drawable.default_folder));
        return view;
    }


    public void clearAllData(){
        listviewitemsList.clear();
        notifyDataSetChanged();
    }

    public  void addItem(String m, ArrayList<Music> fm){
        listviewitemsList.add(m);
        folderMusics.add(fm);
        Log.e("", "ITEM ADDED ");
    }
}
