package com.ofu.app.imageloader;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity implements Gallery.IDataLoadedCallback, ImageListAdapter.OnImageListItemClickListener {

  private static final int REQUEST_TAKE_PHOTO = 1;
  private static final String APP_PICTURE_DIR = "PicTool";

  private Gallery mGallery;
  private RecyclerView mImageListView;
  private ImageListAdapter mAdapter;
  private ImageDataHolder mDataHolder;
  private String mCurrentPhotoPath;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Create the adapter
    mAdapter = new ImageListAdapter(this, this);

    mImageListView = (RecyclerView) findViewById(R.id.image_listview);
    //mImageListView.setLayoutManager(new LinearLayoutManager(this, android.support.v7.widget.LinearLayoutManager.VERTICAL, false));
    mImageListView.setLayoutManager(new VarColumnGridLayoutManager(this, (int) getResources().getDimension(R.dimen.grid_item_width)));

    mImageListView.setAdapter(mAdapter);

    // Get SDCARD image path
    mGallery = Gallery.with(this);
    mGallery.refreshData(this, this);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
          File pictureFile = createNewPictureFile();
          if (pictureFile != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
          }
          startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
        }
      }
    });
  }


  @Override
  protected void onPause() {
    super.onPause();
    ImagePipeline imagePipeline = Fresco.getImagePipeline();
    imagePipeline.clearCaches();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
      addPicToGallery(mCurrentPhotoPath);
    }
  }

  @Override
  public void onItemClicked(int position) {
    lauchImagePreviewActivity(position);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_home, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onDataLoadedCallback(ImageDataHolder data) {
    mDataHolder = data;
    mAdapter.updateData(data);
  }

  private static File getSharedDirectory(){
    return new File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DCIM), APP_PICTURE_DIR);
  }

  public void lauchImagePreviewActivity(int position) {
    Intent intent = new Intent(this, ImagePreviewActivity.class);
    intent.putExtra(ImagePreviewActivity.EXTRA_IMAGE_DATA, mDataHolder);
    intent.putExtra(ImagePreviewActivity.EXTRA_IMAGE_POSITION, position);
    startActivity(intent);
  }

  public File createNewPictureFile() {
    try {
      // Get or create shared dir
      File sharedDir = getSharedDirectory();
      if (! sharedDir.exists()){
        if (! sharedDir.mkdirs()){
          return null;
        }
      }

      // Create an image file name
      String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      String imageFileName = "JPEG_" + timeStamp + "_";
      File dataFile = new File(sharedDir.getPath() + File.separator + imageFileName + ".jpg");
      mCurrentPhotoPath = dataFile.getAbsolutePath();
      return dataFile;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public void addPicToGallery(String path) {
    MediaScannerConnection.scanFile(
        getApplicationContext(),
        new String[]{path},
        null,
        new MediaScannerConnection.OnScanCompletedListener() {
          @Override
          public void onScanCompleted(String s, Uri uri) {
             mGallery.refreshData(HomeActivity.this, HomeActivity.this);
          }
        });
  }

}
