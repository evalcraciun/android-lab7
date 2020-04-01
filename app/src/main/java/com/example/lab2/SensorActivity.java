package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;

public class SensorActivity extends AppCompatActivity implements LocationListener {
    // Holds an instance of the SensorManager system service.
    private SensorManager mSensorManager;
    private List<Sensor> sensorList;
    private FusedLocationProviderClient client;

    private LocationManager locationManager;
    public double latitude;
    public double longitude;

    public Criteria criteria;
    public String bestProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Get the sensor service and retrieve the list of sensors.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        // Iterate through the list of sensors, get the sensor name,
        // append it to the string.
        StringBuilder sensorText = new StringBuilder();
        //String sensorText = "";
        for (Sensor currentSensor : sensorList) {
            // currentSensor.getType()
            sensorText.
                    append(System.getProperty("line.separator")).
                    append(mSensorManager.getDefaultSensor(currentSensor.getType())).
                    append(System.getProperty("line.separator"));
        }

        // Update the sensor list text view to display the list of sensors.
        TextView sensorTextView = (TextView) findViewById(R.id.sensor_list);
        sensorTextView.setText(sensorText);

        getLocation();
    }

    public static boolean isLocationEnabled(Context context) {
        return true;
    }

    protected void getLocation() {
        if (isLocationEnabled(SensorActivity.this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            TextView textView = findViewById(R.id.loc);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                textView.setText("Longitude: "+ longitude + "\n" + "Latitude: " + latitude);
//                Toast.makeText(SensorActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
            }
            else{
                //This is what you need:
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        TextView textView = findViewById(R.id.loc);
        textView.setText("Longitude: "+ longitude + "\n" + "Latitude: " + latitude);
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
