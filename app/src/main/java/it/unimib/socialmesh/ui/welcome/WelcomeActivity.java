package it.unimib.socialmesh.ui.welcome;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseUser;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.ui.main.HomeActivity;
import it.unimib.socialmesh.util.FireBaseUtil;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FirebaseUser currentUser = FireBaseUtil.currentUser();
        if (currentUser!=null) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.container_fragment);
        NavController navController = navHostFragment.getNavController();
    }
}
