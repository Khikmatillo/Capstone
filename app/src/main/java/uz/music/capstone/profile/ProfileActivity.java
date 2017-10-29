package uz.music.capstone.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import uz.music.capstone.MainActivity;
import uz.music.capstone.R;


/**
 * Created by Nemo on 10/13/2017.
 */

public class ProfileActivity extends AppCompatActivity {

    private User user;

    private ImageView img_profile_image;
    private TextView txt_profile_name, txt_profile_city, txt_profile_about, txt_profile_info;
    LinearLayout btn_profile_follow;
    TextView txt_profile_follow, txt_profile_edit, txt_profile_followers, txt_profile_following;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btn_profile_follow = (LinearLayout) findViewById(R.id.btn_profile_follow);
        txt_profile_follow = (TextView) findViewById(R.id.txt_profile_follow);
        txt_profile_edit = (TextView) findViewById(R.id.txt_profile_edit);
        txt_profile_followers = (TextView) findViewById(R.id.txt_profile_followers);
        txt_profile_following = (TextView) findViewById(R.id.txt_profile_following);
        txt_profile_name = (TextView) findViewById(R.id.txt_profile_name);
        txt_profile_city = (TextView) findViewById(R.id.txt_profile_city);
        txt_profile_about = (TextView) findViewById(R.id.txt_profile_about);
        txt_profile_info = (TextView) findViewById(R.id.txt_profile_info);

//        txt_profile_followers.setText(user.getFollowers().size());
//        txt_profile_following.setText(user.getFollowing().size());
//        txt_profile_name.setText(user.getName());
//        txt_profile_city.setText(user.getCity() + ", " + user.getCountry());
//        txt_profile_about.setText(user.getAbout());
//        txt_profile_info.setText(user.getInfo());


        btn_profile_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.CURRENT_USER.addFollowing(user);
                user.addFollower(MainActivity.CURRENT_USER);
                txt_profile_follow.setText("Following");
            }
        });

        txt_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfile.class);

                intent.putExtra("name", user.getName());
                intent.putExtra("city", user.getCity());
                intent.putExtra("country", user.getCountry());
                intent.putExtra("about", user.getAbout());
                intent.putExtra("info", user.getInfo());
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                txt_profile_name.setText(intent.getStringExtra("name"));
                txt_profile_city.setText(intent.getStringExtra("city") + ", " + intent.getStringExtra("country"));
                txt_profile_about.setText(intent.getStringExtra("about"));
                txt_profile_info.setText(intent.getStringExtra("info"));
            }
        }
    }
}
