package com.example.nataliajastrzebska.urbangame;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class CreateRemoteGame extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {


    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    private GoogleMap mMap;
    InputMethodManager inputMethodManager;
    View view;
    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_remote_game);

        setmGoogleApiClient();
        setmAdapter();
        setmAutocompleteView();

        setmMap();

        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        mAutocompleteView.setAdapter(mAdapter);



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

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
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


}
