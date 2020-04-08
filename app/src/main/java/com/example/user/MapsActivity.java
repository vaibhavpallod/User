package com.example.user;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.FragmentActivity;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Firebase mRef;
    LatLng latLng;
    Marker marker;
    String string;
    FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
        @Override
        protected void onCreate (Bundle savedInstanceState){

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);
            Firebase.setAndroidContext(getApplicationContext());
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mRef = new Firebase("https://location-detector-e9936.firebaseio.com/");

            Firebase.setAndroidContext(this);
            mAuth = FirebaseAuth.getInstance();

        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        string = getIntent().getExtras().getString("Value");

        mRef.addValueEventListener(new ValueEventListener() {
            Double Latitude;
            Double Longitude;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (marker != null) {
                    marker.remove();
                }

                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {

                    String location = locationSnapshot.getKey();

                    //Toast.makeText(getApplicationContext(), locationSnapshot.getKey()+ " " , Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "onDataChange: "+location );
                    if (location.equals(string))
                    {

                       // Log.v(TAG, "If Statement : "+locationSnapshot.child("Latitude").getValue() );
                        try {
                            latLng = new LatLng((Double)locationSnapshot.child("Latitude").getValue(),(Double)locationSnapshot.child("Longitude").getValue());
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Bus Current Position").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18), 2000, null);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(getApplicationContext(), Latitude + " " +
                                Longitude, Toast.LENGTH_SHORT).show();

                    }
                }
            }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Toast.makeText(getApplicationContext(), "Failed to load coordinates", Toast.LENGTH_SHORT).show();
                }
            });
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

}
