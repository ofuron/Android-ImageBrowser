package com.ofu.app.imageloader;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ofu.app.imageloader.R;

import java.io.File;


public class ImagePreviewFragment extends Fragment {
  private static final String IMAGE_URL = "image_fragment.url";

  private String mUrl;

  public static ImagePreviewFragment newInstance(String url) {
    ImagePreviewFragment fragment = new ImagePreviewFragment();
    Bundle args = new Bundle();
    args.putString(IMAGE_URL, url);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mUrl = getArguments().getString(IMAGE_URL);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView =  inflater.inflate(R.layout.fragment_image_preview, container, false);

    SimpleDraweeView image;
    image = (SimpleDraweeView) rootView.findViewById(R.id.image);
    Uri uri = Uri.fromFile(new File(mUrl));
    image.setImageURI(uri);

    return rootView;
  }
}
