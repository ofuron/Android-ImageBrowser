package com.ofu.app.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by olivier on 2/10/16.
 */
public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

  private final WeakReference<ImageView> imageViewReference;

  public ImageDownloaderTask(ImageView thumbnail) {
    imageViewReference = new WeakReference<>(thumbnail);
  }

  @Override
  protected Bitmap doInBackground(String... params) {
    String url = params[0];

    HttpURLConnection connection = null;

    try {
      URL uri = new URL(url);
      connection = (HttpURLConnection) uri.openConnection();
      connection.setDoInput(true);
      connection.connect();

      int statusCode = connection.getResponseCode();
      if(statusCode == HttpURLConnection.HTTP_OK) {
        InputStream in = connection.getInputStream();
        Bitmap result = BitmapFactory.decodeStream(in);
        return result;
      }


    } catch (Exception e) {
      connection.disconnect();
      Log.e("ImageDownloader", e.toString());
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }

    return null;
  }

  @Override
  protected void onPostExecute(Bitmap bitmap) {
    if(isCancelled()) {
      bitmap = null;
    }

    if(imageViewReference != null && bitmap != null) {
      ImageView thumbnail = imageViewReference.get();
      if(thumbnail != null) {
        thumbnail.setImageBitmap(bitmap);
      }
    }
  }
}
