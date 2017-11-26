package uz.music.capstone.IndexBottomSheetFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import uz.music.capstone.IndexActivity;
import uz.music.capstone.R;
import uz.music.capstone.profile.EditProfileActivity;
import uz.music.capstone.profile.User;


/**
 * Created by Nemo on 10/13/2017.
 */

public class ProfileFragment extends Fragment {

    LinearLayout ll_favourites, ll_playlists, ll_settings, ll_conatiner_info;
    TextView txt_create, txt_name, txt_location, txt_following, txt_followers;
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        ll_favourites = (LinearLayout) view.findViewById(R.id.profile_favourites);
        ll_playlists = (LinearLayout) view.findViewById(R.id.profile_playlists);
        ll_settings = (LinearLayout) view.findViewById(R.id.profile_settings);
        ll_conatiner_info = (LinearLayout) view.findViewById(R.id.profile_container_info);

        txt_name = (TextView) view.findViewById(R.id.profile_name);
        txt_followers = (TextView) view.findViewById(R.id.profile_followers);
        txt_following = (TextView) view.findViewById(R.id.profile_following);
        image = (ImageView) view.findViewById(R.id.profile_img);

//        if(IndexActivity.CURRENT_USER == null){
//            ll_conatiner_info.setVisibility(View.GONE);
//        }else{
//            txt_create.setVisibility(View.GONE);
//        }
//
//        txt_create.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), SignUpActivity.class);
//                startActivityForResult(intent, 1);
//            }
//        });



        ll_favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ll_playlists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ll_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                getActivity().startActivity(intent);
            }
        });


        return view;
    }

//        btn_profile_follow = (LinearLayout) findViewById(R.id.btn_profile_follow);
//        txt_profile_follow = (TextView) findViewById(R.id.txt_profile_follow);
//        txt_profile_edit = (TextView) findViewById(R.id.txt_profile_edit);
//        txt_profile_followers = (TextView) findViewById(R.id.txt_profile_followers);
//        txt_profile_following = (TextView) findViewById(R.id.txt_profile_following);
//        txt_profile_name = (TextView) findViewById(R.id.txt_profile_name);
//        txt_profile_city = (TextView) findViewById(R.id.txt_profile_city);
//        txt_profile_about = (TextView) findViewById(R.id.txt_profile_about);
//        txt_profile_info = (TextView) findViewById(R.id.txt_profile_info);

//        txt_profile_followers.setText(user.getFollowers().size());
//        txt_profile_following.setText(user.getFollowing().size());
//        txt_profile_name.setText(user.getName());
//        txt_profile_city.setText(user.getCity() + ", " + user.getCountry());
//        txt_profile_about.setText(user.getAbout());
//        txt_profile_info.setText(user.getInfo());


       /* btn_profile_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListedMusicsActivity.CURRENT_USER.addFollowing(user);
                user.addFollower(ListedMusicsActivity.CURRENT_USER);
                txt_profile_follow.setText("Following");
            }
        });*/

//        txt_profile_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ProfileFragment.this, EditProfile.class);
//
//                intent.putExtra("name", user.getName());
//                intent.putExtra("city", user.getCity());
//                intent.putExtra("country", user.getCountry());
//                intent.putExtra("about", user.getAbout());
//                intent.putExtra("info", user.getInfo());
//                startActivityForResult(intent, 1);
//            }
//        });



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                User user = IndexActivity.CURRENT_USER;
                if(user != null){
                    txt_create.setVisibility(View.GONE);
                    ll_conatiner_info.setVisibility(View.VISIBLE);
                    txt_name.setText(user.getName());
                    txt_following.setText(user.getFollowing().size() + "");
                    txt_followers.setText(user.getFollowers().size() + "");
                }else{
                    Toast.makeText(getActivity(), "error while signing up", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getActivity(), "sign in cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
