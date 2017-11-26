package uz.music.capstone.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import uz.music.capstone.IndexActivity;
import uz.music.capstone.ListedMusicsActivity;
import uz.music.capstone.R;
import uz.music.capstone.profile.User;

/**
 * Created by Nemo on 10/7/2017.
 */

public class SignInActivity extends AppCompatActivity {

    private EditText edit_sign_in_mail, edit_sign_in_password;
    private RelativeLayout btn_sign_in;
    private TextView txt_sign_in_forget, txt_sign_in_create;
    private String mail = null, password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edit_sign_in_mail = (EditText) findViewById(R.id.edit_sign_in_mail);
        edit_sign_in_password = (EditText) findViewById(R.id.edit_sign_in_password);
        btn_sign_in = (RelativeLayout) findViewById(R.id.btn_sign_in);
        txt_sign_in_forget = (TextView) findViewById(R.id.txt_sign_in_forget);
        txt_sign_in_create = (TextView) findViewById(R.id.txt_sign_in_create);

        SharedPreferences sp = getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
        String token = sp.getString(User.KEY_TOKEN, "");
        if (token != "") {
            Intent intent = new Intent(SignInActivity.this, IndexActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = edit_sign_in_mail.getText().toString();
                password = edit_sign_in_password.getText().toString();
                JSONObject jsonObject = new JSONObject();
                try {
                    if (Validation.validateUserName(mail)) {
                        if (Validation.validatePassword(password)) {
                            jsonObject.put(User.KEY_USERNAME, mail);
                            jsonObject.put(User.KEY_PASSWORD, password);
                            jsonObject.put(User.KEY_TYPE, User.TYPE_LOGIN);
                            Log.e("data0", jsonObject.toString());
                            new SendJSON(getApplicationContext(), jsonObject);
                            if (User.USER_ACCEPTED) {
                                Intent intent = new Intent(SignInActivity.this, ListedMusicsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(SignInActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, "enter valid username", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("Sign In", e.getMessage());
                }
            }
        });

        txt_sign_in_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        txt_sign_in_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }


}
