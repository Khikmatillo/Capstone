package uz.music.capstone.offline;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import uz.music.capstone.R;

/**
 * Created by Nemo on 11/6/2017.
 */

public class FragmentPlaylists extends Fragment {

    LinearLayout container;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_offline_playlists, container, false);
        container = (LinearLayout) view.findViewById(R.id.offline_playlist_container);
        ListView lw = (ListView) view.findViewById(R.id.offline_playlist_list);
        CustomAdapter adapter = new CustomAdapter();
        lw.setAdapter(adapter);

        LinearLayout LL = new LinearLayout(getActivity());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LL.setLayoutParams(params);
        LL.setOrientation(LinearLayout.HORIZONTAL);
        LL.setClickable(true);
        LL.setGravity(Gravity.CENTER);
        ImageView imageView = new ImageView(getActivity());
        TextView textView = new TextView(getActivity());
        textView.setText("Add playlist");
        textView.setTextSize(35);
        LL.addView(imageView);
        LL.addView(textView);

        LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Create Playlist")
                        .setPositiveButton("create", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
            }
        });
        container.addView(LL);

        return view;
    }
}
