package it.unimib.socialmesh.adapter;


import static it.unimib.socialmesh.util.Constants.FIREBASE_PICTURES_COLLECTION;
import static it.unimib.socialmesh.util.Constants.FIREBASE_PROFILE_PIC_NAME;

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

import java.util.List;

import it.unimib.socialmesh.R;



    public class PartecipantsAdapter extends RecyclerView.Adapter<PartecipantsAdapter.UserViewHolder> {

        private List<String> userIdList;
        private List<String> userNameList;

        private OnItemClickListener clickListener;

        public interface OnItemClickListener {
            void onUserClick(String userId);
        }

        public PartecipantsAdapter(List<String> userIdList, List<String> userNameList, OnItemClickListener clickListener) {
            this.userIdList = userIdList;
            this.userNameList = userNameList;
            this.clickListener = clickListener;
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            if (userIdList != null && userNameList != null && position < userIdList.size() && position < userNameList.size()) {
                String userName = userNameList.get(position);
                holder.bind(userName, clickListener, userIdList.get(position));
            } else {
                Log.e("PartecipantsAdapter", "userIdList or userNameList is null or position is out of bounds");
            }
        }

        @Override
        public int getItemCount() {
            return userIdList.size();
        }

        static class UserViewHolder extends RecyclerView.ViewHolder {
            TextView userNameTextView;
            ImageView profile_pic;

            UserViewHolder(@NonNull View itemView) {
                super(itemView);
                userNameTextView = itemView.findViewById(R.id.textview_username);
                profile_pic = itemView.findViewById(R.id.profile_image);
            }

            void bind(String userName, OnItemClickListener clickListener, String userId) {
                userNameTextView.setText(userName);
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
                itemView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onUserClick(userId);
                    }
                });
            }
        }
    }

