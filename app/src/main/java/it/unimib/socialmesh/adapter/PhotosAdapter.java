package it.unimib.socialmesh.adapter;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private List<Uri> photoUrls;
    private Context context;

    public PhotosAdapter(Context context, List<Uri> photoUrls) {
        this.context = context;
        this.photoUrls = photoUrls;
    }


    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Uri photoUri = photoUrls.get(position);
        Glide.with(context)
                .load(photoUri)
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return photoUrls.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagePhoto);
        }
    }
}
