package it.unimib.socialmesh.adapter;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import java.util.List;

import it.unimib.socialmesh.R;

public class PhotosViewPagerAdapter extends RecyclerView.Adapter<PhotosViewPagerAdapter.PhotoViewHolder> {

    private Context context;
    private List<Uri> photoUris;

    public PhotosViewPagerAdapter(Context context, List<Uri> photoUris) {
        this.context = context;
        this.photoUris = photoUris;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        view.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Uri imageUri = photoUris.get(position);
        CircularProgressDrawable drawable = new CircularProgressDrawable(context.getApplicationContext());
        drawable.setColorSchemeColors(R.color.md_theme_light_primary, R.color.md_theme_dark_primary, R.color.md_theme_dark_inversePrimary);
        drawable.setCenterRadius(30f);
        drawable.setStrokeWidth(5f);
        drawable.start();
        Glide.with(context)
                .load(imageUri)
                .placeholder(drawable)
                .error(drawable)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return photoUris.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagePhoto);
        }
    }
}
