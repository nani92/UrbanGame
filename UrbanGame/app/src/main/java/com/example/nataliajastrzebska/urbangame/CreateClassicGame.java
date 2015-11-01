package com.example.nataliajastrzebska.urbangame;

import android.Manifest;
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

    protected GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Location myLocation;
    private LatLng myPosition;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationManager;
    private Marker myPositionMarker;
    private float mZoom;

    //TO DO onResume and onPause activities implementation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_classic_game);

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        //google api client doesn't have implemented gps monitoring and on gps status change information, that's why location manager needs to be used
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
        getLocation();
    }
    @Override
    public void onConnectionSuspended(int i) {//temporary connection cut with google api
        stopLocationUpdates();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {//TO DO(?)
        //contains all possible errors while connecting to google play services
        //https://developers.google.com/android/reference/com/google/android/gms/common/ConnectionResult
        Toast.makeText(getApplicationContext(),"Google Api Connection Fail",Toast.LENGTH_SHORT).show();finish();}
    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(myPositionMarker))
            marker.showInfoWindow();
        else
            marker.remove();
        return false;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(54.3516273,18.6426237),15));
    }

    @Override
    public void onLocationChanged(Location location) {
        if(myPositionMarker != null){
            myPositionMarker.remove();
            myPositionMarker = null;
        }
        mZoom = mMap.getCameraPosition().zoom;
        myLocation = location;
        showMyPosition();
    }

    @Override
    public void onGpsStatusChanged(int event) {
        // works only when gps was active during activity
        //if activity starts with gps off it posts nothing
        switch(event){
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                //this case shows up right before start (posting that problem is solved and gps should start) or some time after gps is off
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                //this case works constantly when gps got connection with satellite
                break;
            case GpsStatus.GPS_EVENT_STARTED:
                //this case shows up when gps is turned on, NOT WHEN ACTIVITY STARTS
                break;
            case GpsStatus.GPS_EVENT_STOPPED:

                showEnableGPSModeDialog();
                break;
        }

    }

    void checkInternetConnection(){
        if(!Services.getInstance().isNetworkAvailable(this)){//do i need this ???
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(false);//blocks every touch reaction on map
        mMap.setMyLocationEnabled(true);
    }

    void showMyPosition() {
        mZoom = mMap.getCameraPosition().zoom;
        myPosition = null;
        if(myLocation != null) {
            myPosition = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        }
        if (myPosition != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, mZoom));
            myPositionMarker = mMap.addMarker(new MarkerOptions()
                    .position(myPosition)
                    .alpha(0.9f)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .rotation(180.0f)
                    .title("Your location"));//cannot see strings wtf

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
