package uz.music.capstone.offline;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import uz.music.capstone.Playlist;
import uz.music.capstone.R;
import uz.music.capstone.profile.User;

/**
 * Created by Nemo on 11/6/2017.
 */

public class FragmentPlaylists extends Fragment {

    private String playlist_name="";
    LinearLayout layout, create;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_offline_playlists, container, false);
        create = (LinearLayout) view.findViewById(R.id.txt_offline_playlist_create);
        layout = (LinearLayout)view.findViewById(R.id.offline_playlist_container);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("New Playlist");
                // Set up the input
                final EditText input = new EditText(getActivity());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setGravity(Gravity.CENTER);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playlist_name = input.getText().toString();
                        Intent intent = new Intent(getActivity(), PlaylistChooseActivity.class);
                        intent.putExtra("name", playlist_name);
                        startActivityForResult(intent, 1);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });





        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                ArrayList<Playlist> playlists = User.PLAYLISTS;
                Playlist myPlaylist = null;
                for (int i = 0; i < playlists.size(); i++){
                    if(playlists.get(i).getName().equals(playlist_name)){
                        myPlaylist = playlists.get(i);
                    }
                }
                if(myPlaylist == null){
                    Toast.makeText(getActivity(), "Something error", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Worked ok", Toast.LENGTH_SHORT).show();
                    final TextView textView = new TextView(getActivity());
                    textView.setText(playlist_name);
                    textView.setTextSize(35);
                    textView.setPadding(10,5,5,5);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), textView.getText(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    View divider = new View(getActivity());
                    divider.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    divider.setBackgroundColor(Color.BLACK);
                    layout.addView(textView);
                    layout.addView(divider);
                }
            }
        }
    }
}
