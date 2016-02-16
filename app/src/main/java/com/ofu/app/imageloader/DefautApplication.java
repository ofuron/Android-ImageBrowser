package com.ofu.app.imageloader;

import android.app.Application;

import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by olivier on 2/12/16.
 */
public class DefautApplication extends Application{

  private final int MAX_MEMORY_CACHE_SIZE = 1024*50; // 50MB
  private final int MAX_CACHE_ITEMS = 500;

  @Override
  public void onCreate() {
    super.onCreate();

    final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
        MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
        MAX_CACHE_ITEMS,       // Max entries in the cache
        MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
        MAX_CACHE_ITEMS,       // Max length of eviction queue
        MAX_CACHE_ITEMS);      // Max cache entry size

    ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
        .setBitmapMemoryCacheParamsSupplier(
            new Supplier<MemoryCacheParams>() {
              public MemoryCacheParams get() {
                return bitmapCacheParams;
              }
            })
        .setDownsampleEnabled(true)
        .build();

    Fresco.initialize(this, config);
  }
}
