package it.unimib.socialmesh.ui.main.profile;

import static it.unimib.socialmesh.util.Constants.FIREBASE_PROFILE_PIC_NAME;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.InterestsAdapter;
import it.unimib.socialmesh.adapter.PhotosAdapter;
import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.ui.main.profile.DescriptionActivity;
import it.unimib.socialmesh.ui.main.profile.EditPreferencesActivity;
import it.unimib.socialmesh.ui.welcome.UserViewModel;
import it.unimib.socialmesh.ui.welcome.UserViewModelFactory;
import it.unimib.socialmesh.util.FireBaseUtil;
import it.unimib.socialmesh.util.ServiceLocator;

public class SettingsActivity extends AppCompatActivity {
    ImageView profilePic;
    Uri selectedImageUri;
    ActivityResultLauncher<Intent> imagePickLauncher;
    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    private InterestsAdapter interestsAdapter;
    private RecyclerView recyclerView2;
    private PhotosAdapter photosAdapter;
    private boolean isEditingProfilePic = false;
    private List<String> interestsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(this.getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        recyclerView2 = findViewById(R.id.recyclerPhotos);
        setContentView(R.layout.activity_settings);
        String currentUserId = FireBaseUtil.currentUserId();
        recyclerView2 = findViewById(R.id.recyclerPhotos);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        userViewModel.getPhotosUrlLiveData().observe(this, photoUrls -> {
            if (recyclerView2 != null) {
                photosAdapter = new PhotosAdapter(this, photoUrls);
                recyclerView2.setAdapter(photosAdapter);
                photosAdapter.notifyDataSetChanged();
            } else {
                Log.e("SETTINGS", "recyclerView2 è null");
            }
        });
        loadProfileImage();
        updateDescription(currentUserId);
        updateInterests(currentUserId);
        userViewModel.retrieveUserImages(currentUserId);
        profilePic = findViewById(R.id.settings_profile_image);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            updateProfilePic(selectedImageUri);
                            if (selectedImageUri != null) {
                                uploadProfilePic(selectedImageUri);
                            }
                        }
                    }
                }
        );
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            uploadPhoto(selectedImageUri);
                            }
                    }

                });
        profilePic.setOnClickListener((v) -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(intent -> {
                        imagePickLauncher.launch(intent);
                        return null;
                    });
        });
        Button closeSettingsButton = findViewById(R.id.close_settings_btn);
        closeSettingsButton.setOnClickListener(view -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        Button editDescriptionButton = findViewById(R.id.button_edit_description);
        editDescriptionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, DescriptionActivity.class);
            startActivity(intent);

        });
        Button editInterestsButton = findViewById(R.id.button_edit_interests);
        editInterestsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditPreferencesActivity.class);
            startActivity(intent);

        });

        Button editPhotosButton = findViewById(R.id.button_edit_photos);
        editPhotosButton.setOnClickListener(v -> {
            isEditingProfilePic = false;
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(intent -> {
                        imagePickerLauncher.launch(intent);
                        return null;     });
        });
    }

    private void uploadPhoto(Uri imageUri) {
        String currentUserId = FireBaseUtil.currentUserId();
        StorageReference userPhotosRef = FireBaseUtil.getCurrentProfilePicStorageRef(currentUserId);

        if (imageUri != null) {
            StorageReference photoRef = userPhotosRef.child(imageUri.getLastPathSegment());

            userViewModel.uploadImage(imageUri, photoRef);
        }
    }
    private void updateInterests(String currentUserId) {
        recyclerView = findViewById(R.id.recyclerInterests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        interestsAdapter = new InterestsAdapter(interestsList);
        recyclerView.setAdapter(interestsAdapter);
        userViewModel.updateInterests(currentUserId);
        userViewModel.getUserInterestsLiveData().observe(this, interestsList -> {
            interestsAdapter.setInterestsList(interestsList);
            interestsAdapter.notifyDataSetChanged();
        });
    }


    private void updateDescription(String currentUserId) {
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
       TextView descriptionTextView = findViewById(R.id.descriptionTextView);

        userViewModel.fetchUserDescription(currentUserId);

        userViewModel.getUserDescriptionLiveData().observe(this, description -> {
            if (description != null) {
                descriptionTextView.setText(description);
            }
        });
    }


    private void updateProfilePic(Uri selectedImageUri) {
         if (selectedImageUri != null  && selectedImageUri != null) {
             CircularProgressDrawable drawable = new CircularProgressDrawable(this);
             drawable.setColorSchemeColors(R.color.md_theme_light_primary, R.color.md_theme_dark_primary, R.color.md_theme_dark_inversePrimary);
             drawable.setCenterRadius(30f);
             drawable.setStrokeWidth(5f);
             drawable.start();

             Glide.with(this)
                     .load(selectedImageUri)
                     .apply(RequestOptions.circleCropTransform())
                     .placeholder(drawable)
                     .error(drawable)
                     .into(profilePic);
         }
    }

    private void uploadProfilePic(Uri selectedImageUri) {
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        String currentUserId = FireBaseUtil.currentUserId();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userRef = FireBaseUtil.getCurrentProfilePicStorageRef(currentUserId).child(FIREBASE_PROFILE_PIC_NAME);
        userViewModel.uploadImage(selectedImageUri, userRef);

        userViewModel.getImageUrlLiveData().observe(this, imageUrl -> {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                CircularProgressDrawable drawable = new CircularProgressDrawable(this);
                drawable.setColorSchemeColors(R.color.md_theme_light_primary, R.color.md_theme_dark_primary, R.color.md_theme_dark_inversePrimary);
                drawable.setCenterRadius(30f);
                drawable.setStrokeWidth(5f);
                drawable.start();

                Glide.with(this)
                        .load(imageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(drawable)
                        .error(drawable)
                        .into(profilePic);
            }
        });
    }
    private void loadProfileImage() {
        String currentUserId = FireBaseUtil.currentUserId();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userRef = FireBaseUtil.getCurrentProfilePicStorageRef(currentUserId).child(FIREBASE_PROFILE_PIC_NAME);

        userRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageURL = uri.toString();

            if (imageURL != null && !imageURL.isEmpty()) {
                if (!isDestroyed()) {
                    CircularProgressDrawable drawable = new CircularProgressDrawable(this);
                    drawable.setColorSchemeColors(R.color.md_theme_light_primary, R.color.md_theme_dark_primary, R.color.md_theme_dark_inversePrimary);
                    drawable.setCenterRadius(30f);
                    drawable.setStrokeWidth(5f);
                    drawable.start();
                    Glide.with(this)
                            .load(imageURL)
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(drawable)
                            .error(drawable)
                            .into(profilePic);
                }
            } else {

                if (!isDestroyed()) {
                    CircularProgressDrawable drawable = new CircularProgressDrawable(this);
                    drawable.setColorSchemeColors(R.color.md_theme_light_primary, R.color.md_theme_dark_primary, R.color.md_theme_dark_inversePrimary);
                    drawable.setCenterRadius(30f);
                    drawable.setStrokeWidth(5f);
                    drawable.start();
                    Glide.with(this)
                            .load(imageURL)
                            .placeholder(drawable)
                            .error(drawable)
                            .apply(RequestOptions.circleCropTransform())
                            .into(profilePic);
                }
            }
        }).addOnFailureListener(exception -> {
            Log.e("Settings", "Errore durante il caricamento dell'immagine del profilo: " + exception.getMessage());
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        String currentUserId = FireBaseUtil.currentUserId();
        updateDescription(currentUserId);
        updateInterests(currentUserId);
        userViewModel.getPhotosUrlLiveData().observe(this, photoUrls -> {
            recyclerView2.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            photosAdapter = new PhotosAdapter(this, photoUrls);
            recyclerView2.setAdapter(photosAdapter);
        });

    }
}
