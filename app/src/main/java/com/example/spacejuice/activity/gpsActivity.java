package com.example.spacejuice.activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacejuice.R;

public class gpsActivity extends AppCompatActivity {
    private TextView province;
    private TextView city;
    private Button addButton;
    private Button locateButton;
    private Button cancelButton;

    private LocationManager lm;
    private Location location;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_gps);
        province = findViewById(R.id.gpsProvince);
        city = findViewById(R.id.gpsCity);
        addButton = findViewById(R.id.add_gps_b);
        locateButton = findViewById(R.id.LocateB);
        cancelButton = findViewById(R.id.cancel_gps);

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        province.setText("None");
        city.setText("None");
        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }
}
