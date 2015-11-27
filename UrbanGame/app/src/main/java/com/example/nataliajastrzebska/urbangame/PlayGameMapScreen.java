package com.example.nataliajastrzebska.urbangame;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PlayGameMapScreen extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnMapClickListener,
        LocationListener, GpsStatus.Listener, GoogleApiClient.ConnectionCallbacks{

    TextView gameTitle;
    private Location myLocation;
    protected GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    ArrayList<PointListItem> pointItems;
    private GoogleMap mMap;
    private float mZoom;
    private LocationRequest mLocationRequest;
    private LatLng myPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game_map_screen);

        gameTitle = (TextView) findViewById(R.id.tv_playGameMapScreen_gameTitle);
        gameTitle.setText(CurrentGame.getInstance().getGameInformation().getName());

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
        pointItems = new ArrayList<>();
    }

    void setMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_playGame);
        mMap = mapFragment.getMap();
        mapFragment.getMapAsync(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(false);//blocks every touch reaction on map
        mMap.setMyLocationEnabled(true);
    }


    private void checkInternetConnection() {
        if(!Services.getInstance().isNetworkAvailable(this)){//do i need this ???
            showEnableInternetModeDialog();
        }
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            showEnableGPSModeDialog();
        }
    }

    protected synchronized void setmGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
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

    void getLocation(){
        myPosition = null;
        myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(myLocation != null)
            myPosition = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mZoom = mMap.getCameraPosition().zoom;
        myLocation = location;
        myPosition = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        showMyPosition();
    }

    @Override
    public void onConnected(Bundle bundle) {
        setLocationUpdates();
        startLocationUpdates();
        getLocation();
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

    @Override
    public void onConnectionSuspended(int i) {

    }

    void showMyPosition() {
        mZoom = mMap.getCameraPosition().zoom;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, mZoom));
    }
}
