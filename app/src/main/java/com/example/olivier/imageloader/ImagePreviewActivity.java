package com.example.olivier.imageloader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.example.olivier.imageloader.ImageLoaderTask.IImageLoaderListener;

public class ImagePreviewActivity extends AppCompatActivity implements IImageLoaderListener {

  public static final String EXTRA_IMAGE_ITEM = "EXTRA_IMAGE_ITEM";

  private ImageView mImage;
  private ProgressDialog mLoadingDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_preview);

    Intent intent = getIntent();
    ImageListItem item = (ImageListItem) intent.getSerializableExtra(EXTRA_IMAGE_ITEM);

    mImage   = (ImageView) findViewById(R.id.image);

    if(mImage != null) {
      showLoadingDialog();
      new ImageLoaderTask(this, mImage, ImageLoaderTask.MODE_FULLSCREEN, getContentResolver()).execute(item);
    }
  }


  private void showLoadingDialog() {
    if (mLoadingDialog == null) {
      mLoadingDialog = ProgressDialog.show(this, null, getString(R.string.loading_title), true, false);
    }
  }

  private void hideLoadingDialog() {
    if (mLoadingDialog != null) {
      mLoadingDialog.dismiss();
      mLoadingDialog = null;
    }
  }

  @Override
  public void onImageLoaded() {
    hideLoadingDialog();
  }
}
