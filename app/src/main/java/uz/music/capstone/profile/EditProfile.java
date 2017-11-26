package uz.music.capstone.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import uz.music.capstone.IndexActivity;
import uz.music.capstone.R;
import uz.music.capstone.authentication.WelcomeActivity;

/**
 * Created by Nemo on 10/15/2017.
 */

public class EditProfile extends AppCompatActivity {

    private Button btnLogOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnLogOut = (Button) findViewById(R.id.btnLogOut);


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(User.KEY_TOKEN);
                editor.commit();
                IndexActivity.CURRENT_USER = null;
                Intent intent = new Intent(EditProfile.this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}
