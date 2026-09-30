package it.unimib.socialmesh.ui.welcome;

import static it.unimib.socialmesh.util.Constants.USER_COLLISION_ERROR;
import static it.unimib.socialmesh.util.Constants.WEAK_PASSWORD_ERROR;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.databinding.SignupFragmentBinding;
import it.unimib.socialmesh.model.Result;
import it.unimib.socialmesh.model.User;
import it.unimib.socialmesh.ui.main.HomeActivity;

public class SignupFragment extends Fragment {

    private UserViewModel userViewModel;

    private SignupFragmentBinding signupFragmentBinding;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        signupFragmentBinding = SignupFragmentBinding.inflate(inflater, container, false);

        signupFragmentBinding.email.setTranslationX(1000);
        signupFragmentBinding.insertPassword.setTranslationX(1000);
        signupFragmentBinding.confirmpasswordSignup.setTranslationX(1000);
        signupFragmentBinding.buttonRegister.setTranslationX(1000);


        signupFragmentBinding.email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        signupFragmentBinding.insertPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        signupFragmentBinding.confirmpasswordSignup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();
        signupFragmentBinding.buttonRegister.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1300).start();

        return signupFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signupFragmentBinding.buttonRegister.setOnClickListener(v -> {

            signupFragmentBinding.progressBar.setVisibility(ProgressBar.VISIBLE);

            String email = signupFragmentBinding.email.getEditText().getText().toString().trim();
            String password = signupFragmentBinding.insertPassword.getEditText().getText().toString().trim();
            String confirmPassword = signupFragmentBinding.confirmpasswordSignup.getEditText().getText().toString().trim();

            if (validFields(email, password, confirmPassword)) {

                //resetto gli errori
                signupFragmentBinding.email.setError(null);
                signupFragmentBinding.insertPassword.setError(null);
                signupFragmentBinding.confirmpasswordSignup.setError(null);

                if (!userViewModel.isAuthenticationError()) {
                    userViewModel.getUserMutableLiveData(email, password, false).observe(
                            getViewLifecycleOwner(), result -> {
                                if (result.isSuccess()) {
                                    User user = ((Result.UserResponseSuccess) result).getData();
                                    userViewModel.setAuthenticationError(false);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("user", user);
                                    signupFragmentBinding.progressBar.setVisibility(ProgressBar.GONE);
                                    Navigation.findNavController(view).navigate(
                                            R.id.navigate_to_detailsFragment, bundle);
                                } else {
                                    userViewModel.setAuthenticationError(true);
                                    signupFragmentBinding.progressBar.setVisibility(ProgressBar.GONE);
                                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                            getErrorMessage(((Result.Error) result).getMessage()),
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    userViewModel.getUser(email, password, false);
                }
            } else {
                signupFragmentBinding.progressBar.setVisibility(ProgressBar.GONE);
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        R.string.check_signup_data_message, Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private String getErrorMessage(String message) {
        switch(message) {
            case WEAK_PASSWORD_ERROR:
                return requireActivity().getString(R.string.error_password);
            case USER_COLLISION_ERROR:
                return requireActivity().getString(R.string.error_user_collision_message);
            default:
                return requireActivity().getString(R.string.unexpected_error);
        }
    }


    private boolean validFields(String email, String password1, String password2) {
        boolean result = true;

        if (!EmailValidator.getInstance().isValid((email))) {
            signupFragmentBinding.email.setError(getString(R.string.error_email));
            result = false;
        } else {
            signupFragmentBinding.email.setError(null);
        }  if (password1 == null) {
            signupFragmentBinding.insertPassword.setError(getString(R.string.error_password_null));
            return false;
        }

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!()_%])(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password1);

        boolean match1 = m.matches();

        // If password matches pattern return true
        if(!match1) {
            signupFragmentBinding.insertPassword.setError(getString(R.string.error_password_invalid));
            return false;
        } else {
            signupFragmentBinding.insertPassword.setError(null);
        }

        boolean match2 = password2.equals(password1);

        if(!match2) {
            signupFragmentBinding.insertPassword.setError(getString(R.string.error_password_mismatch));
            signupFragmentBinding.confirmpasswordSignup.setError(getString(R.string.error_password_mismatch));
            return false;
        } else {
            signupFragmentBinding.insertPassword.setError(null);
            signupFragmentBinding.confirmpasswordSignup.setError(null);
        }
        return result;
    }
}