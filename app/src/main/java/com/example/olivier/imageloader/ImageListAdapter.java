package com.example.olivier.imageloader;

import android.app.Activity;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by olivier on 2/10/16.
 */
public class ImageListAdapter extends RecyclerView.Adapter {
  /**
   * Callback interface to handle buy button click. Implemented by parent
   */
  interface OnImageListItemClickListener {
    void onItemClicked(ImageListItem data);
  }

  /**
   *   View holder to show items
   */
  private class ImageItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mTitle;
    private ImageView mThumbnail;

    public ImageItemViewHolder(View v) {
      super(v);
      mTitle = (TextView) v.findViewById(R.id.title);
      mThumbnail = (ImageView) v.findViewById(R.id.thumbnail);
      v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if(mListener != null) {
        mListener.onItemClicked(getImageItemAt(getLayoutPosition()));
      }
    }
  }

  private Activity mContext;
  private OnImageListItemClickListener mListener;
  private ArrayList<ImageListItem> mData;

  public ImageListAdapter(Activity context, OnImageListItemClickListener listener) {
    this.mContext = context;
    this.mListener = listener;
  }

  public void updateData(ArrayList<ImageListItem> data) {
    this.mData = data;
    notifyDataSetChanged();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ImageItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_view_item, parent, false));
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    final ImageItemViewHolder vh = (ImageItemViewHolder) holder;
    final ImageListItem item = getImageItemAt(position);
    vh.mTitle.setText(item.getTitle());
    new ImageLoaderTask(null, vh.mThumbnail, ImageLoaderTask.MODE_THUMB, mContext.getContentResolver()).execute(mData.get(position));
  }

  @Override
  public int getItemCount() {
    return mData == null ? 0 : mData.size();
  }

  //Get item by position in adapter.
  private ImageListItem getImageItemAt(int positionInAdapter) {
    return mData.get(positionInAdapter);
  }
}
