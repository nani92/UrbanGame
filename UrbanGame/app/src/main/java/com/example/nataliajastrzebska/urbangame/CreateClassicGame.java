package com.example.nataliajastrzebska.urbangame;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;

import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreateClassicGame extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback, LocationListener, GpsStatus.Listener {

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Location myLocation;
    private LatLng myPosition;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_classic_game);

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mLocationManager.addGpsStatusListener(this);

        checkInternetConnection();

        setmGoogleApiClient();
        setMap();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        setLocationUpdates();
        startLocationUpdates();
        getLocation();}
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();   }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"nie dziala",Toast.LENGTH_SHORT).show();}//////////////////////TO DO
    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.remove();
        return false;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) { }//????????????????????????????????

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        showMyPosition();
    }

    @Override
    public void onGpsStatusChanged(int event) {/////////////////////////TO DO

        switch(event){
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                //???????????????????????????
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                //???????????????????????????
                break;
            case GpsStatus.GPS_EVENT_STARTED:
                //???????????????????????????
                break;
            case GpsStatus.GPS_EVENT_STOPPED:

                showEnableGPSModeDialog();
                break;

        }

    }


    void checkInternetConnection(){
        if(!Services.getInstance().isNetworkAvailable(this)){
            showEnableInternetModeDialog();
        }
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            showEnableGPSModeDialog();
        }
    }

    void showEnableInternetModeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EnableInternetDialog enableInternetDialog = new EnableInternetDialog();
        enableInternetDialog.show(fm, "dialog_enable_internet");
    }

    void showEnableGPSModeDialog() {
        FragmentManager fm2 = getSupportFragmentManager();
        EnableGpsDialog enableGpsDialog = new EnableGpsDialog();
        enableGpsDialog.show(fm2, "dialog_enable_gps");
    }

    protected synchronized void setmGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    protected void setLocationUpdates(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);//10 seconds
        mLocationRequest.setFastestInterval(5000);//5 seconds
        mLocationRequest.setSmallestDisplacement(10);//10 meters
    }

    protected void startLocationUpdates(){
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }

    void setMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_creatingClassicGame);
        mMap = mapFragment.getMap();
        mapFragment.getMapAsync(this);
        mMap.setOnMarkerClickListener(this);
        //mMap.setMyLocationEnabled(true);//whats the location source ??

    }

    void showMyPosition() {
        myPosition = null;
        if(myLocation != null) {
            myPosition = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        }
        if (myPosition != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 18));
            mMap.addMarker(new MarkerOptions().position(myPosition).alpha(0.9f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }

    public void putMarkerOnMyPosition(View view){
        getLocation();
        if(myPosition != null)
            mMap.addMarker(new MarkerOptions().position(myPosition));
    }

    void getLocation(){
        myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    }



}
