package uz.music.capstone.authentication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.RelativeLayout;

import uz.music.capstone.R;

/**
 * Created by Nemo on 10/7/2017.
 */

public class ChangePasswordActivity extends AppCompatActivity {

    EditText edit_change_password_old, edit_change_password_new, edit_change_password_confirm;
    RelativeLayout btn_change_password;
    String old_password = null, new_password = null, confirm_password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edit_change_password_old = (EditText) findViewById(R.id.edit_change_password_old);
        edit_change_password_new = (EditText) findViewById(R.id.edit_change_password_new);
        edit_change_password_confirm = (EditText) findViewById(R.id.edit_change_password_confirm);
        btn_change_password = (RelativeLayout) findViewById(R.id.btn_change_password);

        old_password = edit_change_password_old.getText().toString();
        new_password = edit_change_password_new.getText().toString();
        confirm_password = edit_change_password_confirm.getText().toString();


    }

}
