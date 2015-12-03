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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class PlayGameMapScreen extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnMapClickListener,
        LocationListener, GpsStatus.Listener, GoogleApiClient.ConnectionCallbacks {

    TextView gameTitle;
    private Location myLocation;
    protected GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    ArrayList<PointListItem> pointItems;
    private GoogleMap mMap;
    private float mZoom;
    private LocationRequest mLocationRequest;
    private LatLng myPosition;
    private List<GamePoint> gamePointList;
    private int currID = 0;
    private int radius = 30;
    private boolean displayedHint = false;
    private boolean coughtInArea = false;
    private boolean isEnd = false;
    private long startMillis;
    private Location startLocation;

    private TextView timer;


    private int REQUEST_CODE_DISPLAY_TASK = 1010;
    PolylineOptions lineThin, lineThick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game_map_screen);

        startMillis = System.currentTimeMillis();

        gameTitle = (TextView) findViewById(R.id.tv_playGameMapScreen_gameTitle);
        Log.d("Natalia", String.valueOf(CurrentGame.getInstance().getGameInformation()));
        gameTitle.setText(CurrentGame.getInstance().getGameInformation().getName());

        gamePointList = CurrentGame.getInstance().getGameInformation().getPoints();

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
        displayHint();
        setPathObject();
        setTimerTextView();
    }

    private void setTimerTextView() {
        timer = (TextView) findViewById(R.id.tv_playGameMapScreen_timer);
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setTime();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    private void setTime() {
        timer.setText(getTimeString());
    }

    private String getTimeString(){
        long diff = System.currentTimeMillis() - startMillis;
        int seconds = (int) (diff / 1000) % 60 ;
        int minutes = (int) ((diff / (1000*60)) % 60);
        int hours   = (int) ((diff / (1000*60*60)) % 24);
        String time ="";
        if (hours < 10)
            time+="0";
        time+=hours;
        time+=":";
        if(minutes < 10)
            time+="0";
        time+=minutes;
        time+=":";
        if(seconds < 10)
            time+="0";
        time+=seconds;
        return time;
    }

    private void setPathObject() {
        lineThin = new PolylineOptions();
        lineThick = new PolylineOptions();
        lineThin.color(getResources().getColor(R.color.colorPrimaryDark));
        lineThick.color(getResources().getColor(R.color.colorPrimary));
        lineThick.width(20);
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
        if (!Services.getInstance().isNetworkAvailable(this)) {//do i need this ???
            showEnableInternetModeDialog();
        }
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            showEnableGPSModeDialog();
        }
    }

    protected synchronized void setmGoogleApiClient() {
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

    void getLocation() {
        myPosition = null;
        myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (myLocation != null)
            myPosition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

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
        mZoom = 18.0f;//mMap.getCameraPosition().zoom;
        if(myLocation == null)
            startLocation = location;
        myLocation = location;
        myPosition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        showMyPosition();
        if (!isEnd) {
            checkIfNearby();
            lineThin.add(new LatLng(location.getLatitude(), location.getLongitude()));
            lineThin.add(new LatLng(location.getLatitude(), location.getLongitude()));
            mMap.addPolyline(lineThick);
            mMap.addPolyline(lineThin);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        setLocationUpdates();
        startLocationUpdates();
        getLocation();
    }

    protected void setLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);//10 seconds
        mLocationRequest.setFastestInterval(5000);//5 seconds
        mLocationRequest.setSmallestDisplacement(10);//10 meters
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    void showMyPosition() {
        mZoom = 18.0f;//mMap.getCameraPosition().zoom;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, mZoom));
    }

    void checkIfNearby() {
        Log.d("Natalia", "distance " + myLocation.distanceTo(getDestLocation()));
        if (myLocation.distanceTo(getDestLocation()) < radius) {
            displayTask();
        }

    }

    Location getDestLocation() {
        Location destinLocation = new Location("");
        destinLocation.setLatitude(gamePointList.get(currID).getCoordinateX());
        destinLocation.setLongitude(gamePointList.get(currID).getCoordinateY());
        return destinLocation;
    }

    void displayHint() {
        if (!displayedHint) {
            FragmentManager fm = getSupportFragmentManager();
            DisplayHintDialog dh = new DisplayHintDialog();
            dh.show(fm, "dialog_display_hint");
            dh.setMessage(gamePointList.get(currID).getHint());
            displayedHint = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_DISPLAY_TASK) {
            if (resultCode == Activity.RESULT_OK) {
                currID++;
                if(isEndOfGame()){
                    endGame();
                }else {
                    displayedHint = false;
                    coughtInArea = false;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        displayHint();
    }

    private void displayTask(){
        if (!coughtInArea) {
            Intent intent = new Intent(this, DisplayTaskActivity.class);
            intent.putExtra("pointId", currID);
            startActivityForResult(intent, REQUEST_CODE_DISPLAY_TASK);
            addPoint();
            coughtInArea = true;
        }
    }

    void addPoint(){
        LatLng latLng = new LatLng(
                gamePointList.get(currID).getCoordinateX(),
                gamePointList.get(currID).getCoordinateY()
        );
        mMap.addMarker(new MarkerOptions().position(latLng).title("Start")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    private boolean isEndOfGame(){
        return currID >= gamePointList.size();
    }
    private void endGame(){
        Intent intent = new Intent(this, SummaryScreen.class);
        intent.putExtra("time", getTimeString());
        intent.putExtra("distance",countDistance());
        startActivity(intent);
        isEnd = true;
        finish();
    }

    private long countDistance(){
        long distance = 0;
        Location locationOld = new Location("");
        locationOld.setLatitude(lineThin.getPoints().get(0).latitude);
        locationOld.setLongitude(lineThin.getPoints().get(0).longitude);
        for(int i = 1; i < lineThin.getPoints().size(); i++){
            Location locationNew = new Location("");
            locationNew.setLatitude(lineThin.getPoints().get(i).latitude);
            locationNew.setLongitude(lineThin.getPoints().get(i).longitude);
            distance+=locationOld.distanceTo(locationNew);
            locationOld = locationNew;
        }
        return distance;
    }
}
