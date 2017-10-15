package uz.music.capstone.authentication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import uz.music.capstone.R;

/**
 * Created by Nemo on 10/7/2017.
 */

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText edit_reset_password_password1, edit_reset_password_password2;
    private RelativeLayout btn_reset_password_submit;
    private String password1 = null, password2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        edit_reset_password_password1 = (EditText) findViewById(R.id.edit_sign_in_mail);
        edit_reset_password_password2 = (EditText) findViewById(R.id.edit_sign_in_password);
        btn_reset_password_submit = (RelativeLayout) findViewById(R.id.btn_reset_password);

        btn_reset_password_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password1 = edit_reset_password_password1.getText().toString();
                password2 = edit_reset_password_password2.getText().toString();
            }
        });

    }

}
