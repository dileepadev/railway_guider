/*
   --------------------------------------
      Developed by
      Dileepa Bandara
      https://dileepabandara.github.io
      contact.dileepabandara@gmail.com
      ©dileepabandara.dev
      2020
   --------------------------------------
*/

package dev.dileepabandara.railwayguider.User;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    TextView gpsNotTurnON, locationDetails;
    TextView locationAddress, locationCity, locationProvince, locationLatitude, locationLongitude;
    ProgressBar mapProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        //Request permissions from user
        ActivityCompat.requestPermissions(MapsActivity2.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, PackageManager.PERMISSION_GRANTED);

        Toast.makeText(this, "Please turn on your GPS for this service", Toast.LENGTH_SHORT).show();
        mapProgressBar = findViewById(R.id.mapProgressBar);

        Intent intent = getIntent();
        final String route = intent.getStringExtra("ticketTrainNo");
        Toast.makeText(this, "" + route, Toast.LENGTH_SHORT).show();

        //Back Button
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Home Button
        ImageView imgHome = findViewById(R.id.imgHome);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                finishAffinity();
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Map marker
        int height = 120;
        int width = 120;
        BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_train_location);
        Bitmap b = bitmapDraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


        Intent intent = getIntent();
        final String route = intent.getStringExtra("ticketTrainNo");

        //Get Location From Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("trains").child(route);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Get location from database
                String latitudeS = dataSnapshot.child("latitude").getValue(String.class);
                String longitudeS = dataSnapshot.child("longitude").getValue(String.class);

                try {
                    double databaseLatitude = Double.parseDouble(latitudeS);
                    double databaseLongitude = Double.parseDouble(longitudeS);

                    //Clear Latest Place
                    mMap.clear();

                    // Add a marker and move the camera
                    final LatLng trainLocation = new LatLng(databaseLatitude, databaseLongitude);
                    Marker marker = mMap.addMarker(new MarkerOptions().position(trainLocation).title("Your Train").snippet(route).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trainLocation, 15));
                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    mMap.setMaxZoomPreference(70.0f);
                    mMap.setMinZoomPreference(12.0f);
                    marker.showInfoWindow();

                    final String latitude = String.valueOf(databaseLatitude);
                    final String longitude = String.valueOf(databaseLongitude);

                    //Convert location to real address
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(MapsActivity2.this, Locale.getDefault());
                    String address;
                    String city;
                    String province;
                    String country;
                    String postalCode = "N/A";
                    String featureName;

                    try {
                        addresses = geocoder.getFromLocation(databaseLatitude, databaseLongitude, 1);

                        address = addresses.get(0).getAddressLine(0);
                        city = addresses.get(0).getLocality();
                        province = addresses.get(0).getAdminArea();
                        country = addresses.get(0).getCountryName();
                        postalCode = addresses.get(0).getPostalCode();
                        featureName = addresses.get(0).getFeatureName();

                        locationAddress = findViewById(R.id.locationAddress);
                        locationCity = findViewById(R.id.locationCity);
                        locationProvince = findViewById(R.id.locationProvince);
                        locationLatitude = findViewById(R.id.locationLatitude);
                        locationLongitude = findViewById(R.id.locationLongitude);

                        LinearLayout locationDetails = findViewById(R.id.locationDetails);
                        locationDetails.setVisibility(View.VISIBLE);
                        locationAddress.setText("Address - " + address);
                        locationCity.setText("City - " + city);
                        locationProvince.setText("Province - " + province);
                        locationLatitude.setText("Latitude - " + latitude);
                        locationLongitude.setText("Longitude - " + longitude);
                        mapProgressBar.setVisibility(View.GONE);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //Goto Location
                    ImageView goToLocation = findViewById(R.id.goToLocation);
                    goToLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(trainLocation, 18.0f));
                        }
                    });

                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Toast.makeText(MapsActivity2.this, "Your Train", Toast.LENGTH_SHORT).show();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(trainLocation, 18.0f));

                        }
                    });



                } catch (Exception e) {
                    Toast.makeText(MapsActivity2.this, "Your train location is not available", Toast.LENGTH_LONG).show();
                    mapProgressBar.setVisibility(View.GONE);
                    TextView trainNotAvailable = findViewById(R.id.trainNotAvailable);
                    trainNotAvailable.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MapsActivity2.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
