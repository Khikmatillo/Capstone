package uz.music.capstone;

/**
 * Created by Maestro on 11/5/2017.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CustomAdapterForRecyclerItem extends RecyclerView.Adapter<CustomAdapterForRecyclerItem.MyViewHolder> {

    ArrayList<String> musicNames;
    ArrayList<String> musicArtists;
    ArrayList<Integer> musicImages;
    Boolean upOrdown;
    Context context;

    public CustomAdapterForRecyclerItem(Context context, ArrayList<String> musicNames, ArrayList<String> musicArtists,
                                        ArrayList<Integer> musicImages, Boolean upOrdown)
    {
        this.context = context;
        this.musicNames = musicNames;
        this.musicArtists =musicArtists;
        this.musicImages = musicImages;
        this.upOrdown = upOrdown;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(!upOrdown) {
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        } else
        {
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayoutupper, parent, false);
        }
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.name.setText(musicNames.get(position));
        holder.artist.setText(musicArtists.get(position));
        holder.image.setImageResource(musicImages.get(position));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, musicNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return musicNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView artist;
        ImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);


            name = (TextView) itemView.findViewById(R.id.name);
            artist = (TextView) itemView.findViewById(R.id.artist);
            image = (ImageView) itemView.findViewById(R.id.image);


        }
    }
}
