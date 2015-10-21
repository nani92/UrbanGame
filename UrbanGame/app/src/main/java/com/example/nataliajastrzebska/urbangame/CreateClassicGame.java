package com.example.nataliajastrzebska.urbangame;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;

public class CreateClassicGame extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener {

    protected GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Location mPositionNetwork;
    private Location mPositionGPS;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_classic_game);

        checkInternetConnection();

        setMap();

        setLocationUpdates();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {}
    @Override
    public void onConnected(Bundle bundle) {}
    @Override
    public void onConnectionSuspended(int i) {}
    @Override
    public void onLocationChanged(Location location) {
        getLocation();
        showMyPosition();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    void checkInternetConnection() {
        if (!Services.getInstance().isNetworkAvailable(this)) {//is this necessary????
            showEnableInternetModeDialog();
        }
    }

    void showEnableInternetModeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EnableInternetDialog enableInternetDialog = new EnableInternetDialog();
        enableInternetDialog.show(fm, "dialog_enable_internet");
    }

    void showEnableGPSModeDialog() {//todo
    }


    void setMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_creatingClassicGame);
        mMap = mapFragment.getMap();
        mapFragment.getMapAsync(this);
    }

    void setLocationUpdates() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 15, this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,60000, 15, this);

    }

    void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            mPositionNetwork = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            mPositionGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    }
    void showMyPosition() {
        LatLng myPosition = null;
        if(mPositionNetwork != null) {
            myPosition = new LatLng(mPositionNetwork.getLatitude(), mPositionNetwork.getLongitude());
        }
        if(mPositionGPS != null) {
            myPosition = new LatLng(mPositionGPS.getLatitude(), mPositionGPS.getLongitude());
        }
        if (myPosition != null) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
        mMap.moveCamera(CameraUpdateFactory.zoomBy(15));
        mMap.addMarker(new MarkerOptions().position(myPosition));
        }

        //Toast toast = Toast.makeText(getApplicationContext(),"Unable to receive position",Toast.LENGTH_SHORT);
        //toast.show();

    }

}
