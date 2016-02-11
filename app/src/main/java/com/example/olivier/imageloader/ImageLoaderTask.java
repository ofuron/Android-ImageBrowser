package com.example.olivier.imageloader;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by olivier on 2/10/16.
 */
public class ImageLoaderTask extends AsyncTask<ImageListItem, Void, Bitmap> {
  /**
   * Callback interface to handle image loaded. Implemented by parent
   */
  interface IImageLoaderListener {
    void onImageLoaded();
  }

  public static final int MODE_THUMB = 0;
  public static final int MODE_FULLSCREEN = 1;
  @IntDef({MODE_THUMB, MODE_FULLSCREEN})
  @Retention(RetentionPolicy.SOURCE)
  public @interface DisplayMode {}

  private final WeakReference<ImageView> mImageViewReference;
  private final ContentResolver mContentResolver;
  private final IImageLoaderListener mListener;
  private int mMode;

  public ImageLoaderTask(IImageLoaderListener listener, ImageView thumbnail, int mode, ContentResolver contentResolver) {
    mListener = listener;
    mImageViewReference = new WeakReference<>(thumbnail);
    mContentResolver = contentResolver;
    mMode = mode;
  }

  @Override
  protected Bitmap doInBackground(ImageListItem... params) {
    ImageListItem item = params[0];

    if(mMode == MODE_FULLSCREEN) {
      return BitmapFactory.decodeFile(item.getImageUrl());
    } else {
      return MediaStore.Images.Thumbnails.getThumbnail(mContentResolver, item.getId(), MediaStore.Images.Thumbnails.MICRO_KIND, null);
    }
  }

  @Override
  protected void onPostExecute(Bitmap bitmap) {
    if(isCancelled()) {
      bitmap = null;
    }

    if(mImageViewReference != null && bitmap != null) {
      ImageView thumbnail = mImageViewReference.get();
      if(thumbnail != null) {
        thumbnail.setImageBitmap(bitmap);
      }

      if(mListener != null) {
        mListener.onImageLoaded();
      }
    }
  }
}
