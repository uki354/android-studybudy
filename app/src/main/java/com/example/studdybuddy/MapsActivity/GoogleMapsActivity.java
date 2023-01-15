package com.example.studdybuddy.MapsActivity;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.example.studdybuddy.R;
import com.example.studdybuddy.User;
import com.example.studdybuddy.databinding.ActivityGoogleMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapsBinding binding;
    private User studyUser;
    private User appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String works1 = extras.getString("works");
        System.out.println(works1);
        User studyUser1 = extras.getParcelable("studyUser");

//
        this.studyUser = studyUser1;
//        this.appUser = appUser;

        binding = ActivityGoogleMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    // show app user loc at map, add button to draw route between them
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(20);
        LatLng userLoc = new LatLng(Double.parseDouble(studyUser.getLat()), Double.parseDouble(studyUser.getLng()));

        mMap.addMarker(new MarkerOptions().position(userLoc).title(studyUser.getName() + studyUser.getLastname()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLoc));
    }
}