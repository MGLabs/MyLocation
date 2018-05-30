package com.mglabs.mylocation;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private  final String LOG_TAG = "MGTestApp";

    private TextView txtLatitude;
    private TextView txtLongitude;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);
        buildGoogleApiClient();
    }

    //Custom method to create an instance of the GoogleApiClient to talk to Google Services
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Connect the client
        mGoogleApiClient.connect();  //when this is done (successfully), it'll fire the onConnected.
                                    // Otherwise it'll fire onConnectionFailed. If it did connect but something went wrong,
                                    //it'll fire onConnectionSuspended
    }

    @Override
    protected void onStop() {
        Log.i(LOG_TAG, "onSTOP method");
        super.onStop();

        //Disconnect the client if something happens that stops the client from accessing the service
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //whenever we need continuos updates, we want a LocationRequest
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);   //ricorda che il manifest sovrascrive questi settings
        mLocationRequest.setInterval(1000);         //update location every second
        Log.i(LOG_TAG, "on CONNECTED method");

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, connectionResult.getErrorMessage());

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG_TAG, "on location CHANGED method");

        //txtOutput.setText(location.toString());
        txtLatitude.setText(Double.toString((location.getLatitude())));
        txtLongitude.setText(Double.toString((location.getLongitude())));
    }


}
