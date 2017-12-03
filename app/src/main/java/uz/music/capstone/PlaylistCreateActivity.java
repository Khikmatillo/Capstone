package uz.music.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import uz.music.capstone.authentication.WelcomeActivity;
import uz.music.capstone.profile.User;

/**
 * Created by Nemo on 11/27/2017.
 */

public class PlaylistCreateActivity extends AppCompatActivity {

    EditText editName, editDesc;
    Button btnChoose, btnSubmit;
    TextView image;
    int PICK_IMAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_create);

        editName = (EditText) findViewById(R.id.editPlaylistName);
        editDesc = (EditText)findViewById(R.id.editPlaylistDesc);

        btnChoose = (Button)findViewById(R.id.btnChoosePicture);
        btnSubmit = (Button)findViewById(R.id.btnPlaylistSubmit);
        image = (TextView) findViewById(R.id.imagePlaylistImage);


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editName.getText().toString() != null && editDesc.getText().toString() != null && image.getText().toString() != null){
                    SharedPreferences sp = getSharedPreferences(User.FILE_PREFERENCES, Context.MODE_PRIVATE);
                    String token = sp.getString(User.KEY_TOKEN, "");
                    Ion.with(PlaylistCreateActivity.this)
                            .load("http://moozee.pythonanywhere.com/playlist/create/").addHeader("Authorization", "Token " + token)
                            .setMultipartParameter("name", editName.getText().toString())
                            .setMultipartParameter("description", editDesc.getText().toString())
                            .setMultipartFile("photo", new File(image.getText().toString()))
                            .asString().setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Log.i("FORM DATA RESULT", jsonObject.getString("status"));
                                Toast.makeText(PlaylistCreateActivity.this, jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                        }
                    });
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {

            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};

            try {
                Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Toast.makeText(PlaylistCreateActivity.this, picturePath, Toast.LENGTH_LONG).show();
                Log.e("STRING URL::::", picturePath);

                image.setText(picturePath);

            }
            catch(Exception e) {
                Log.e("Path Error", e.toString());
            }


        }
    }
}
