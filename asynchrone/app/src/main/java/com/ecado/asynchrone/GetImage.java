package com.ecado.asynchrone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class GetImage extends AsyncTask<Void,Void,Void> {
    private Bitmap bmp;
    private String imageURL;
    public ImageView imageView;
    public GetImage(String url, ImageView image){
        imageURL=url;
        imageView=image;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            InputStream in = new URL(imageURL).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            // log error
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (bmp != null)
            imageView.setImageBitmap(bmp);
    }
}
