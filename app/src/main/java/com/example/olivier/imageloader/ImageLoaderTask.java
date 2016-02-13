package com.example.olivier.imageloader;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
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

  private static final String TAG = GalleryLoader.class.getSimpleName();

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
  private final Point mScreenSize = new Point();
  private int mMode;

  public ImageLoaderTask(Context context, IImageLoaderListener listener, ImageView thumbnail, int mode, ContentResolver contentResolver) {
    mListener = listener;
    mImageViewReference = new WeakReference<>(thumbnail);
    mContentResolver = contentResolver;
    mMode = mode;

    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    display.getSize(mScreenSize);
  }

  @Override
  protected Bitmap doInBackground(ImageListItem... params) {
    ImageListItem item = params[0];

    if(mMode == MODE_FULLSCREEN) {
      try {
        File f = new File(item.getImageUrl());

        // if image size is too large.Then scale image.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new FileInputStream(f), null, options);

        int scale = 1;
        while(options.outWidth/scale > mScreenSize.x ||options.outHeight/scale > mScreenSize.y) {
          scale *=2;
        }

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = scale;
        return BitmapFactory.decodeStream(new FileInputStream(f), null, options2);
      } catch (Exception e) {
        Log.e(TAG, e.toString());
        return null;
      }
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
      ImageView image = mImageViewReference.get();
      if(image != null) {
        image.setImageBitmap(bitmap);

        if(mMode == MODE_FULLSCREEN) {
          Matrix m = image.getImageMatrix();
          RectF viewRect = new RectF(0, 0, image.getWidth(), image.getHeight());
          RectF drawableRect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());

          m.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);

          image.setImageMatrix(m);
        }
      }

      if(mListener != null) {
        mListener.onImageLoaded();
      }
    }
  }
}
