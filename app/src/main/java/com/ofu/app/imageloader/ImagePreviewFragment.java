package com.ofu.app.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
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

    Uri uri = Uri.fromFile(new File(mUrl));

    WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);

    SimpleDraweeView image;
    image = (SimpleDraweeView) rootView.findViewById(R.id.image);

    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
        .setResizeOptions(new ResizeOptions(size.x, size.y))
        .build();

    PipelineDraweeController controller = (PipelineDraweeController)
        Fresco.newDraweeControllerBuilder()
            .setImageRequest(request)
            .setOldController(image.getController())
            .build();
    image.setController(controller);

    return rootView;
  }
}
