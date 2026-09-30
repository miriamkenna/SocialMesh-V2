package it.unimib.socialmesh.ui.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.databinding.PreferencesFragmentBinding;
import it.unimib.socialmesh.util.ServiceLocator;

public class PreferencesFragment extends Fragment implements View.OnClickListener {

    DatabaseReference userPreferencesRef;
    private PreferencesFragmentBinding preferencesFragmentBinding;
    private UserViewModel userViewModel;

    public PreferencesFragment() {}

    public static PreferencesFragment newInstance() { return new PreferencesFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        preferencesFragmentBinding = PreferencesFragmentBinding.inflate(inflater, container, false);

        //definisco i setOnClickListener(this) in modo tale che utilizzi il metodo OnClick(v) quando vengono premuti
        preferencesFragmentBinding.buttonCinema.setOnClickListener(this);
        preferencesFragmentBinding.buttonParty.setOnClickListener(this);
        preferencesFragmentBinding.buttonViaggiLowCost.setOnClickListener(this);
        preferencesFragmentBinding.buttonLgbt.setOnClickListener(this);
        preferencesFragmentBinding.buttonKaraoke.setOnClickListener(this);
        preferencesFragmentBinding.buttonBoxe.setOnClickListener(this);
        preferencesFragmentBinding.buttonNft.setOnClickListener(this);
        preferencesFragmentBinding.buttonFestival.setOnClickListener(this);
        preferencesFragmentBinding.buttonCrossfit.setOnClickListener(this);
        preferencesFragmentBinding.buttonNature.setOnClickListener(this);
        preferencesFragmentBinding.buttonBeach.setOnClickListener(this);
        preferencesFragmentBinding.buttonMotorsport.setOnClickListener(this);
        preferencesFragmentBinding.buttonInstagram.setOnClickListener(this);
        preferencesFragmentBinding.buttonTwitter.setOnClickListener(this);
        preferencesFragmentBinding.buttonFotografia.setOnClickListener(this);
        preferencesFragmentBinding.buttonPittura.setOnClickListener(this);
        preferencesFragmentBinding.buttonEscursioni.setOnClickListener(this);
        preferencesFragmentBinding.buttonGiardinaggio.setOnClickListener(this);
        preferencesFragmentBinding.buttonProgrammatore.setOnClickListener(this);
        preferencesFragmentBinding.buttonScrittura.setOnClickListener(this);
        preferencesFragmentBinding.buttonModa.setOnClickListener(this);
        preferencesFragmentBinding.buttonVegetariano.setOnClickListener(this);
        preferencesFragmentBinding.buttonVegan.setOnClickListener(this);
        preferencesFragmentBinding.buttonCarnivoro.setOnClickListener(this);
        preferencesFragmentBinding.buttonSingle.setOnClickListener(this);
        preferencesFragmentBinding.buttonMarried.setOnClickListener(this);
        preferencesFragmentBinding.buttonImpegnato.setOnClickListener(this);
        preferencesFragmentBinding.buttonCucinaItaliana.setOnClickListener(this);
        preferencesFragmentBinding.buttonBlogger.setOnClickListener(this);
        preferencesFragmentBinding.buttonBasket.setOnClickListener(this);
        preferencesFragmentBinding.buttonFumatore.setOnClickListener(this);
        preferencesFragmentBinding.buttonFacebook.setOnClickListener(this);
        preferencesFragmentBinding.buttonFreelance.setOnClickListener(this);
        preferencesFragmentBinding.buttonImprenditoria.setOnClickListener(this);
        preferencesFragmentBinding.buttonElettricista.setOnClickListener(this);
        preferencesFragmentBinding.buttonSerieTV.setOnClickListener(this);
        preferencesFragmentBinding.buttonLibri.setOnClickListener(this);
        preferencesFragmentBinding.buttonTecnologia.setOnClickListener(this);
        preferencesFragmentBinding.buttonDestinazioniEsotiche.setOnClickListener(this);
        preferencesFragmentBinding.buttonRock.setOnClickListener(this);
        preferencesFragmentBinding.buttonClassica.setOnClickListener(this);
        preferencesFragmentBinding.buttonPop.setOnClickListener(this);
        preferencesFragmentBinding.buttonRicette.setOnClickListener(this);
        preferencesFragmentBinding.buttonDolce.setOnClickListener(this);
        preferencesFragmentBinding.buttonCalcio.setOnClickListener(this);
        preferencesFragmentBinding.buttonTennis.setOnClickListener(this);
        preferencesFragmentBinding.buttonYoga.setOnClickListener(this);
        preferencesFragmentBinding.buttonPalestra.setOnClickListener(this);

        //prendo l'id dell'utente
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //definisco il campo "preferences" in firebase
        userPreferencesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("preferences");

        //change schermata
        preferencesFragmentBinding.buttonNavigate.setOnClickListener(v ->{
            Navigation.findNavController(requireView()).navigate(R.id.navigate_to_homeActivity);
        });

        return preferencesFragmentBinding.getRoot();
    }
    @Override
    public void onClick(View v) {

        //salvo il nome del button cliccato in una lista
        if (v == preferencesFragmentBinding.buttonCinema) {
            userViewModel.addPreference("Cinema");
        } else if (v == preferencesFragmentBinding.buttonParty) {
            userViewModel.addPreference("Party");
        } else if (v == preferencesFragmentBinding.buttonViaggiLowCost) {
            userViewModel.addPreference("ViaggiLowCost");
        } else if (v == preferencesFragmentBinding.buttonLgbt) {
            userViewModel.addPreference("LGBT");
        } else if (v == preferencesFragmentBinding.buttonKaraoke) {
            userViewModel.addPreference("Karaoke");
        } else if (v == preferencesFragmentBinding.buttonNft) {
            userViewModel.addPreference("NFT");
        } else if (v == preferencesFragmentBinding.buttonBoxe) {
            userViewModel.addPreference("Boxe");
        } else if (v == preferencesFragmentBinding.buttonFestival) {
            userViewModel.addPreference("Festival");
        } else if (v == preferencesFragmentBinding.buttonCrossfit) {
            userViewModel.addPreference("Crossfit");
        } else if (v == preferencesFragmentBinding.buttonNature) {
            userViewModel.addPreference("Nature");
        } else if (v == preferencesFragmentBinding.buttonBeach) {
            userViewModel.addPreference("Spiaggia");
        } else if (v == preferencesFragmentBinding.buttonMotorsport) {
            userViewModel.addPreference("Motorsport");
        } else if (v == preferencesFragmentBinding.buttonInstagram) {
            userViewModel.addPreference("Instagram");
        } else if (v == preferencesFragmentBinding.buttonTwitter) {
            userViewModel.addPreference("Twitter");
        } else if (v == preferencesFragmentBinding.buttonFotografia) {
            userViewModel.addPreference("Fotografia");
        } else if (v == preferencesFragmentBinding.buttonPittura) {
            userViewModel.addPreference("Pittura");
        } else if (v == preferencesFragmentBinding.buttonEscursioni) {
            userViewModel.addPreference("Escursioni");
        } else if (v == preferencesFragmentBinding.buttonGiardinaggio) {
            userViewModel.addPreference("Giardinaggio");
        } else if (v == preferencesFragmentBinding.buttonProgrammatore) {
            userViewModel.addPreference("Programmatore");
        } else if (v == preferencesFragmentBinding.buttonScrittura) {
            userViewModel.addPreference("Scrittura");
        } else if (v == preferencesFragmentBinding.buttonModa) {
            userViewModel.addPreference("Moda");
        } else if (v == preferencesFragmentBinding.buttonVegetariano) {
            userViewModel.addPreference("Vegetariano");
        } else if (v == preferencesFragmentBinding.buttonVegan) {
            userViewModel.addPreference("Vegan");
        } else if (v == preferencesFragmentBinding.buttonCarnivoro) {
            userViewModel.addPreference("Carnivoro");
        } else if (v == preferencesFragmentBinding.buttonSingle) {
            userViewModel.addPreference("Single");
        } else if (v == preferencesFragmentBinding.buttonMarried) {
            userViewModel.addPreference("Sposato");
        } else if (v == preferencesFragmentBinding.buttonImpegnato) {
            userViewModel.addPreference("Impegnato");
        } else if (v == preferencesFragmentBinding.buttonCucinaItaliana) {
            userViewModel.addPreference("Cucina Italiana");
        } else if (v == preferencesFragmentBinding.buttonBlogger) {
            userViewModel.addPreference("Blogger");
        } else if (v == preferencesFragmentBinding.buttonBasket) {
            userViewModel.addPreference("Basket");
        } else if (v == preferencesFragmentBinding.buttonFumatore) {
            userViewModel.addPreference("Fumatore");
        } else if (v == preferencesFragmentBinding.buttonFacebook) {
            userViewModel.addPreference("Facebook");
        } else if (v == preferencesFragmentBinding.buttonFreelance) {
            userViewModel.addPreference("Freelance");
        } else if (v == preferencesFragmentBinding.buttonImprenditoria) {
            userViewModel.addPreference("Imprenditoria");
        } else if (v == preferencesFragmentBinding.buttonElettricista) {
            userViewModel.addPreference("Elettricista");
        } else if (v == preferencesFragmentBinding.buttonSerieTV) {
            userViewModel.addPreference("Serie TV");
        } else if (v == preferencesFragmentBinding.buttonLibri) {
            userViewModel.addPreference("Libri");
        } else if (v == preferencesFragmentBinding.buttonTecnologia) {
            userViewModel.addPreference("Tecnologia");
        } else if (v == preferencesFragmentBinding.buttonDestinazioniEsotiche) {
            userViewModel.addPreference("Destinazioni Esotiche");
        } else if (v == preferencesFragmentBinding.buttonRock) {
            userViewModel.addPreference("Rock");
        } else if (v == preferencesFragmentBinding.buttonClassica) {
            userViewModel.addPreference("Classica");
        } else if (v == preferencesFragmentBinding.buttonPop) {
            userViewModel.addPreference("Pop");
        } else if (v == preferencesFragmentBinding.buttonRicette) {
            userViewModel.addPreference("Ricette");
        } else if (v == preferencesFragmentBinding.buttonDolce) {
            userViewModel.addPreference("Dolce");
        } else if (v == preferencesFragmentBinding.buttonCalcio) {
            userViewModel.addPreference("Calcio");
        } else if (v == preferencesFragmentBinding.buttonTennis) {
            userViewModel.addPreference("Tennis");
        } else if (v == preferencesFragmentBinding.buttonYoga) {
            userViewModel.addPreference("Yoga");
        } else if (v == preferencesFragmentBinding.buttonPalestra) {
            userViewModel.addPreference("Palestra");
        }
    }
}