package com.ofu.app.imageloader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.ofu.app.imageloader.ImageLoaderTask.IImageLoaderListener;

public class ImagePreviewActivity extends AppCompatActivity implements IImageLoaderListener {

  public static final String EXTRA_IMAGE_DATA = "EXTRA_IMAGE_DATA";
  public static final String EXTRA_IMAGE_POSITION = "EXTRA_IMAGE_POSITION";

  private ViewPager mPager;
  private ProgressDialog mLoadingDialog;

  private ScaleGestureDetector mScaleDetector;
  private float mScaleFactor = 1.f;
  private Matrix mMatrix = new Matrix();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_preview);

    Intent intent = getIntent();
    ImageDataHolder data = (ImageDataHolder) intent.getSerializableExtra(EXTRA_IMAGE_DATA);
    int position = intent.getIntExtra(EXTRA_IMAGE_POSITION, 0);

    mPager = (ViewPager) findViewById(R.id.pager);
    mPager.setAdapter(new ImagePreviewAdapter(this, getContentResolver(), data));
    mPager.setCurrentItem(position);

    mScaleDetector = new ScaleGestureDetector(this, new ImageScaleListener());
  }


  @Override
  public boolean onTouchEvent(MotionEvent event) {
    mScaleDetector.onTouchEvent(event);
    return true;
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


  private class ImageScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
      mScaleFactor *= detector.getScaleFactor();
      // Don't let the object get too small or too large.
      mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
      mMatrix.setScale(mScaleFactor, mScaleFactor);
      //mImage.setImageMatrix(mMatrix);
      return true;
    }
  }
}
