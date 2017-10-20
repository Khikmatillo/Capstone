package uz.music.capstone.authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import uz.music.capstone.MainActivity;
import uz.music.capstone.R;
import uz.music.capstone.profile.User;

public class SignUpActivity extends AppCompatActivity {

    private EditText edit_sign_up_username, edit_sign_up_mail, edit_sign_up_password1, edit_sign_up_password2;
    private RelativeLayout btn_sign_up;
    private CheckBox check_sign_up_agree;
    private TextView txt_sign_up_login;

    private String username = null, mail = null, password1 = null, password2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edit_sign_up_username = (EditText) findViewById(R.id.edit_sign_up_username);
        edit_sign_up_mail = (EditText) findViewById(R.id.edit_sign_up_mail);
        edit_sign_up_password1 = (EditText) findViewById(R.id.edit_sign_up_password1);
        edit_sign_up_password2 = (EditText) findViewById(R.id.edit_sign_up_password2);
        btn_sign_up = (RelativeLayout) findViewById(R.id.btn_sign_up);
        check_sign_up_agree = (CheckBox) findViewById(R.id.check_sign_up_agree);
        txt_sign_up_login = (TextView) findViewById(R.id.txt_sign_up_login);

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_sign_up_agree.isChecked()){
                    username = edit_sign_up_username.getText().toString();
                    mail = edit_sign_up_mail.getText().toString();
                    password1 = edit_sign_up_password1.getText().toString();
                    password2 = edit_sign_up_password2.getText().toString();

                    if(Validation.validateUserName(username)){
                        if(Validation.validateEmail(mail)){
                            if(Validation.validatePassword(password1)){
                                if(Validation.validateConfirmPassword(password1, password2)){
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put(User.KEY_USERNAME, username);
                                        jsonObject.put(User.KEY_EMAIL, mail);
                                        jsonObject.put(User.KEY_PASSWORD1, password1);
                                        jsonObject.put(User.KEY_PASSWORD2, password2);
                                        jsonObject.put(User.KEY_TYPE, User.TYPE_CREATE);
                                        Log.e("data0", jsonObject.toString());
                                        new SendJSON(getApplicationContext(), jsonObject);
                                        if(User.USER_ACCEPTED){
                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        Log.e("Sign Up", e.getMessage());
                                    }
                                }else{
                                    Toast.makeText(SignUpActivity.this, "Passwords are not same", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(SignUpActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SignUpActivity.this, "enter valid email", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SignUpActivity.this, "Username must be at least 5 char", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignUpActivity.this, "You have to be agree", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_sign_up_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }



}
