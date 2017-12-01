package uz.music.capstone;

import android.content.Intent;
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

import java.io.File;

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
