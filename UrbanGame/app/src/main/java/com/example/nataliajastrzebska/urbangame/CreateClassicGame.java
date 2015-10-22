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
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//TO FIX - network and gps dialogs

public class CreateClassicGame extends AppCompatActivity implements  OnMapReadyCallback,  LocationListener, GoogleMap.OnMarkerClickListener {

    protected GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Location mPositionNetwork;
    private Location mPositionGPS;
    private LatLng myPosition;
    private LocationManager mLocationManager;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_classic_game);

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        checkInternetAndGpsConnection();

        setMap();

        setLocationUpdates();

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {}

    @Override
    public void onLocationChanged(Location location) {
        getLocation();
        showMyPosition();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {
        getLocation();
        showMyPosition();}

    @Override
    public void onProviderDisabled(String provider) {checkInternetAndGpsConnection();}



    void checkInternetAndGpsConnection() {
        if (Services.getInstance().isNetworkAvailable(this)) {
            showEnableInternetModeDialog();
        }
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showEnableGPSModeDialog();
        }
    }

    void showEnableInternetModeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EnableInternetDialog enableInternetDialog = new EnableInternetDialog();
        enableInternetDialog.show(fm, "dialog_enable_internet");
    }

    void showEnableGPSModeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EnableGpsDialog enableGpsDialog = new EnableGpsDialog();
        enableGpsDialog.show(fm, "dialog_enable_gps");
    }


    void setMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_creatingClassicGame);
        mMap = mapFragment.getMap();
        mapFragment.getMapAsync(this);
        mMap.setOnMarkerClickListener(this);
    }

    void setLocationUpdates() {

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
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 10, this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, this);

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

        if(mPositionNetwork != null) {
            myPosition = new LatLng(mPositionNetwork.getLatitude(), mPositionNetwork.getLongitude());
        }
        if(mPositionGPS != null) {
            myPosition = new LatLng(mPositionGPS.getLatitude(), mPositionGPS.getLongitude());
        }

    }
    void showMyPosition() {
        if (myPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15));
            mMap.addMarker(new MarkerOptions().position(myPosition));
        }
    }

    public void putMarkerOnMyPosition(View view){
        getLocation();
        mMap.addMarker(new MarkerOptions().position(myPosition));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.remove();
        return true;
    }
}
