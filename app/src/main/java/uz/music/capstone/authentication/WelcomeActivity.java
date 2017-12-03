package uz.music.capstone.authentication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import uz.music.capstone.IndexActivity;
import uz.music.capstone.R;
import uz.music.capstone.profile.User;

/**
 * Created by Nemo on 11/27/2017.
 */

public class WelcomeActivity extends AppCompatActivity{

    Button btnLogIn, btnSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (shouldShowRequestPermissionRationale(
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            }
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    1234);
//            return;
//        }

        SharedPreferences sp = getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
        String token = sp.getString(User.KEY_TOKEN, "");
        if (!token.equals("")) {
            Intent intent = new Intent(WelcomeActivity.this, IndexActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
//    @Override
//    public void onRequestPermissionsResult(final int requestCode, @NonNull final
//    String[] permissions, @NonNull final int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1234) {
//            if (grantResults.length > 0 && grantResults[0] ==
//                    PackageManager.PERMISSION_GRANTED) {
//// Permission granted.
//                Toast.makeText(WelcomeActivity.this, "grant", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(WelcomeActivity.this, "deny", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

}
