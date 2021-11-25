package com.example.spacejuice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.spacejuice.activity.AllHabitsActivity;
import com.example.spacejuice.activity.GooglePlayNotFoundError;
import com.example.spacejuice.activity.LoginActivity;
import com.example.spacejuice.activity.OverviewActivity;
import com.example.spacejuice.activity.WelcomeActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 3;
    public static Member user; // this is the Member instance for the app user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkPlayServices()) {
            Log.d("debugInfo", "PLAY SERVICES FALSE");
            setContentView(R.layout.google_play_not_found);
            Intent intent = new Intent(this, GooglePlayNotFoundError.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_welcome);
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            getUser().setScore(5);
            Log.d("debugInfo", "user score set to 5");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
        }
    }

    /*
        Singleton Support for Member
    */
    public static Member getUser() {
        // Getter to get the Member class for the User
        if (user == null) {
            user = new Member();
            user.initTestData();
        }
        return user;
    }

    public static void setUser(Member new_user) {
        user = new_user;
        user.initTestData();
    }

    public static Boolean checkForSmallDisplay(Context context) {
        /* if the display is small, adjusts the title width to fit properly */
        float densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        return densityDpi > 460;
    }

    boolean checkPlayServices() {
        GoogleApiAvailability gApi = GoogleApiAvailability.getInstance();
        int resultCode = gApi.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (gApi.isUserResolvableError(resultCode)) {
                gApi.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
            }
            return false;
        }
        return true;
    }

}