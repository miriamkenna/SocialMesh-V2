package it.unimib.socialmesh.ui.welcome;

import static androidx.core.content.ContextCompat.getSystemService;

import static it.unimib.socialmesh.util.Constants.LIMIT_AGE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.data.repository.user.UserResponseCallback;
import it.unimib.socialmesh.databinding.FragmentProfileDetailsBinding;
import it.unimib.socialmesh.model.User;

public class  ProfileDetailsFragment extends Fragment {
    private UserViewModel userViewModel;

    private FragmentProfileDetailsBinding fragmentProfileDetailsBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentProfileDetailsBinding = FragmentProfileDetailsBinding.inflate(inflater, container, false);
        return fragmentProfileDetailsBinding.getRoot();
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        Bundle bundle = getArguments();
        if (bundle != null) {

            User user = bundle.getParcelable("user");

            if(user !=null) {

                fragmentProfileDetailsBinding.testoDatanasc.setOnClickListener(v -> {

                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            this.getContext(),
                            R.style.ThemeOverlay_App_Dialog,
                            (view2, year1, monthOfYear, dayOfMonth) -> {
                                fragmentProfileDetailsBinding.testoDatanasc.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                            },
                            year, month, day);

                    //Chiudo la tastiera
                    datePickerDialog.getDatePicker().setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

                    datePickerDialog.show();
                });

                fragmentProfileDetailsBinding.buttonRegister.setOnClickListener(v -> {

                    fragmentProfileDetailsBinding.progressBar.setVisibility(ProgressBar.VISIBLE);

                    String fullName = fragmentProfileDetailsBinding.fullName.getEditText().getText().toString().trim();
                    String birthDate = fragmentProfileDetailsBinding.datanasc.getEditText().getText().toString().trim();

                    if(!fullName.isEmpty() && !birthDate.isEmpty() && ageCheck(birthDate)) {

                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference usersRef = database.getReference().child("users");
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                        usersRef.child(firebaseUser.getUid()).child("name").setValue(fullName).addOnCompleteListener(setValueTask -> {
                            if (setValueTask.isSuccessful()) {
                                Log.d("OK","PESO");
                            } else {
                                Log.d("NO","NONPESO");
                            }
                        });
                        usersRef.child(firebaseUser.getUid()).child("data_di_nascita").setValue(birthDate).addOnCompleteListener(setValueTask -> {
                            if (setValueTask.isSuccessful()) {
                                Log.d("OK","PESO");
                            } else {
                                Log.d("NO","NONPESO");
                            }
                        });

                        fragmentProfileDetailsBinding.progressBar.setVisibility(ProgressBar.GONE);
                        Navigation.findNavController(requireView()).navigate(R.id.action_profileDetailsFragment_to_preferencesFragment);
                    } else {
                        fragmentProfileDetailsBinding.progressBar.setVisibility(ProgressBar.GONE);
                        if (fullName.isEmpty())
                            fragmentProfileDetailsBinding.fullName.setError("Nome non valido");
                        else if(birthDate.isEmpty())
                            fragmentProfileDetailsBinding.datanasc.setError("Data nascita non valida");
                        else if(!ageCheck(birthDate))
                            fragmentProfileDetailsBinding.datanasc.setError(String.format("Devi avere almeno %d anni per usare l'app", LIMIT_AGE));
                    }


                });

                userViewModel.getProfileFullName().observe(getViewLifecycleOwner(), fullName -> {
                    fragmentProfileDetailsBinding.fullName.getEditText().setText(fullName);
                });

                userViewModel.getProfileBirthDate().observe(getViewLifecycleOwner(), birthDate -> {
                    fragmentProfileDetailsBinding.datanasc.getEditText().setText(birthDate);
                });

            }
        }
    }

    private static boolean ageCheck(String birthDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date birthDate = sdf.parse(birthDateString);

            Calendar currentDate = Calendar.getInstance();

            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(birthDate);

            int age = currentDate.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

            return age > LIMIT_AGE;

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}