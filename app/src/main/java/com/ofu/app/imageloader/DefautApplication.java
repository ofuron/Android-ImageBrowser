package com.ofu.app.imageloader;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by olivier on 2/12/16.
 */
public class DefautApplication extends Application{

  @Override
  public void onCreate() {
    super.onCreate();

    Fresco.initialize(this);
  }
}
