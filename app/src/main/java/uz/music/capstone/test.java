package uz.music.capstone;

import android.content.Context;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

/**
 * Created by Nemo on 11/26/2017.
 */

public class test {
    public test(Context context, String url, String token) {
        Ion.with(context)
                .load(url).addHeader("Authorization", "Token " + token)
                .setMultipartParameter("title", "title of asdsad")
                .setMultipartFile("image", new File(url))
                .asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (result.equals("4")) {

                }
            }
        });
    }
}
