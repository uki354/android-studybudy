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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        User studyUser1 = extras.getParcelable("studyUser");

        this.studyUser = studyUser1;

        binding = ActivityGoogleMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(20);
        LatLng userLoc = new LatLng(Double.parseDouble(studyUser.getLat()), Double.parseDouble(studyUser.getLng()));

        mMap.addMarker(new MarkerOptions().position(userLoc).title(studyUser.getName() + studyUser.getLastname()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLoc));
    }
}