package it.unimib.socialmesh.ui.main.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.databinding.FragmentProfileBinding;
import it.unimib.socialmesh.ui.welcome.UserViewModel;
import it.unimib.socialmesh.ui.welcome.UserViewModelFactory;
import it.unimib.socialmesh.util.FireBaseUtil;
import it.unimib.socialmesh.util.LocaleManager;
import it.unimib.socialmesh.util.ServiceLocator;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    ImageView profile_image_view;
    TextView tvProfile;
    private UserViewModel userViewModel;
    private FragmentProfileBinding fragmentProfileBinding;
    private LocationRequest locationRequest;
    private Double latitude, longitude;
    private int currentTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

    public ProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        userPositionRequest(requireContext());

    }
    ActivityResultLauncher<Intent> settingsLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {

            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.hasExtra("image_url")) {
                    String imageUrl = data.getStringExtra("image_url");
                    CircularProgressDrawable drawable = new CircularProgressDrawable(getContext());
                    drawable.setColorSchemeColors(R.color.md_theme_light_primary, R.color.md_theme_dark_primary, R.color.md_theme_dark_inversePrimary);
                    drawable.setCenterRadius(30f);
                    drawable.setStrokeWidth(5f);
                    drawable.start();
                    Glide.with(this)
                            .load(imageUrl)
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(drawable)
                            .error(drawable)
                            .into(profile_image_view);
                }
            }
        }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        tvProfile = fragmentProfileBinding.tvAddress;
        profile_image_view = fragmentProfileBinding.profileImageView;
        ImageView country = fragmentProfileBinding.country;
        String userId = FireBaseUtil.currentUserId();
        userViewModel.obtainUserData(userId);
        country.setOnClickListener(view -> {
            LanguageDialogFragment dialog = new LanguageDialogFragment();
            dialog.show(getParentFragmentManager(), "language_dialog");

        });
        updateCountryFlag();
        SwitchMaterial themeSwitch = fragmentProfileBinding.switchDarkMode;
        themeSwitch.setChecked(currentTheme != AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                currentTheme = AppCompatDelegate.MODE_NIGHT_YES;
                Snackbar.make(requireView(), R.string.dark_mode, Snackbar.LENGTH_SHORT).show();
            } else {
                currentTheme = AppCompatDelegate.MODE_NIGHT_NO;
            }
            AppCompatDelegate.setDefaultNightMode(currentTheme);
        });
        fragmentProfileBinding.buttonSettings.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), SettingsActivity.class);
            settingsLauncher.launch(intent);
        });

        userViewModel.getProfileFullName().observe(getViewLifecycleOwner(), name -> {
            fragmentProfileBinding.userName.setText(name);
        });

        userViewModel.getEmailLiveData().observe(getViewLifecycleOwner(), email -> {
            fragmentProfileBinding.userEmail.setText(email);
        });

        userViewModel.getProfileBirthDate().observe(getViewLifecycleOwner(), birthDate -> {
            String age = "," + calculateAge(birthDate);
            fragmentProfileBinding.tvAge.setText(age);
        });
        return fragmentProfileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadProfileImage();
        fragmentProfileBinding.btnContattaci.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_helpFragment);
        });
        fragmentProfileBinding.buttonLogout.setOnClickListener(v -> {
            userViewModel.logout();
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_welcomeActivity);
        });

}


    private void loadProfileImage() {
        String currentUserId =FireBaseUtil.currentUserId();
        userViewModel.getProfileImageUrl(currentUserId).observe(getViewLifecycleOwner(), imageUrl -> {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                CircularProgressDrawable drawable = new CircularProgressDrawable(getContext());
                drawable.setColorSchemeColors(R.color.md_theme_light_primary, R.color.md_theme_dark_primary, R.color.md_theme_dark_inversePrimary);
                drawable.setCenterRadius(30f);
                drawable.setStrokeWidth(5f);
                drawable.start();
                Glide.with(this)
                        .load(imageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(drawable)
                        .error(drawable)
                        .into(profile_image_view);
            } else {
                Glide.with(this)
                        .load(com.facebook.R.drawable.com_facebook_profile_picture_blank_portrait) // Immagine di default
                        .apply(RequestOptions.circleCropTransform())
                        .into(profile_image_view);
            }
        });
    }
    public void userPositionRequest(Context context) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //priorità della precisione della posizione
        locationRequest.setInterval(5000); //intervallo di aggiornamento
        locationRequest.setFastestInterval(2000); //altro intervallo di aggiornamento
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //i permessi sono stati accettati dall'utente
                Log.d(TAG, "permessi accettati");
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                if (isGPSEnabled()) {
                    //se il gps è on e i permessi sono stati accettati dall'utente
                    //si aggiorna la posizione tramite getFusedLocationProviderClient
                    Log.d(TAG, "GPS attivo");
                    startLocationUpdates(context);
                } else {
                    //altrimenti si richiede all'utente di attivarlo
                    turnOnGPS();
                }
            } else {// i permessi non sono stati accettati dall'utente
                //si richiede di accettare i permessi
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                Log.d(TAG, "Richiesta mandata");
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permessi accettati, richiamo startLocationUpdates");
                    startLocationUpdates(context);
                }
            }
        }

    }


    private void turnOnGPS() {

        //definisco la richiesta e inserisco di richiedere sempre il permesso di attivare il GPS nel caso in cui è disattivato
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        //controlliamo le impostazioni del GPS e se è attivo
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(requireContext()).checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {

            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                Toast.makeText(requireContext(), "GPS is already tured on", Toast.LENGTH_SHORT).show();

            } catch (ApiException e) {

                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //se il GPS è gia attivo viene richiesto di modificare le impostazioni di esso

                        try {
                            //viene richiesto all'utente di modificare le impostazioni
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(requireActivity(), 1);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Il dispositivo non ha una localizzazione
                        break;
                }
            }
        });

    }

    private void startLocationUpdates(Context context) {
        if (isAdded()) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
                LocationCallback locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        Log.d(TAG, "prima del if");
                        if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                            int index = locationResult.getLocations().size() - 1;
                            latitude = locationResult.getLocations().get(index).getLatitude();
                            longitude = locationResult.getLocations().get(index).getLongitude();
                            Log.d(TAG, "latitudine: " + latitude.toString() + " longitudine: " + longitude.toString());
                            fusedLocationClient.removeLocationUpdates(this); // Rimuovo l'ascoltatore

                            if (tvProfile != null && isAdded()) {
                                String location = getCountryAndCityFromLocation(latitude, longitude);
                                tvProfile.setText(location);
                            }
                        }
                    }
                };
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            }
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            if (isGPSEnabled()) {
                startLocationUpdates(requireContext());
            } else {
                turnOnGPS();
            }
        } else {
            Snackbar.make(requireView(), "I permessi sono necessari per ottenere la posizione", Snackbar.LENGTH_LONG)
                    .setAction("OK", v -> {}).show();
        }
    });
    private boolean isGPSEnabled() {
        //controllo se il GPS è on
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    private String getCountryAndCityFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String city = address.getLocality();
                String country = address.getCountryName();
                return city + ", " + country;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    private int calculateAge(String dateOfBirth) {
        int age = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date birthDate = sdf.parse(dateOfBirth);
            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();

            dob.setTime(birthDate);

            int yearDiff = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            int currentMonth = today.get(Calendar.MONTH) + 1;
            int birthMonth = dob.get(Calendar.MONTH) + 1;
            int currentDay = today.get(Calendar.DAY_OF_MONTH);
            int birthDay = dob.get(Calendar.DAY_OF_MONTH);

            if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
                age = yearDiff - 1;
            } else {
                age = yearDiff;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return age;
    }

        // ... Altri metodi del fragment

        private void updateCountryFlag() {

                ImageView countryFlag = fragmentProfileBinding.country;

                // Ora puoi impostare l'immagine in base alla lingua corrente
                if (countryFlag != null) {
                    // Ottenere la lingua corrente dal LocaleManager
                    LocaleManager localeManager = new LocaleManager(getContext());
                    String currentLanguage = localeManager.getCurrentLanguage();

                    // Impostare l'immagine in base alla lingua corrente
                    switch (currentLanguage) {
                        case "it":
                            countryFlag.setImageResource(R.drawable.italy_flag_icon);
                            break;
                        case "en":
                            countryFlag.setImageResource(R.drawable.ukflag);
                            break;
                        case "es":
                            countryFlag.setImageResource(R.drawable.spain_country_flag);
                            break;
                        case "zh":
                            countryFlag.setImageResource(R.drawable.china_flag_icon);
                            break;
                        // Aggiungi altri casi per le lingue supportate
                        default:
                            // Usa un'immagine di default o gestisci il caso secondo le tue esigenze
                            break;
                    }
                }
            }



    @Override
    public void onResume() {
        super.onResume();
        loadProfileImage();
        updateCountryFlag();
    }
}
