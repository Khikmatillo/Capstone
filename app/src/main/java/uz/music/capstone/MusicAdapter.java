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

        import android.widget.TextView;



        import java.util.ArrayList;

/**
 * Created by Maestro on 7/31/2017.
 */

public class MusicAdapter extends BaseAdapter{

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
            view  = inflater.inflate(R.layout.music_list_item, viewGroup, false);
        }

//        ImageView icon = (ImageView) view.findViewById(R.id.avatarPictureXML);


        TextView titleStr = (TextView) view.findViewById(R.id.list_item_multi);
        //use custom font for memo information text in list item


        Music listViewitem  = listviewitemsList.get(i);

        titleStr.setText(listViewitem.getMusic_name());



        return view;
    }

//    public String memotext;
//    public String datetext;
//    public Drawable icontext;

    public  void addItem(Music m){
        listviewitemsList.add(m);
        Log.e("", "ITEM ADDED ");
    }
}
