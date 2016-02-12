package com.example.olivier.imageloader;

import android.app.Activity;
import android.content.ContentResolver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class ImagePreviewAdapter extends PagerAdapter {

  private Activity mContext;
  private ImageDataHolder mData;
  private final ContentResolver mContentResolver;

  public ImagePreviewAdapter(Activity context, ContentResolver contentResolver, ImageDataHolder data) {
    this.mContext = context;
    this.mContentResolver = contentResolver;
    this.mData = data;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    ImageView image;

    View v = LayoutInflater.from(mContext).inflate(R.layout.full_screen_image_preview, container, false);
    image = (ImageView) v.findViewById(R.id.image);

    new ImageLoaderTask(mContext, null, image, ImageLoaderTask.MODE_FULLSCREEN, mContentResolver).execute(mData.getList().get(position));

    container.addView(v);

    return v;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((RelativeLayout) object);
  }

  @Override
  public int getCount() {
    return mData.getList().size();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == ((RelativeLayout) object);
  }
}
