package com.ofu.app.imageloader;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

public class ImagePreviewAdapter extends FragmentStatePagerAdapter {

  private ImageDataHolder mData;

  public ImagePreviewAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    return ImagePreviewFragment.newInstance(mData.getList().get(position).getImageUrl());
  }

  @Override
  public int getCount() {
    return mData.getList().size();
  }

  public void updateData(ImageDataHolder data) {
    this.mData = data;
  }
}
