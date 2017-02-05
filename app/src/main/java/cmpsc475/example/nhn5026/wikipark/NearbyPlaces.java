    package cmpsc475.example.nhn5026.wikipark;

    import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
    import com.google.android.gms.maps.SupportMapFragment;
    import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

    public class NearbyPlaces extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

        private GoogleMap mMap;
        private GoogleApiClient mGoogleApiClient;
        private  Location mLastLocation;
        private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
        private static String TAG = "NearbyPlaces";
        private LocationRequest mLocationRequest;
        double currentLatitude;
        AsyncHttpRequest	asyncHttpRequest	=	null;
        double currentLongitude;
        static	final	String	SERVER	=	"http://api.parkwhiz.com/";
        static final String PW_SERVER_KEY = "62d882d8cfe5680004fa849286b6ce20";
        //static String[] lat_Lists;
        ArrayList<Double> lat_Lists = new ArrayList<>();
        ArrayList<Double> lng_Lists = new ArrayList<>();
        static String[] loc_name;
        static String[] address;
        static String[] city;
        static String[] state;
        static String[] zip;
        static String[] recommendation;
        static String[] av_spots;
        static String[] price;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nearby_places);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this, this)
                    .build();

            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(5 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(1 * 1000); // 1 second, in milliseconds
            //mGoogleApiClient.connect();
        }

        @Override
        protected void onResume(){
            super.onResume();
            mGoogleApiClient.connect();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }

        class	AsyncHttpRequest	extends AsyncTask<Void,Void,JSONObject> {

            String	method;
            String	url;
            JSONViaHttp.QueryStringParams	queryStringParams;
            String	payload;

            public	AsyncHttpRequest(String	method,	String	url,
                                       JSONViaHttp.QueryStringParams	queryStringParams,	String	payload)	{
                this.method	=	method;
                this.url	=	url;
                this.queryStringParams	=	queryStringParams;
                this.payload	=	payload;
            }

            @Override
            protected	JSONObject	doInBackground(Void...	params)	{
                return	JSONViaHttp.get(method,	url,	queryStringParams,	payload);
            }

            @Override
            protected	void	onPostExecute(JSONObject result)	{
                super.onPostExecute(result);

                if	(result	!=	null)	{
                    //result.toString(2));

                    try	{
                        //((TextView)	findViewById(R.id.text4)).setText(
                        // result.toString(2));
                        //lat_Lists= new String[result.getJSONArray("parking_listings").length()];
                        //lng_Lists= new String[result.getJSONArray("parking_listings").length()];
                        loc_name= new String[result.getJSONArray("parking_listings").length()];
                        address= new String[result.getJSONArray("parking_listings").length()];
                        city= new String[result.getJSONArray("parking_listings").length()];
                        state= new String[result.getJSONArray("parking_listings").length()];
                        zip= new String[result.getJSONArray("parking_listings").length()];
                        recommendation= new String[result.getJSONArray("parking_listings").length()];
                        av_spots= new String[result.getJSONArray("parking_listings").length()];
                        price= new String[result.getJSONArray("parking_listings").length()];
                        //lat_Lists[0]= ((Double) result.getDouble("lat")).toString();
                        //lng_Lists[0]= ((Double) result.getDouble("lng")).toString();
                        for(int i=0;i<result.getJSONArray("parking_listings").length();i++){
                            lat_Lists.add(( result.getJSONArray("parking_listings").getJSONObject(i).getDouble("lat")));
                            lng_Lists.add(( result.getJSONArray("parking_listings").getJSONObject(i).getDouble("lng")));
                            loc_name[i]= (result.getJSONArray("parking_listings").getJSONObject(i).getString("location_name"));
                            address[i]= (result.getJSONArray("parking_listings").getJSONObject(i).getString("address"));
                            city[i]= (result.getJSONArray("parking_listings").getJSONObject(i).getString("city"));
                            state[i]= (result.getJSONArray("parking_listings").getJSONObject(i).getString("state"));
                            zip[i]= ((Integer) result.getJSONArray("parking_listings").getJSONObject(i).getInt("zip")).toString();
                            recommendation[i]= ((Integer) result.getJSONArray("parking_listings").getJSONObject(i).getInt("recommendations")).toString();
                            av_spots[i]= ((Integer) result.getJSONArray("parking_listings").getJSONObject(i).getInt("available_spots")).toString();
                            price[i]= result.getJSONArray("parking_listings").getJSONObject(i).getString("price_formatted");

                        }
                        Log.i(TAG,lat_Lists.get(0) +"from async tasks");
                        if(lat_Lists.size()==0){
                            Log.i(TAG, "no parking locations nearby");
                        }
    ContentResolver cr = getContentResolver();
                        ContentValues values = new ContentValues();
                        String where= null;

                        for(int k=0;k<price.length;k++) {
                            where = "latitude=" + lat_Lists.get(k) + " AND longitude = " + lng_Lists.get(k);
                            Cursor c = cr.query(PlacesContentProvider.CONTENT_URI,
                                    new String[]{"location", "address", "city", "state", "zip", "latitude", "longitude", "available_spots", "price"}, where, null, null);
                            if (!(c.moveToFirst())) {
                                values.put("location", loc_name[k]);
                                values.put("address", address[k]);
                                values.put("city", city[k]);
                                values.put("state", state[k]);
                                values.put("zip", zip[k]);
                                values.put("latitude", lat_Lists.get(k));
                                values.put("longitude", lng_Lists.get(k));
                                values.put("recommendations", recommendation[k]);
                                values.put("available_spots", av_spots[k]);
                                values.put("price", price[k]);
                                values.put("favourite", "false");
                                cr.insert(PlacesContentProvider.CONTENT_URI, values);
                            }
                            else{
                                Log.i(TAG,"they are already stored in the database");
                            }

                        }

                        //Intent intent = new Intent(getBaseContext(), PlacesList.class);
                        //Intent intent_for_desired_loc = new Intent(getBaseContext(), LocationFinderMap.class);
                        //intent_for_desired_loc.putExtra("calling-activity", ActivityConstants.ACTIVITY_2);
                        //intent_for_desired_loc.putExtra("Longitude list",lng_Lists);
                        //intent_for_desired_loc.putExtra("Latitude list", lat_Lists);
                        //startActivity(intent);

                        //startActivity(intent_for_desired_loc);
                    }
                    catch	(JSONException e)	{

                        Log.e(TAG, "Error	retrieving	list:	" + e.getMessage());

                    }

                }
                /*else	{
                    //((TextView)	findViewById(R.id.text4)).setText("Bad	request");
                    Log.e(TAG, "no parking locations found");
                }*/
                asyncHttpRequest	=	null;
            }
        }


        @Override
        protected void onPause() {
            super.onPause();
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
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
        @Override
        public void onMapReady(GoogleMap googleMap) {

            Log.i(TAG,"value of current latitude is(first call) :"+ String.valueOf(currentLatitude));
            mMap = googleMap;
            Log.i(TAG,"current latitude is(second call)"+ String.valueOf(currentLatitude));
            //double currentLatitude = mLastLocation.getLatitude();
            //double currentLongitude = mLastLocation.getLongitude();
            Log.i(TAG,"current latitude is(third call)"+ String.valueOf(currentLatitude));
            LatLng current_loc = new LatLng(currentLatitude, currentLongitude);
            mMap.addMarker(new MarkerOptions().position(current_loc).title("current location").snippet("hhghghg"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current_loc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            // Add a marker in Sydney and move the camera
            //LatLng sydney = new LatLng(-34, 151);
            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            Log.i(TAG, "before if, but after current location has been set");
            //Log.i(TAG, lng_Lists.size());
            if(lng_Lists!=null) {
                Log.i(TAG, "inside if");
                Marker m[] = new Marker[lng_Lists.size()];
                for (int i = 1; i < lng_Lists.size(); i++) {
                    LatLng nearby_locations = null;
                    nearby_locations = new LatLng(lat_Lists.get(i), lng_Lists.get(i));
                    m[1] = mMap.addMarker(new MarkerOptions().position(nearby_locations).title("Marker in " + i + "location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(nearby_locations));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                }
            }


            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker arg0) {
                    if (arg0.getTitle().equals("Marker in " + 1 + "location")) {
                        // if marker source is clicked
                        Intent intent = new Intent(NearbyPlaces.this, PlaceInfo.class);
                        intent.putExtra("latlist", lat_Lists.get(0));
                        intent.putExtra("lnglist", lng_Lists.get(0));
                        Log.i(TAG, "values got from whiz" + lat_Lists.get(0).toString());
                        intent.putExtra("callingActivity", ActivityConstants.ACTIVITY_NEARBY);
                        startActivity(intent);
                    }
                    if (arg0.getTitle().equals("Marker in " + 2 + "location")) {
                        // if marker source is clicked
                        Intent intent = new Intent(NearbyPlaces.this, PlaceInfo.class);
                        intent.putExtra("latlist", lat_Lists.get(1));
                        intent.putExtra("lnglist", lng_Lists.get(1));
                        Log.i(TAG, "values got from whiz" + lat_Lists.get(1).toString());
                        intent.putExtra("callingActivity", ActivityConstants.ACTIVITY_NEARBY);
                        startActivity(intent);
                    }
                    if (arg0.getTitle().equals("Marker in " + 3 + "location")) {
                        // if marker source is clicked
                        Intent intent = new Intent(NearbyPlaces.this, PlaceInfo.class);
                        intent.putExtra("latlist", lat_Lists.get(2));
                        intent.putExtra("lnglist", lng_Lists.get(2));
                        Log.i(TAG, "values got from whiz" + lat_Lists.get(2).toString());
                        intent.putExtra("callingActivity", ActivityConstants.ACTIVITY_NEARBY);
                        startActivity(intent);
                    }
                    if (arg0.getTitle().equals("Marker in " + 4 + "location")) {
                        // if marker source is clicked
                        Intent intent = new Intent(NearbyPlaces.this, PlaceInfo.class);
                        intent.putExtra("latlist", lat_Lists.get(3));
                        intent.putExtra("lnglist", lng_Lists.get(3));
                        Log.i(TAG, "values got from whiz" + lat_Lists.get(3).toString());
                        intent.putExtra("callingActivity", ActivityConstants.ACTIVITY_NEARBY);
                        startActivity(intent);
                    }
                    if (arg0.getTitle().equals("Marker in " + 5 + "location")) {
                        // if marker source is clicked
                        Intent intent = new Intent(NearbyPlaces.this, PlaceInfo.class);
                        intent.putExtra("latlist", lat_Lists.get(4));
                        intent.putExtra("lnglist", lng_Lists.get(4));
                        Log.i(TAG, "values got from whiz" + lat_Lists.get(4).toString());
                        intent.putExtra("callingActivity", ActivityConstants.ACTIVITY_NEARBY);
                        startActivity(intent);
                    }
                    if (arg0.getTitle().equals("Marker in " + 6 + "location")) {
                        // if marker source is clicked
                        Intent intent = new Intent(NearbyPlaces.this, PlaceInfo.class);
                        intent.putExtra("latlist", lat_Lists.get(5));
                        intent.putExtra("lnglist", lng_Lists.get(5));
                        Log.i(TAG, "values got from whiz" + lat_Lists.get(5).toString());
                        intent.putExtra("callingActivity", ActivityConstants.ACTIVITY_NEARBY);
                        startActivity(intent);
                    }
                    if (arg0.getTitle().equals("Marker in " + 7 + "location")) {
                        // if marker source is clicked
                        Intent intent = new Intent(NearbyPlaces.this, PlaceInfo.class);
                        intent.putExtra("latlist", lat_Lists.get(6));
                        intent.putExtra("lnglist", lng_Lists.get(6));
                        Log.i(TAG, "values got from whiz" + lat_Lists.get(6).toString());
                        intent.putExtra("callingActivity", ActivityConstants.ACTIVITY_NEARBY);
                        startActivity(intent);
                    }
                    if (arg0.getTitle().equals("Marker in " + 8 + "location")) {
                        // if marker source is clicked
                        Intent intent = new Intent(NearbyPlaces.this, PlaceInfo.class);
                        intent.putExtra("latlist", lat_Lists.get(7));
                        intent.putExtra("lnglist", lng_Lists.get(7));
                        Log.i(TAG, "values got from whiz" + lat_Lists.get(7).toString());
                        intent.putExtra("callingActivity", ActivityConstants.ACTIVITY_NEARBY);
                        startActivity(intent);
                    }
                    if (arg0.getTitle().equals("Marker in " + 9 + "location")) {
                        // if marker source is clicked
                        Intent intent = new Intent(NearbyPlaces.this, PlaceInfo.class);
                        intent.putExtra("latlist", lat_Lists.get(8));
                        intent.putExtra("lnglist", lng_Lists.get(8));
                        Log.i(TAG, "values got from whiz" + lat_Lists.get(8).toString());
                        intent.putExtra("callingActivity", ActivityConstants.ACTIVITY_NEARBY);
                        startActivity(intent);
                    }
                    if (arg0.getTitle().equals("Marker in " + 10 + "location")) {
                        // if marker source is clicked
                        Intent intent = new Intent(NearbyPlaces.this, PlaceInfo.class);
                        intent.putExtra("latlist", lat_Lists.get(9));
                        intent.putExtra("lnglist", lng_Lists.get(9));
                        Log.i(TAG, "values got from whiz" + lat_Lists.get(9).toString());
                        intent.putExtra("callingActivity", ActivityConstants.ACTIVITY_NEARBY);
                        startActivity(intent);
                    }
                    if (arg0.getTitle().equals("Marker in " + 11 + "location")) {
                        // if marker source is clicked
                        Intent intent = new Intent(NearbyPlaces.this, PlaceInfo.class);
                        intent.putExtra("latlist", lat_Lists.get(10));
                        intent.putExtra("lnglist", lng_Lists.get(10));
                        Log.i(TAG, "values got from whiz" + lat_Lists.get(10).toString());
                        intent.putExtra("callingActivity", ActivityConstants.ACTIVITY_NEARBY);
                        startActivity(intent);
                    }


                    return true;
                }

            });




      //      }


        }

        @Override
        public void onConnected(Bundle bundle) {

            Log.i(TAG, "Location services connected.");
            try {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLastLocation == null) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                }

                //currentLatitude= mLastLocation.getLatitude();
                //currentLongitude= mLastLocation.getLongitude();
                Log.i(TAG," value of current latitude"+ String.valueOf(currentLatitude));
                if	(asyncHttpRequest	!=	null)	{
                    asyncHttpRequest.cancel(true);
                }
                currentLatitude=41.8857256;
                currentLongitude=-87.6369590;
                asyncHttpRequest	=	new	AsyncHttpRequest("GET",	SERVER	+
                        "search/?lat="+currentLatitude+"&lng="+currentLongitude+"&key="+PW_SERVER_KEY,	null,	null);
                asyncHttpRequest.execute();
                //Intent intent = new Intent(this,LocationFinderMap.class);
                //intent.putExtra("lat",lat_Lists);
                //intent.putExtra("lat",lng_Lists);
                //intent.putExtra("callingActivity",ActivityConstants.ACTIVITY_1);
                //startActivity(intent);
                Log.i(TAG, "from on connected, after async call " + String.valueOf(currentLatitude));
                //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                  //      .findFragmentById(R.id.map);
                //mapFragment.getMapAsync(this);
            }
            catch (SecurityException s){
                Log.i(TAG,"permission not granted"+ s);
            }

            //currentLatitude=41.8857256;
            //currentLongitude=-87.6369590;



            //Log.i(TAG,"latitude response "+  lat_Lists[0]);

        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.i(TAG, "Location services suspended. Please reconnect.");
            // The connection has been interrupted.
            // Disable any UI components that depend on Google APIs
            // until onConnected() is called.

        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation=location;
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
    }
