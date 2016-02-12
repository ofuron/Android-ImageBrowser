package com.example.olivier.imageloader;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by olivier on 2/10/16.
 */
public class Gallery {

  private static Gallery s_inst;

  //  Create Gallery
  public static Gallery with(Context context) {
    if(s_inst == null) {
      s_inst = new Gallery(context);
    }
    return s_inst;
  }

  private Context mContext;
  private Set<IDataLoadedCallback> mCallbacks = new HashSet<>();

  public Gallery(Context context) {
    this.mContext = context;
  }

  public interface IDataLoadedCallback {
    void onDataLoadedCallback(ImageDataHolder data);
  }

  public void refreshData(final IDataLoadedCallback callback, final Context context) {
    mCallbacks.add(callback);

    AsyncTask<Void, Void, ImageDataHolder > loadImagesTask = new AsyncTask<Void, Void, ImageDataHolder >() {
      @Override
      protected ImageDataHolder doInBackground(Void... params) {
        return GalleryLoader.loadGallery(context);
      }

      @Override
      protected void onPostExecute(ImageDataHolder data) {
        if(!mCallbacks.isEmpty()) {
          for( IDataLoadedCallback callback : mCallbacks) {
            callback.onDataLoadedCallback(data);
          }
        }
      }
    };

    loadImagesTask.execute();

  }
}
