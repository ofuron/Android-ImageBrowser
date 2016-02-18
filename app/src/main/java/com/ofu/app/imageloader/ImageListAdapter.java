package com.ofu.app.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;


public class ImageListAdapter extends RecyclerView.Adapter {

  private static final String TAG = ImageListAdapter.class.getSimpleName();

  /**
   * Callback interface to handle buy button click. Implemented by parent
   */
  interface OnImageListItemClickListener {
    void onItemClicked(int position);
  }

  /**
   *   View holder to show items
   */
  private class ImageItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mTitle;
    private SimpleDraweeView mThumbnail;
    private CheckBox mSelectedCheckBox;
    private ImageLoaderTask mTask;

    public ImageItemViewHolder(View v) {
      super(v);
      mTitle = (TextView) v.findViewById(R.id.title);
      mThumbnail = (SimpleDraweeView) v.findViewById(R.id.thumbnail);
      mSelectedCheckBox = (CheckBox) v.findViewById(R.id.select);
      v.setOnClickListener(this);

      mSelectedCheckBox.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          final int position = ImageItemViewHolder.this.getAdapterPosition();
          if( ((CheckBox) v).isChecked() ) {
            mData.addItemToSelected(mData.getList().get(position).getId(), mData.getList().get(position));
          } else {
            mData.removeItemFromSelected(mData.getList().get(position).getId());
          }
        }
      });
    }

    @Override
    public void onClick(View v) {
      if(mListener != null) {
        mListener.onItemClicked(getLayoutPosition());
      }
    }
  }

  private Activity mContext;
  private OnImageListItemClickListener mListener;
  private ImageDataHolder mData;

  public ImageListAdapter(Activity context, OnImageListItemClickListener listener) {
    this.mContext = context;
    this.mListener = listener;
  }

  public void updateData(ImageDataHolder data) {
    this.mData = data;
    notifyDataSetChanged();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    WindowManager wm = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);

    ImageItemViewHolder vh = new ImageItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_grid_view_item, parent, false));
    return vh;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    final ImageItemViewHolder vh = (ImageItemViewHolder) holder;
    final ImageListItem item = getImageItemAt(position);

    vh.mTitle.setText(item.getDate());

    SimpleDraweeView image = vh.mThumbnail;
    Uri uri = Uri.fromFile(new File(item.getImageUrl()));
    int size = (int) this.mContext.getResources().getDimension(R.dimen.grid_item_width);
    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
        .setResizeOptions(new ResizeOptions(size, size))
        .setLocalThumbnailPreviewsEnabled(true)
        .setImageType(ImageRequest.ImageType.SMALL)
        .build();

    PipelineDraweeController controller = (PipelineDraweeController)
        Fresco.newDraweeControllerBuilder()
            .setImageRequest(request)
            .setOldController(image.getController())
            .build();
    image.setController(controller);

  }

  @Override
  public int getItemCount() {
    if(mData == null) return 0;

    return mData.getList() == null ? 0 : mData.getList().size();
  }

  //Get item by position in adapter.
  private ImageListItem getImageItemAt(int positionInAdapter) {
    return mData.getList().get(positionInAdapter);
  }
}
