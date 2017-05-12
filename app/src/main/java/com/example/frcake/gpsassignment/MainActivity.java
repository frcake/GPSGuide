package com.example.frcake.gpsassignment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private final int permissionCode = 100;
    LocationManager locationManager;
    TextView textView, textView2;
    String badPermissionToast = "For this application to work, GPS data permission is needed!";

    static final double athensLong = 23.7275;
    static final double athensLat = 37.9838;
    static final double peirLong = 23.6513371;
    static final double peirLat = 37.939409;
    static final double thesLong = 22.9444;
    static final double thesLat = 40.6401;
    static final double kritiLong = 24.8093;
    static final double kritiLat = 35.2401;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        Toast toastBadPermission = Toast.makeText(getApplicationContext(), badPermissionToast, Toast.LENGTH_LONG);
        //ask for permissions right after the application starts!
        askForPermission();

        //check if permission was given by the user
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Go to an activity that warns the user about
            //this applications functionality and needed
            //permissions in order to function correctly
            toastBadPermission.show();
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        }
    }


    public void askForPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permissionCode);

    }

    //LocationListener Methods!
    @Override
    public void onLocationChanged(Location location) {
        double currentLat = location.getLatitude();
        double currentLong = location.getLongitude();
        textView.setText("Current Loc:" + String.valueOf(currentLat) + "," + String.valueOf(currentLong));
        float[] distance1 = new float[3];
        float[] distance2 = new float[3];
        float[] distance3 = new float[3];
        float[] distance4 = new float[3];
        float[][] distances = {distance1, distance2, distance3, distance4};
        Location.distanceBetween(currentLat, currentLong, athensLat, athensLong, distance1);
        Location.distanceBetween(currentLat, currentLong, peirLat, peirLong, distance2);
        Location.distanceBetween(currentLat, currentLong, thesLat, thesLong, distance3);
        Location.distanceBetween(currentLat, currentLong, kritiLat, kritiLong, distance4);
        //textView2.setText(String.valueOf(distance1[0]));
        double minDist = -1;

        if (Double.valueOf(distances[0][0]) < 10) {
            textView2.setText("U ARE CLOSE TO ATHENS");
        } else if (Double.valueOf(distances[1][0]) < 10) {
            textView2.setText("U ARE CLOSE TO PIRAEUS");
        } else if (Double.valueOf(distances[2][0]) < 10) {
            textView2.setText("U ARE CLOSE TO THESSALONIKI");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
