package com.example.nataliajastrzebska.urbangame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class CreateRemoteGame extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {


    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    private GoogleMap mMap;
    InputMethodManager inputMethodManager;
    View view;
    private RelativeLayout leftSideList;
    private ListView listView;
    PointListAdapter listAdapter;
    ArrayList<PointListItem> pointItems;
    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    private  LatLng coordsToSave = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_remote_game);

        checkInternetConnection();

        setmGoogleApiClient();
        setmAdapter();
        setmAutocompleteView();

        setmMap();

        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        mAutocompleteView.setAdapter(mAdapter);

        setOnMapClicked();

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
    public void onMapReady(GoogleMap map) {
         showStartingPointLocationAtMap();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    //do usunięcia
    private LatLngBounds AUSTRALIA = new LatLngBounds(
            new LatLng(-44, 113), new LatLng(-10, 154));

    void showStartingPointLocationAtMap() {
        //starting location should be stored in Settings
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 5));
    }

    void showLocationAtMap(LatLngBounds place) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(place, 1));
    }

    void hideKeyboard() {
        if(getCurrentFocus() != null) {
            inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    void setmGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, R.string.google_maps_key, this)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    void setLeftSideMenu(){
        leftSideList = (RelativeLayout) findViewById(R.id.activityCreateRemoteGame_leftSidePointList);
        listView = (ListView) findViewById(R.id.activityCreateRemoteGame_pointListView);
        listAdapter = new PointListAdapter(this, pointItems);
        listView.setAdapter(listAdapter);
    }

    void setmAdapter() {
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS, null);
    }

    void setmAutocompleteView() {
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.tv_createRemoteGameActivity_searchLocation);
    }

    void setmMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_creatingRemoteGame);
        mMap = mapFragment.getMap();
        mapFragment.getMapAsync(this);
    }

    void setOnMapClicked() {
        mMap.setOnMapClickListener(this);
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                places.release();
                return;
            }
            final Place place = places.get(0);
            showLocationAtMap(new LatLngBounds(
                    new LatLng(
                            place.getLatLng().latitude - 0.5,
                            place.getLatLng().longitude - 0.5),
                    new LatLng(
                            place.getLatLng().latitude + 0.5,
                            place.getLatLng().longitude + 0.5
                    )));
            hideKeyboard();
            places.release();
        }
    };

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


    @Override
    public void onMapClick(LatLng latLng) {
        addPoint(latLng);
    }

    public void saveGameButtonClicked(View view) {
        int pointNumber = CurrentGame.getInstance().getGameInformation().getPoints().size();
        CurrentGame.getInstance().getGameInformation().setNumberOfPoints(pointNumber);
        Intent i = new Intent(this, SaveGameActivity.class);
        startActivity(i);
        finish();

    }

    void checkInternetConnection(){
        if(!Services.getInstance().isNetworkAvailable(this)){
            showEnableInternetModeDialog();
        }
    }

    void showEnableInternetModeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EnableInternetDialog enableInternetDialog = new EnableInternetDialog();
        enableInternetDialog.show(fm, "dialog_enable_internet");
    }


}
