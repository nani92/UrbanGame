package com.example.nataliajastrzebska.urbangame.laby;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nataliajastrzebska.urbangame.R;
import com.example.nataliajastrzebska.urbangame.Services;
import com.example.nataliajastrzebska.urbangame.laby.com.directions.route.Route;
import com.example.nataliajastrzebska.urbangame.laby.com.directions.route.Routing;
import com.example.nataliajastrzebska.urbangame.laby.com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.barcode.Barcode;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class laby extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback, GpsStatus.Listener,
        com.google.android.gms.location.LocationListener, RoutingListener, GoogleMap.OnMyLocationChangeListener {

    LocationManager mLocationManager;
    GoogleApiClient mGoogleApiClient;
    GoogleMap mMap;
    LocationRequest mLocationRequest;
    Location myLocation;
    int PLACE_PICKER_REQUEST = 1;
    PolylineOptions mPolyOptions;
    PolylineOptions actualPolyOptions;
    private ArrayList<Polyline> polylines;
    private int[] colors = new int[]{Color.RED,Color.BLACK,Color.BLUE,Color.CYAN,Color.DKGRAY};
    private List<LatLng> waypoints;
    int wayIndex = 0;
    private boolean navigating = false;

    DirectionsView directionsView;
    private int direction=0;
    Direction directionEn = Direction.NO;

    double longDif = 0, latDiff =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laby);

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
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        mPolyOptions = new PolylineOptions();
        actualPolyOptions = new PolylineOptions();
        actualPolyOptions.color(getResources().getColor(R.color.colorPrimaryDark));
        actualPolyOptions.width(3);
        polylines =new ArrayList<>();
        directionsView = (DirectionsView) findViewById(R.id.dirView);
    }

    protected synchronized void setmGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, R.string.google_maps_key, this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    void setMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapa);
        mMap = mapFragment.getMap();
        mapFragment.getMapAsync(this);
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(false);//blocks every touch reaction on map
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(this);
    }

    void checkInternetConnection(){
        if(!Services.getInstance().isNetworkAvailable(this)){//do i need this ???
            Log.d("Natalia", "int");
        }
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            Log.d("Natalia", "loca");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Natalia", "on Connected");
        setLocationUpdates();
        startLocationUpdates();
        getLocation();
    }

    private void getLocation() {
        myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void setLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);//10 seconds
        mLocationRequest.setFastestInterval(5000);//5 seconds
        mLocationRequest.setSmallestDisplacement(10);//10 meters
    }

    @Override
    public void onConnectionSuspended(int i) {
        stopLocationUpdates();
        mGoogleApiClient.connect();
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Natalia", "conn failed");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Natalia", "on map ready");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(54.3516273, 18.6426237), 15));
        if(mMap.getMyLocation() != null) {
            myLocation = mMap.getMyLocation();
        }
        else {
            myLocation = new Location("");
            myLocation.setLatitude(54.3516273);
            myLocation.setLongitude(18.6426237);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "location changed", Toast.LENGTH_SHORT).show();
        if(location != null) {
            myLocation = location;
            showMyPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            actualPolyOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));
            mMap.addPolyline(actualPolyOptions);
            if(navigating)
                checkIfNearby(30);
        }
    }

    private void showMyPosition(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                double dLatitude = place.getLatLng().latitude;
                double dLongitude = place.getLatLng().longitude;
                LatLng start = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                LatLng end = new LatLng(dLatitude, dLongitude);

                Routing routing = new Routing.Builder()
                        .travelMode(Routing.TravelMode.WALKING)
                        .withListener(this)
                        .waypoints(start, end)
                        .build();
                routing.execute();
            }
        }
    }


    @Override
    public void onRoutingFailure() {
        Toast.makeText(this,"Something went wrong, Try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingStart() {
        LatLng start = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, mMap.getCameraPosition().zoom));
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        LatLng start = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        waypoints = route.get(0).getPoints();
        wayIndex = 0;
        navigating = true;
        Toast.makeText(this, getDirectionMsg(0), Toast.LENGTH_SHORT).show();
        directionsView.setMsg(getDirectionMsg(0));
        directionsView.invalidate();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);


            //Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, mMap.getCameraPosition().zoom));
    }

    @Override
    public void onRoutingCancelled() {

    }

    void checkIfNearby(int radius) {
        Log.d("Natalia", "distance " + myLocation.distanceTo(getDestLocation()));
        if (myLocation.distanceTo(getDestLocation()) < radius) {
            wayIndex++;
            directionsView.setDirection(directionEn);
            directionsView.setMsg(getDirectionMsg(1));
            directionsView.invalidate();
            Toast.makeText(this, getDirectionMsg(1), Toast.LENGTH_SHORT);
        }

    }

    private String getDirectionMsg(int i) {
        String string = "Idź ";
        double dist = Math.floor(myLocation.distanceTo(getDestLocation()) * 100) / 100;
        string+= dist;
        if (i == 0) {
            string += "m na ";
            string += getDirection(myLocation);
        }
        else{
            string += "m na ";
            string += getTurn(myLocation);
        }
        return string;
    }

    public Location getDestLocation() {
        Location location = new Location("");
        location.setLatitude(waypoints.get(wayIndex).latitude);
        location.setLongitude(waypoints.get(wayIndex).longitude);
        return location;
    }

    String getLongitudeDirection (Location newLocation){
        Location destLocation = new Location("");
        destLocation.setLatitude(waypoints.get(wayIndex).latitude);
        destLocation.setLongitude(waypoints.get(wayIndex).longitude);
        longDif = newLocation.getLongitude() - destLocation.getLongitude();
        if (newLocation.getLongitude() > destLocation.getLongitude()){

            return "W";
        }
        else if ( newLocation.getLongitude() == destLocation.getLongitude()){

            return "";
        }
        else {

            return "E";
        }
    }
    String getLatitudeDirection( Location newLocation){
        Location destLocation = new Location("");
        destLocation.setLatitude(waypoints.get(wayIndex).latitude);
        destLocation.setLongitude(waypoints.get(wayIndex).longitude);
        latDiff = newLocation.getLatitude() - destLocation.getLatitude();
        if (newLocation.getLatitude() > destLocation.getLatitude()){
            return "S";
        }
        else if (newLocation.getLatitude() == destLocation.getLatitude())
            return "";
        else
            return "N";
    }


    String getTurn(Location newLocaton){
        double oldLatDif = latDiff;
        double oldLonDif = longDif;
        String dir = getDirection(newLocaton);
        int oldDirection = direction;
        if (dir == "N")
            direction = 0;
        if (dir == "NE")
            direction = 1;
        if(dir == "E")
            direction = 2;
        if (dir == "SE")
            direction = 3;
        if (dir == "S")
            direction = 4;
        if (dir == "SW")
            direction = 5;
        if (dir == "W")
            direction = 6;
        if (dir == "NW")
            direction = 7;

        if(direction > oldDirection) {
            directionEn = Direction.RIGHT;
            return dir + " i skręć w prawo";
        }
        if (direction <oldDirection) {
            directionEn = Direction.LEFT;
            return dir +  " i skręć w lewo";
        }

        if(oldLatDif > latDiff || oldLonDif > longDif){
            directionEn = Direction.LEFT;
            return dir +  " i skręć w lewo";
        }
        else {
            directionEn = Direction.LEFT;
            return dir +  " i skręć w lewo";
        }

    }

    String getDirection (Location newLocation){
        return getLatitudeDirection(newLocation) + getLongitudeDirection(newLocation);
    }

    @Override
    public void onMyLocationChange(Location location) {
        myLocation = location;
    }
}
