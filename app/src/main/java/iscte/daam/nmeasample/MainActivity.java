package iscte.daam.nmeasample;

import android.Manifest;
import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends AppCompatActivity implements LocationListener, GpsStatus.Listener {
    protected LocationManager locationManager;
    public double latitude, longitude;
    private static final int RC_LOCATION = 123;
    protected String history = "";

    protected EditText etLat, etLgt, etNmea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLat = (EditText) findViewById(R.id.etLat);
        etLgt = (EditText) findViewById(R.id.etLgt);
        etNmea = (EditText) findViewById(R.id.etNMEA);

        locationSetup();
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void locationSetup() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            locationManager.addGpsStatusListener(this);
            locationManager.addNmeaListener(new OnNmeaMessageListener() {
                @Override
                public void onNmeaMessage(String s, long l) {
                    history = "NMEA = " + s + "\n" + history ;
                    etNmea.setText(history);
                }
            });
        } else {
            EasyPermissions.requestPermissions(new PermissionRequest.Builder(this, RC_LOCATION, perms)
                .setRationale("I need this to know the location of the user")
                .setPositiveButtonText("OK")
                    .setNegativeButtonText("NO")
                    .build());
        }
    }

    @Override
    public void onGpsStatusChanged(int i) {
        history = "GPS Status = " + i + "\n" + history;
        etNmea.setText(history);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("NMEA Sample", "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        history = "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude() + "\n" + history ;

        etLat.setText(""+latitude);
        etLgt.setText(""+longitude);
        etNmea.setText(history);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
