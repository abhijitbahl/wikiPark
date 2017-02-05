package cmpsc475.example.nhn5026.wikipark;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParkSearch extends AppCompatActivity implements OnItemSelectedListener{

    static	final	String	SERVER	=	"http://api.parkwhiz.com/";
    static final String PW_SERVER_KEY = "62d882d8cfe5680004fa849286b6ce20";

    static	final	String	TAG	=	"ParkSearch";
    String input;//grab the state, city and street
    static final String TEMP_ADDRESS = "address";
    static final String TEMP_ADDRESS2 = "subaddress1";
    static final String TEMP_ADDRESS3 = "subaddress2";

    //static String[] lat_Lists;
    //static String[] lng_Lists;
    ArrayList<Double> lat_Lists= new ArrayList<>();
    ArrayList<Double> lng_Lists= new ArrayList<>();
    static String[] loc_name;
    static String[] address;
    static String[] city;
    static String[] state;
    static String[] zip;
    static String[] recommendation;
    static String[] av_spots;
    static String[] price;
    static String[] place;
    //String temp_address;
    String temp_address=new String();
    StringBuffer temp_address2= new StringBuffer();
    Spinner spinner1, spinner2;
    String str1[]=null;
    String str_temp="";
    AsyncHttpRequest	asyncHttpRequest	=	null;
    StringBuffer ad_str=new StringBuffer();
    String ad_str_alias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_park_search);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        ad_str.append("");

        if (savedInstanceState != null) {
            temp_address = savedInstanceState.getString("TEMP_ADDRESS");
            str_temp = savedInstanceState.getString("TEMP_ADDRESS2");
            ad_str_alias = savedInstanceState.getString("TEMP_ADDRESS3");
        }

        //adding autocomplete at the end of the layout before button
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Street and State");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getAddress());
                temp_address.concat(place.getAddress().toString());
                Log.i(TAG, "Place: " +temp_address);
                //address=address.replaceAll(",","");
                String str[] = temp_address.toString().split(",");
                //temp_address2.add(str[0]);
                str1 = str[0].split(" ");
                StringBuffer temp =new StringBuffer();

                for (int i = 1; i < str1.length; i++) {
                    temp.append(str1[i]);
                }
                Log.i(TAG,temp.toString());
                temp_address2.append(temp.toString());
                //temp_address2.=str1;
                for (int i = 1; i < str1.length; i++) {
                    ad_str.append("+");
                    ad_str.append(str1[i]);
                }
                //code to get the states and city from the spinners

                ad_str.append(",+");
                ad_str.append(spinner2.getSelectedItem());
                ad_str_alias=ad_str.toString();
                Log.i(TAG, "ad_str_alias " + ad_str_alias);
                str_temp.concat(str1[0]);
                Log.i(TAG, "str_temp " + str_temp);
                //ad_str.append(",+");
                //ad_str.append(spinner1.getSelectedItem());

                https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=50000&key=GP_SERVER_KEY
                Log.i(TAG, "Place: " + address);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

         //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

        //SPINNER2 WILL BE GENERATED BASED ON A STATE SELECTED
/*
        btnSearch = (Button)findViewById(R.id.button_search);

            btnSearch.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //String restURL = "http://www.androidexample.com/media/webservice/JsonReturn.php";
                    String adURL = "http://api.parkwhiz.com/search/?destination=312+N+wacker+Dr,+Chicago&start=1456424018&end=1456434818&key=62d882d8cfe5680004fa849286b6ce20";
                    new searchOperation().execute(adURL);

                }
            });
            */
        }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TEMP_ADDRESS, temp_address);
        savedInstanceState.putString(TEMP_ADDRESS2, str_temp);
        savedInstanceState.putString(TEMP_ADDRESS3, ad_str_alias);
    }

    public	void btnSearch (View	view)	{
        if	(asyncHttpRequest	!=	null)	{
            asyncHttpRequest.cancel(true);
        }

        asyncHttpRequest	=	new	AsyncHttpRequest("GET",	SERVER	+
                "search/?destination="+str_temp+ad_str_alias+"&key="+PW_SERVER_KEY,	null,	null);
                //312+N+wacker+Dr,+Chicago&start=1456424018&end=1456434818&key="+ SERVER_KEY,	null,	null);
        //asyncHttpRequest	=	new	AsyncHttpRequest("GET",	SERVER	+
          //      "search/?destination="+"33"+str1[0]+spinner2.getSelectedItem()+"&key="+PW_SERVER_KEY,	null,	null);
/*
        asyncHttpRequest	=	new	AsyncHttpRequest("GET",	SERVER	+
                "search/?destination=312+N+wacker+Dr,+Chicago&start=1456424018&end=1456434818&key="+ SERVER_KEY,	null,	null);
                */
        asyncHttpRequest.execute();
    }

    @Override
    protected	void	onPause()	{
        super.onPause();
        if	(asyncHttpRequest	!=	null)	{
            asyncHttpRequest.cancel(true);
        }
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
                    place = new String[result.getJSONArray("parking_listings").length()];
                    //lat_Lists[0]= ((Double) result.getDouble("lat")).toString();
                    //lng_Lists[0]= ((Double) result.getDouble("lng")).toString();
                    for(int i=0;i<result.getJSONArray("parking_listings").length();i++){
                        lat_Lists.add((result.getJSONArray("parking_listings").getJSONObject(i).getDouble("lat")));
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

                    ContentResolver cr = getContentResolver();
                    //cr.delete(PlacesContentProvider.CONTENT_URI,null, null);
                    //finish();
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
                            //values.put("name",temp_address2.get(0));
                            cr.insert(PlacesContentProvider.CONTENT_URI, values);
                        }
                        /*else{
                            //values.put("name",temp_address2.get(0));
                            cr.update(PlacesContentProvider.CONTENT_URI, values, where, null);
                        }*/
                    }


                    Intent intent = new Intent(getBaseContext(), PlacesList.class);
                    //Intent intent_for_desired_loc = new Intent(getBaseContext(), LocationFinderMap.class);
                    //intent_for_desired_loc.putExtra("calling-activity", ActivityConstants.ACTIVITY_2);
                    //intent_for_desired_loc.putExtra("LongitudeList",lng_Lists);
                    //intent_for_desired_loc.putExtra("LatitudeList", lat_Lists);
                    intent.putExtra("LatitudeList", lat_Lists);
                    intent.putExtra("LongitudeList",lng_Lists);
             //       intent.putExtra("name",temp_address2);
            /*        intent.putExtra("location name",loc_name);
                    intent.putExtra("address",address);
                    intent.putExtra("city",city);
                    intent.putExtra("state",state);
                    intent.putExtra("zip",zip);
                    intent.putExtra("recommendations",recommendation);
                    intent.putExtra("available spots",av_spots);
                    intent.putExtra("price",price);
                    */
                    startActivity(intent);

                    //startActivity(intent_for_desired_loc);
                    //((TextView)	findViewById(R.id.text4)).setText(
                      //      lat_Lists[0]);
                }
                catch	(JSONException e)	{
                    Toast.makeText(ParkSearch.this,"No parking spots found for the given address",Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error	retrieving	list:	" + e.getMessage());
                }

            }
            else	{
                //((TextView)	findViewById(R.id.text4)).setText("Bad	request");
            }
            asyncHttpRequest	=	null;
        }
    }

    //for dark mode setting
    @Override
    protected void onResume(){
        super.onResume();



        String uriLookup1 = "@drawable/lookup";
        String uriLookup2 = "@drawable/lookup2";
        int lookup1 = getResources().getIdentifier(uriLookup1, null, getPackageName());
        int lookup2 = getResources().getIdentifier(uriLookup2, null, getPackageName());
        Drawable resLookup1 = getResources().getDrawable(lookup1);
        Drawable resLookup2 = getResources().getDrawable(lookup2);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean changeColor = sharedPref.getBoolean(getString(R.string.pref_filtered_key1), true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RelativeLayout parkSearchScreen = (RelativeLayout) findViewById(R.id.parkSearch);
        ImageButton btn = (ImageButton) findViewById(R.id.button_search);
        TextView txt1 = (TextView) findViewById(R.id.text1);
        TextView txt2 = (TextView) findViewById(R.id.text2);
        TextView txt3 = (TextView) findViewById(R.id.text3);
        if(changeColor) {
            txt1.setTextColor(Color.rgb(0, 34, 9));
            txt2.setTextColor(Color.rgb(0,34,9));
            txt3.setTextColor(Color.rgb(0,34,9));

            btn.setImageDrawable(resLookup2);
            btn.setBackgroundColor(Color.rgb(105, 105, 105));
            parkSearchScreen.setBackgroundColor(Color.rgb(105,105,105));

            toolbar.setBackgroundColor(Color.rgb(51,51,51));
        }
        else{

            txt1.setTextColor(Color.rgb(32,35,77));
            txt2.setTextColor(Color.rgb(32,35,77));
            txt3.setTextColor(Color.rgb(32,35,77));

            btn.setImageDrawable(resLookup1);
            btn.setBackgroundColor(Color.rgb(224, 238, 238));
            parkSearchScreen.setBackgroundColor(Color.rgb(224,238,238));
            toolbar.setBackgroundColor(Color.rgb(0,154,205));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
                               long arg3) {
        // TODO Auto-generated method stub
        parent.getItemAtPosition(pos);
        if(pos==0) {


            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Alabama,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);
        }
        else if(pos==1) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Arizona,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==2) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_California,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==3) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Colorado,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==4) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Connecticut,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==5) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Florida,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==6) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Georgia,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==7) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Illinois,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==8) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Indiana,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==9) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Iowa,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==10) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Louisiana,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==11) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Maryland,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==12) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Massachusetts,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==13) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Michigan,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==14) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Minnesota,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==15) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Missouri,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==16) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_NewJersey,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==17) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_NY,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==18) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_NorthCarolina,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }

        else if(pos==19) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Ohio,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==20) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Oklahoma,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==21) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Oregon,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==22) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Pennsylvania,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==23) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Tennessee,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==24) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Texas,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==25) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Utah,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==26) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Virginia,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==27) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Washington,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==28) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_WashingtonDC,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }
        else if(pos==29) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.citi_Wisconsin,android.R.layout.simple_spinner_item);

            spinner2.setAdapter(adapter);


        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}

