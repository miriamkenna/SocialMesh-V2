package it.unimib.socialmesh.ui.welcome;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.databinding.SignupFragmentBinding;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment {

    private TextInputEditText emailEditText;
    private Button resetButton,backToLoginButton;
    private FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        backToLoginButton = view.findViewById(R.id.btn_back_to_login);
        emailEditText = view.findViewById(R.id.testo_email);
        resetButton = view.findViewById(R.id.buttonRecupera);

        auth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getActivity(), "Inserisci il tuo indirizzo email!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Invia l'email di reset password
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),
                                    "Abbiamo inviato un'email per il reset della password. Controlla la tua casella di posta.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Errore nell'invio dell'email di reset della password. Verifica l'indirizzo email inserito.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        backToLoginButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
        return view;
    }
}
