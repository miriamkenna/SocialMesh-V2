package it.unimib.socialmesh.ui.main.eventsmap;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.Event;

public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = MapsMarkerActivity.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;

    private PlacesClient placesClient;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 8;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra("EVENT_LIST")) {
             eventList = getIntent().getParcelableArrayListExtra("EVENT_LIST");
        }

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_maps);
        Places.initialize(getApplicationContext(),"AIzaSyB3WFIwU3fCEYmeuX0APxNQibeo6G4Kuww");
        placesClient = Places.createClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button switchToList = findViewById(R.id.list_view);
        switchToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace();

        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        updateEventMarkers(eventList);
        map.setOnMarkerClickListener(marker -> {
            try {
                Event clickedEvent = (Event) marker.getTag();
                if (clickedEvent != null) {
                    runOnUiThread(() -> {
                        Log.d(TAG, clickedEvent.getName());
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("EVENT_KEY", clickedEvent);
                        Intent intent = new Intent(MapsMarkerActivity.this, EventMapDetailsActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    });


                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        map.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }


    private void showCurrentPlace() {
        if (map == null) {
            return;
        }

        if (locationPermissionGranted) {
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);
            @SuppressWarnings("MissingPermission") final
            Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener (task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    FindCurrentPlaceResponse likelyPlaces = task.getResult();
                    int count;
                    if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                        count = likelyPlaces.getPlaceLikelihoods().size();
                    } else {
                        count = M_MAX_ENTRIES;
                    }

                    int i = 0;
                    likelyPlaceNames = new String[count];
                    likelyPlaceAddresses = new String[count];
                    likelyPlaceAttributions = new List[count];
                    likelyPlaceLatLngs = new LatLng[count];

                    for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                        likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                        likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                        likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                .getAttributions();
                        likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                        i++;
                        if (i > (count - 1)) {
                            break;
                        }
                    }
                    MapsMarkerActivity.this.openPlacesDialog();
                }
                else {
                    Log.e(TAG, "Exception: %s", task.getException());
                }
            });
        } else {
            Log.i(TAG, "The user did not grant location permission.");
            map.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            getLocationPermission();
        }
    }

    private void openPlacesDialog() {
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            LatLng markerLatLng = likelyPlaceLatLngs[which];
            String markerSnippet = likelyPlaceAddresses[which];
            if (likelyPlaceAttributions[which] != null) {
                markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
            }
            map.addMarker(new MarkerOptions()
                    .title(likelyPlaceNames[which])
                    .position(markerLatLng)
                    .snippet(markerSnippet));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                    DEFAULT_ZOOM));
        };

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(likelyPlaceNames, listener)
                .show();
    }


    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private void updateEventMarkers(List<Event> eventList) {
        if (map != null && eventList != null && !eventList.isEmpty()) {

            for (int i = 0; i < eventList.size(); i++) {
                Log.d(TAG, "Aggiunta del marker per l'evento: " + eventList.get(i).getName());
                LatLng eventLatLng = new LatLng(eventList.get(i).getVenueLatitude(), eventList.get(i).getVenueLongitude());
                String eventSnippet = eventList.get(i).getName();
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(eventLatLng)
                        .title(eventList.get(i).getName())
                        .snippet(eventSnippet)); // Usa il nome dell'evento come titolo
                marker.setTag(eventList.get(i));
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Event event : eventList) {
                builder.include(new LatLng(event.getVenueLatitude(), event.getVenueLongitude()));
            }
            LatLngBounds bounds = builder.build();
            int paddingInPixels = 50;
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, paddingInPixels));
        }
    }

}