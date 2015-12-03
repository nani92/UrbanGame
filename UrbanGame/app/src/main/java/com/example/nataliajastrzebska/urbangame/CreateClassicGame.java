package com.example.nataliajastrzebska.urbangame;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;

import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Game;
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

import java.util.ArrayList;

public class CreateClassicGame extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback, LocationListener, GpsStatus.Listener {

    protected GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Location myLocation;
    private LatLng myPosition;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationManager;
    private float mZoom;

    private RelativeLayout leftSideList;
    private ListView listView;
    PointListAdapter listAdapter;
    ArrayList<PointListItem> pointItems;

    private  LatLng coordsToSave = null;



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
        pointItems = new ArrayList<>();
        setLeftSideMenu();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int pointNumber = CurrentGame.getInstance().getGameInformation().getPoints().size() - 1;
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                mMap.addMarker(new MarkerOptions().position(coordsToSave).title(String.valueOf(pointNumber + 1)));
                String pointName =  CurrentGame.getInstance().getGameInformation().getPoints().get(CurrentGame.getInstance().getGameInformation().getPoints().size() - 1).getName();
                pointItems.add(new PointListItem(pointName));
                listAdapter = new PointListAdapter(this, pointItems);
                listView.setAdapter(listAdapter);
            }
            else
                CurrentGame.getInstance().getGameInformation().getPoints().remove(pointNumber);
        }
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
        Toast.makeText(getApplicationContext(),"Google Api Connection Fail",Toast.LENGTH_SHORT).show();finish();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        if(marker.equals(myPositionMarker))
//            marker.showInfoWindow();
//        else
//            marker.remove();
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(54.3516273, 18.6426237), 15));
    }

    @Override
    public void onLocationChanged(Location location) {
        mZoom = mMap.getCameraPosition().zoom;
        myLocation = location;
        myPosition = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
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

                checkInternetConnection();
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

    @Override
    protected void onResume() {
        super.onResume();

        if(mGoogleApiClient.isConnected()) {
            checkInternetConnection();
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected())
            stopLocationUpdates();
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
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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

    private void setLeftSideMenu() {
        leftSideList = (RelativeLayout) findViewById(R.id.rl_activityCreateClassicGame_leftSidePointList);
        listView = (ListView) findViewById(R.id.lv_activityCreateClassicGame_pointListView);
        listAdapter = new PointListAdapter(this, pointItems);
        listView.setAdapter(listAdapter);
    }

    void showMyPosition() {
        mZoom = mMap.getCameraPosition().zoom;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, mZoom));
    }

    void addPoint(LatLng latLng){

        CurrentGame.getInstance().getGameInformation().getPoints().add(new GamePoint());
        int pointNumber = CurrentGame.getInstance().getGameInformation().getPoints().size() - 1;
        CurrentGame.getInstance().getGameInformation().getPoints().get(pointNumber).setNumber(pointNumber + 1);
        CurrentGame.getInstance().getGameInformation().getPoints().get(pointNumber).setCoordinateX(latLng.latitude);
        CurrentGame.getInstance().getGameInformation().getPoints().get(pointNumber).setCoordinateY(latLng.longitude);
        coordsToSave = latLng;
        Intent i = new Intent(this, PointInformationSetupActivity.class);
        startActivityForResult(i, 1);

       }

    public void putMarkerOnMyPosition(View view){
        getLocation();
        if(myPosition != null)
            addPoint(myPosition);
    }

    void getLocation(){
        myPosition = null;
        myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(myLocation != null)
            myPosition = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
    }

    public void saveGameClassicMode(View v){
        CurrentGame.getInstance().getGameInformation().setNumberOfPoints(CurrentGame.getInstance().getGameInformation().getPoints().size());
        Intent i = new Intent(this, SaveGameActivity.class);
        startActivity(i);
        finish();



    }

}
