package com.ofu.app.imageloader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity implements Gallery.IDataLoadedCallback, ImageListAdapter.OnImageListItemClickListener{

    private RecyclerView mImageListView;
    private ImageListAdapter mAdapter;
    private ImageDataHolder mDataHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter
        mAdapter = new ImageListAdapter(this, this);

        mImageListView = (RecyclerView) findViewById(R.id.image_listview);
        mImageListView.setLayoutManager(new LinearLayoutManager(this, android.support.v7.widget.LinearLayoutManager.VERTICAL, false));
        mImageListView.setAdapter(mAdapter);

        // Get SDCARD image path
        final Gallery gallery= Gallery.with(this);
        gallery.refreshData(this, this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
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

    public void lauchImagePreviewActivity(int position) {
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.EXTRA_IMAGE_DATA, mDataHolder);
        intent.putExtra(ImagePreviewActivity.EXTRA_IMAGE_POSITION, position);
        startActivity(intent);
    }
}
