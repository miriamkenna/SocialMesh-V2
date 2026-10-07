package it.unimib.socialmesh.adapter;

import static it.unimib.socialmesh.util.Constants.FIREBASE_PICTURES_COLLECTION;
import static it.unimib.socialmesh.util.Constants.FIREBASE_PROFILE_PIC_NAME;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.User;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.UserViewHolder> {

    private final Context context;
    private final ArrayList<User> matchesList;
    private final ArrayList<String> usersId;
    private final OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onUserClick(User user);
    }

    public MatchesAdapter(Context context, ArrayList<User> matchesList,ArrayList<String> usersId, OnItemClickListener clickListener) {
        this.context = context;
        this.matchesList = matchesList;
        this.usersId = usersId;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentUser = matchesList.get(position);
        holder.bind(currentUser.getName(), clickListener, usersId.get(position));
        holder.textName.setText(currentUser.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onUserClick(currentUser);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        ImageView profile_pic;

        public UserViewHolder(View itemView) {
            super(itemView);
            profile_pic = itemView.findViewById(R.id.profile_image);

            textName = itemView.findViewById(R.id.textview_username);
        }
        void bind(String userName, MatchesAdapter.OnItemClickListener clickListener, String userId){
            StorageReference userImageRef = FirebaseStorage.getInstance().getReference().child(FIREBASE_PICTURES_COLLECTION).child(userId).child(FIREBASE_PROFILE_PIC_NAME);
            userImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                CircularProgressDrawable drawable = new CircularProgressDrawable(itemView.getContext());
                drawable.setColorSchemeColors(R.color.md_theme_light_primary, R.color.md_theme_dark_primary, R.color.md_theme_dark_inversePrimary);
                drawable.setCenterRadius(30f);
                drawable.setStrokeWidth(5f);
                drawable.start();
                Glide.with(itemView.getContext())
                        .load(uri)
                        .placeholder(drawable)
                        .error(drawable)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profile_pic);
            }).addOnFailureListener(exception -> {
                Log.e("PartecipantsAdapter", "Errore durante il caricamento dell'immagine: " + exception.getMessage());
            });
        }
    }
}
