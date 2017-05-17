package com.example.frcake.gpsassignment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements LocationListener {

    private final int permissionCode = 100;
    LocationManager locationManager;
    Button button;
    TextView textView, textView2;
    private static boolean clickFlag = true;
    String[] perm = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    int result[] = new int[]{};
    //sets the global radius of proximity
    public static final double radius = 100;

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

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askForPermission();
            onRequestPermissionsResult(permissionCode,perm,result);
        }else if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getUpdates();
        }

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView5);
        textView2 = (TextView) findViewById(R.id.textView2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clickFlag) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        askForPermission();
                        onRequestPermissionsResult(permissionCode,perm,result);
                    }else if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        getUpdates();
                    }

                    button.setText("Stop Guide");
                    clickFlag = true;
                } else {
                    try {
                        stopGps();
                    } catch (Exception ex) {

                    }
                    button.setText("Start Guide");
                    clickFlag = false;
                }
            }
        });
    }

    public void getUpdates() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);
    }

    public void stopGps() {
        locationManager.removeUpdates(MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        button = (Button) findViewById(R.id.button);
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    button.setText("Stop Guide");
                    clickFlag = true;
                    getUpdates();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    button.setText("Start Guide");
                    clickFlag = false;
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void askForPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,perm, permissionCode);

    }

    //LocationListener Methods!
    @Override
    public void onLocationChanged(Location location) {
        double currentLat = location.getLatitude();
        double currentLong = location.getLongitude();
        // textView.setText("Current Loc:" + String.valueOf(currentLat) + "," + String.valueOf(currentLong));
        float[] distance1 = new float[3];
        float[] distance2 = new float[3];
        float[] distance3 = new float[3];
        float[] distance4 = new float[3];
        Location.distanceBetween(currentLat, currentLong, athensLat, athensLong, distance1);
        Location.distanceBetween(currentLat, currentLong, peirLat, peirLong, distance2);
        Location.distanceBetween(currentLat, currentLong, thesLat, thesLong, distance3);
        Location.distanceBetween(currentLat, currentLong, kritiLat, kritiLong, distance4);
        float[][] distances = {distance1, distance2, distance3, distance4};
        String closest = "defaultLocation";
        switch (result(distances)) {
            case "Athens":
                startActivity(new Intent(MainActivity.this, AthensActivity.class));
                break;
            case "Piraeus":
                startActivity(new Intent(MainActivity.this, PiraeusActivity.class));
                break;
            case "Thessaloniki":
                startActivity(new Intent(MainActivity.this, ThessalonikiActivity.class));
                break;
            case "Kriti":
                startActivity(new Intent(MainActivity.this, KritiActivity.class));
                break;
            default:
                startActivity(new Intent(this, MainActivity.class));
                break;
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


    public static String result(float[][] distances) {
        if (Double.valueOf(distances[0][0]) < radius) {
            return "Athens";
        } else if (Double.valueOf(distances[1][0]) < radius) {
            return "Piraeus";
        } else if (Double.valueOf(distances[2][0]) < radius) {
            return "Thessaloniki";
        } else if (Double.valueOf(distances[3][0]) < radius) {
            return "Kriti";
        } else {
            return "Nowhere";
        }
    }


    protected void onPause(){
        super.onPause();
        stopGps();
    }

    protected void onResume(){
        super.onResume();
        getUpdates();
    }

}
