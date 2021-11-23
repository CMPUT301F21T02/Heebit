package com.example.spacejuice.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.spacejuice.R;

import java.io.IOException;
import java.security.spec.MGF1ParameterSpec;
import java.util.List;
import java.util.Locale;

public class gpsActivity extends AppCompatActivity {
    private TextView province;
    private TextView city;
    private Button addButton;
    private Button locateButton;
    private Button cancelButton;
    private LocationManager lm;
    private Location location;
    private double latitude;
    private double longitude;
    private String cityName;
    private String stateName;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        province = findViewById(R.id.gpsProvince);
        city = findViewById(R.id.gpsCity);
        addButton = findViewById(R.id.add_gps_b);
        locateButton = findViewById(R.id.LocateB);
        cancelButton = findViewById(R.id.cancel_gps);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        province.setText(getString(R.string.none));
        city.setText(getString(R.string.none));
        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGpsAble(lm)) {
                    Toast.makeText(gpsActivity.this, "Please open your GPS!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (ActivityCompat.checkSelfPermission(gpsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(gpsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(gpsActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                    else {
                        location = lm.getLastKnownLocation(lm.PASSIVE_PROVIDER);
                        if (location != null){
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            Geocoder geocoder = new Geocoder(gpsActivity.this, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            cityName = addresses.get(0).getLocality();
                            stateName = addresses.get(0).getAdminArea();
                            province.setText(stateName);
                            city.setText(cityName);
                        }
                        else{
                            Toast.makeText(gpsActivity.this, "Can't get location data!", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cityName.isEmpty()){
                    Intent intent = new Intent();
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("latitude", latitude);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });




    }
    private boolean isGpsAble(LocationManager lm) {
        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ? true : false;
    }

}
