package uz.music.capstone;

/**
 * Created by Maestro on 9/6/2017.
 */



        import android.content.Context;


        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;

        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;


        import com.lucasurbas.listitemview.ListItemView;

        import java.util.ArrayList;

/**
 * Created by Maestro on 7/31/2017.
 */

public class MusicAdapter extends BaseAdapter{
    int type=0;
    public MusicAdapter(){}
    public MusicAdapter(int type){
        this.type = type;
    }


    private ArrayList<Music> listviewitemsList=
            new ArrayList<Music>();
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
            if(type == 1){
                view  = inflater.inflate(R.layout.music_list_item_with_check, viewGroup, false);
            }else{
                view  = inflater.inflate(R.layout.music_list_item, viewGroup, false);
            }
        }
        Music music  = listviewitemsList.get(i);

        ImageView image = (ImageView) view.findViewById(R.id.imageView2);
        TextView text_name = (TextView) view.findViewById(R.id.music_title);
        TextView text_artist = (TextView) view.findViewById(R.id.music_artist);
        text_name.setText(music.getMusic_name());
        text_artist.setText(music.getArtist());
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

    public  void addItem(Music m){
        listviewitemsList.add(m);
        Log.e("", "ITEM ADDED ");
    }
}
