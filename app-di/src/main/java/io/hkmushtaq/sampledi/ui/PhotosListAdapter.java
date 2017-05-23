package io.hkmushtaq.sampledi.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hkmushtaq.sampledi.R;
import io.hkmushtaq.sampledi.models.Photo;

class PhotosListAdapter extends RecyclerView.Adapter<PhotosListAdapter.PhotoViewHolder> {

    private final Context mContext;
    private List<Photo> mPhotosList;

    PhotosListAdapter(Context context, List<Photo> photosList) {
        this.mContext = context;
        this.mPhotosList = photosList;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View photoView = LayoutInflater.from(mContext).inflate(R.layout.view_photo, parent, false);
        return new PhotoViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Photo photo = mPhotosList.get(position);

        Glide.clear(holder.mImageView);
        Glide.with(mContext)
                .load(photo.getUri())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.mImageView);

        holder.mTextView.setText(photo.getTitle());
    }

    @Override
    public int getItemCount() {
        return mPhotosList != null ? mPhotosList.size() : 0;
    }

    public void setPhotosList(List<Photo> photosList) {
        this.mPhotosList = photosList;
        notifyDataSetChanged();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view)
        ImageView mImageView;

        @BindView(R.id.text_view)
        TextView mTextView;

        public PhotoViewHolder(View photoView) {
            super(photoView);
            ButterKnife.bind(this, photoView);
        }
    }

}
