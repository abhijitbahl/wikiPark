package cmpsc475.example.nhn5026.wikipark;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class LocationFinderMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private  Location mLastLocation;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MyActivity";
    private LocationRequest mLocationRequest;
//    String lat_Lists[]=null;
  //  String lng_Lists[]=null;
    ArrayList<Double> lat_Lists= new ArrayList<>();
    ArrayList<Double> lng_Lists= new ArrayList<>();
    double latitudeList[];
    double longitudeList[];
    int callingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_finder_map);

        // Create an instance of GoogleAPIClient.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                 .build();


// for autocomplete edit text
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        callingActivity = getIntent().getIntExtra("calling-activity", 0);

    }


    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        //
        // More about this in the 'Handle Connection Failures' section.
        if (result.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                result.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + result.getErrorCode());
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();

        //Intent intent=getIntent();
        //if ( extras_bundle!= null) {
            //lat_Lists= extras_bundle.getStringArrayList("LatitudeList");// number of rows for the table
            //lng_Lists= extras_bundle.getStringArrayList("LongitudeList");// same for number of rows
            //lat_Lists = (ArrayList<Double>) intent.getExtras().getSerializable("lat");
            //lng_Lists = (ArrayList<Double>) intent.getExtras().getSerializable("lng");
            //Log.i(TAG,"we have"+ lat_Lists[0]);
        //}
        Log.i(TAG,"we were in onresume");
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(Bundle connectionHint) {

        Log.i(TAG, "Location services connected.");
        switch (callingActivity) {
            case ActivityConstants.ACTIVITY_1:
                // Activity is started from Activity1
                try{
                    mLastLocation= LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);
                    Log.i(TAG,"inside the try for onconnected");
                    if (mLastLocation == null) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                        Log.i(TAG, "inside the if after try for onconnected");
                    }
                    //double currentLatitude = mLastLocation.getLatitude();
                    //double currentLongitude = mLastLocation.getLongitude();
                    //Intent nearby_places = new Intent(this,NearbyPlaces.class);
                    //nearby_places.putExtra("currentLatitude",currentLatitude);
                    //nearby_places.putExtra("currentLongitude",currentLongitude);
                    //startActivity(nearby_places);

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);

                    Log.i(TAG, mLastLocation.toString() + "5");
                }
                catch (SecurityException s){
                    Log.i(TAG,"permission not granted"+ s);
                }
                break;
            case ActivityConstants.ACTIVITY_2:
                // Activity is started from Activity2
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
                break;
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    //@Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng nearby_locations=null;
        //latitudeList= new double[lat_Lists.size()];
        //longitudeList=new double[lng_Lists.size()];

        switch (callingActivity) {
            case ActivityConstants.ACTIVITY_1:
                // Add a marker in current location and move the camera
                //code for current location

                double currentLatitude = mLastLocation.getLatitude();
                double currentLongitude = mLastLocation.getLongitude();
                LatLng current_loc = new LatLng(currentLatitude, currentLongitude);
                mMap.addMarker(new MarkerOptions().position(current_loc).title("current location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(current_loc));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                //Double currentLatitude=41.8857256;
                //Double currentLongitude=-87.6369590;
/*
                Marker m[] = new Marker[lng_Lists.size()];
                for(int i=1;i<lng_Lists.size();i++){
         //           LatLng nearby_locations=null;
                    //Log.i(TAG," "+lat_Lists[i]);
                    //Log.i(TAG," "+lng_Lists[i]);
                    nearby_locations = new LatLng(lat_Lists.get(i),lng_Lists.get(i));
                    m[1]=mMap.addMarker(new MarkerOptions().position(nearby_locations).title("Marker in " + i + "location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(nearby_locations));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                }
                */
/*
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                {

                    @Override
                    public boolean onMarkerClick(Marker arg0) {
                        if(arg0.getTitle().equals("Marker in " + 1 + "location")){
                            // if marker source is clicked
                            Intent intent = new Intent(LocationFinderMap.this,PlaceInfo.class);
                            intent.putExtra("latlist",lat_Lists.get(0));
                            intent.putExtra("lnglist",lng_Lists.get(0));
                            Log.i(TAG,lat_Lists.get(0).toString());
                            intent.putExtra("callingActivity",ActivityConstants.ACTIVITY_NEARBY);
                            startActivity(intent);
                        }
                        //Toast.makeText(MainActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast\
                        //
                        return true;
                    }

                });
*/
                break;
            case ActivityConstants.ACTIVITY_2:
                // Activity is started from Activity2
                for(int i=1;i<lat_Lists.size();i++){
                    //Log.i(TAG," "+lat_Lists[i]);
                    //Log.i(TAG," "+lng_Lists[i]);
                    //latitudeList[i] = Double.parseDouble(lat_Lists.get(i));
                    //longitudeList[i] = Double.parseDouble(lng_Lists.get(i));
                    //nearby_locations = new LatLng(latitudeList[i], longitudeList[i]);
                    //mMap.addMarker(new MarkerOptions().position(nearby_locations).title("Marker in " + i + "location"));
                }
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(nearby_locations));
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                break;
        }


/*
        // Add a marker in current location and move the camera
        double currentLatitude = mLastLocation.getLatitude();
        double currentLongitude = mLastLocation.getLongitude();
        LatLng sydney = new LatLng(currentLatitude, currentLongitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
*/
    }

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation=location;
    }

}





