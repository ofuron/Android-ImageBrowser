package com.ofu.app.imageloader;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by olivier on 2/10/16.
 */
public class GalleryLoader {
  private static final String TAG = GalleryLoader.class.getSimpleName();

  public static ImageDataHolder loadGallery(Context context) {
    final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME};
    final String orderBy = MediaStore.Images.Media._ID + " DESC";
    Cursor cursor = null;

    ImageDataHolder imageList = new ImageDataHolder();

    try {
      cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

      if(cursor != null && cursor.moveToFirst()) {
        do {
          ImageListItem item = new ImageListItem();

          String path = cursor.getString(0);
          if(!TextUtils.isEmpty(path)) {
            item.setImageUrl(path);
          }

          Long id = cursor.getLong(1);
          item.setId(id);

          String title = cursor.getString(2);
          if(!TextUtils.isEmpty(title)) {
            item.setTitle(title);
          }

          imageList.getList().add(item);

        } while(cursor.moveToNext());
      }

    } catch (Exception e) {
      Log.e(TAG, e.toString());
    }

    return imageList;
  }
}
