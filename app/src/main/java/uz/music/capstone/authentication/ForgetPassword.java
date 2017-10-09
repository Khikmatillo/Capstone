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

public class ForgetPassword extends AppCompatActivity {

    EditText edit_forget_password_mail;
    RelativeLayout btn_forget_password_submit;
    String mail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        edit_forget_password_mail = (EditText) findViewById(R.id.edit_forget_password_mail);
        btn_forget_password_submit = (RelativeLayout) findViewById(R.id.btn_forget_password_submit);

        btn_forget_password_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = edit_forget_password_mail.getText().toString();
            }
        });

    }

}
