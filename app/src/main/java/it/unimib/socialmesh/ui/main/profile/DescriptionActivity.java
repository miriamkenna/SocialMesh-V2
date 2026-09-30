package it.unimib.socialmesh.ui.main.profile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import it.unimib.socialmesh.R;
import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.ui.welcome.UserViewModel;
import it.unimib.socialmesh.ui.welcome.UserViewModelFactory;
import it.unimib.socialmesh.util.ServiceLocator;

import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class DescriptionActivity extends AppCompatActivity {
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_description);
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        Button confirmButton = findViewById(R.id.button_confirm_description);
        confirmButton.setOnClickListener(v -> {
            saveDescription();
        });

        ImageButton backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void saveDescription() {
        EditText descriptionEditText = findViewById(R.id.descriptionEditText);
        String description = descriptionEditText.getText().toString().trim();
        userViewModel.saveDescription(description);
        Snackbar.make(findViewById(android.R.id.content), R.string.user_description_message, Snackbar.LENGTH_SHORT).show();
        finish();
    }
}
