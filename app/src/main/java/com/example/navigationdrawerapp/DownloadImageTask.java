package com.example.navigationdrawerapp;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    String url;
    Context context;
    public static final String IMAGE = "image/*";
    ProgressBar progressBar;
    public DownloadImageTask(ImageView bmImage, Context context) {
        this.context=context;
        this.bmImage = bmImage;

    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        url = urldisplay;
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }
    protected void onPostExecute(Bitmap result) {

        bmImage.setImageBitmap(result);
        bmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(Uri.parse(url),IMAGE);
                context.startActivity(intent);
            }
        });

    }
}
