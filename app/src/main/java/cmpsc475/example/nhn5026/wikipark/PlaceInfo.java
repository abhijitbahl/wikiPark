package cmpsc475.example.nhn5026.wikipark;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlaceInfo extends AppCompatActivity {


    static Boolean favourite=false;
    long rowid;
    String latitude;
    String longitude;
    String pname;
    ArrayList<Double> lat_Lsts= new ArrayList<Double>();
    ArrayList<Double> lng_Lsts= new ArrayList<Double>();
    Double lat_Lists;
    Double lng_Lists;
    int call_activity;
    final String TAG= "PlacesInfo";
    //ArrayList<Double> lng_Lists= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        call_activity=0;
        call_activity = getIntent().getIntExtra("callingActivity", 0);
        //lat_Lists=getIntent().getStringArrayListExtra("latlist");
        Intent intent = getIntent();
        lat_Lists = intent.getDoubleExtra("latlist", 0);
        lng_Lists = intent.getDoubleExtra("lnglist", 0);
        //lat_Lsts = (ArrayList<Double>) intent.getExtras().getSerializable("latlist");
        //lng_Lsts = (ArrayList<Double>) intent.getExtras().getSerializable("lnglist");
        //lng_Lists=getIntent().getStringArrayListExtra("lnglist");
        intent.getIntExtra("callingActivity", 0);
        if (call_activity == ActivityConstants.ACTIVITY_NEARBY) {

            String where = null;
            where = "latitude=" + lat_Lists + " AND longitude = " + lng_Lists;
            ContentResolver cr2 = getContentResolver();
            Cursor c = cr2.query(PlacesContentProvider.CONTENT_URI,
                    new String[]{"location", "address", "city", "state", "zip", "latitude", "longitude", "available_spots", "price"}, where, null, null);

            if (c.moveToFirst()) {
                pname = c.getString(0);
                ((TextView) findViewById(R.id.txtName1)).setText(c.getString(0));
                ((TextView) findViewById(R.id.txtAddress1)).setText(c.getString(1));
                ((TextView) findViewById(R.id.txtCity1)).setText(c.getString(2));
                ((TextView) findViewById(R.id.txtState1)).setText(c.getString(3));
                ((TextView) findViewById(R.id.txtZip1)).setText(c.getString(4));
                ((TextView) findViewById(R.id.txtSpotsAvailable1)).setText(c.getString(7));
                ((TextView) findViewById(R.id.txtPrice1)).setText(c.getString(8));
                latitude = c.getString(6);
                longitude = c.getString(5);

                c.close();
            } else {
                Log.i(TAG, "in the else");
            }
        }
        //Bundle extras_rowId=getIntent().getExtras();
        if (getIntent().hasExtra("rowid")) {
            rowid = getIntent().getLongExtra("rowid", 0);
            ContentResolver cr = getContentResolver();
            //cr.delete(PlacesContentProvider.CONTENT_URI,null, null);
            //finish();
            Cursor c2 = cr.query(PlacesContentProvider.CONTENT_URI.buildUpon().appendPath(Long.toString(rowid)).build(),
                    new String[]{"location", "address", "city", "state", "zip", "latitude", "longitude", "available_spots", "price"}, null, null, null);

            if (c2.moveToFirst()) {
                pname = c2.getString(0);
                ((TextView) findViewById(R.id.txtName1)).setText(c2.getString(0));
                ((TextView) findViewById(R.id.txtAddress1)).setText(c2.getString(1));
                ((TextView) findViewById(R.id.txtCity1)).setText(c2.getString(2));
                ((TextView) findViewById(R.id.txtState1)).setText(c2.getString(3));
                ((TextView) findViewById(R.id.txtZip1)).setText(c2.getString(4));
                ((TextView) findViewById(R.id.txtSpotsAvailable1)).setText(c2.getString(7));
                ((TextView) findViewById(R.id.txtPrice1)).setText(c2.getString(8));
                latitude = c2.getString(6);
                longitude = c2.getString(5);

                c2.close();
            }
        }
//            return builder.create();


        if (savedInstanceState != null) {
            rowid = savedInstanceState.getInt("rowid");
        }

    }
    @Override
    protected void onResume(){
        super.onResume();





        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean changeColor = sharedPref.getBoolean(getString(R.string.pref_filtered_key1), true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ScrollView parkInfoScreen = (ScrollView) findViewById(R.id.parkInfo);
        LinearLayout buttons = (LinearLayout) findViewById(R.id.buttons);
        ImageButton direction =(ImageButton)findViewById(R.id.button_directions);
        ImageButton fav =(ImageButton)findViewById(R.id.button_favourite);

        TextView txt1 = (TextView) findViewById(R.id.Name1);
        TextView txt2 = (TextView) findViewById(R.id.txtName1);
        TextView txt3 = (TextView) findViewById(R.id.Address1);
        TextView txt4 = (TextView) findViewById(R.id.txtAddress1);
        TextView txt5 = (TextView) findViewById(R.id.City1);
        TextView txt6 = (TextView) findViewById(R.id.txtCity1);
        TextView txt7 = (TextView) findViewById(R.id.State1);
        TextView txt8 = (TextView) findViewById(R.id.txtState1);
        TextView txt9 = (TextView) findViewById(R.id.Zip1);
        TextView txt10 = (TextView) findViewById(R.id.txtZip1);
        TextView txt11 = (TextView) findViewById(R.id.Price1);
        TextView txt12 = (TextView) findViewById(R.id.txtPrice1);
        TextView txt13 = (TextView) findViewById(R.id.Spots1);
        TextView txt14 = (TextView) findViewById(R.id.txtSpotsAvailable1);
        if(changeColor) {
            txt1.setTextColor(Color.rgb(0, 34, 9));
            txt2.setTextColor(Color.rgb(0,34,9));
            txt3.setTextColor(Color.rgb(0, 34, 9));
            txt4.setTextColor(Color.rgb(0, 34, 9));
            txt5.setTextColor(Color.rgb(0,34,9));
            txt6.setTextColor(Color.rgb(0, 34, 9));
            txt7.setTextColor(Color.rgb(0, 34, 9));
            txt8.setTextColor(Color.rgb(0,34,9));
            txt9.setTextColor(Color.rgb(0, 34, 9));
            txt10.setTextColor(Color.rgb(0, 34, 9));
            txt11.setTextColor(Color.rgb(0,34,9));
            txt12.setTextColor(Color.rgb(0, 34, 9));
            txt13.setTextColor(Color.rgb(0, 34, 9));
            txt14.setTextColor(Color.rgb(0,34,9));




            parkInfoScreen.setBackgroundColor(Color.rgb(105, 105, 105));
            buttons.setBackgroundColor(Color.rgb(105, 105, 105));
            direction.setBackgroundColor(Color.rgb(105, 105, 105));
            fav.setBackgroundColor(Color.rgb(105, 105, 105));

            toolbar.setBackgroundColor(Color.rgb(51,51,51));
        }
        else{

            txt1.setTextColor(Color.rgb(0,0,0));
            txt2.setTextColor(Color.rgb(0,0,0));
            txt3.setTextColor(Color.rgb(0,0,0));
            txt4.setTextColor(Color.rgb(0,0,0));
            txt5.setTextColor(Color.rgb(0, 0, 0));
            txt6.setTextColor(Color.rgb(0, 0, 0));
            txt7.setTextColor(Color.rgb(0,0,0));
            txt8.setTextColor(Color.rgb(0,0,0));
            txt9.setTextColor(Color.rgb(0,0,0));
            txt10.setTextColor(Color.rgb(0,0,0));
            txt11.setTextColor(Color.rgb(0,0,0));
            txt12.setTextColor(Color.rgb(0,0,0));
            txt13.setTextColor(Color.rgb(0,0,0));
            txt14.setTextColor(Color.rgb(0,0,0));



            parkInfoScreen.setBackgroundColor(Color.rgb(224, 238, 238));
            buttons.setBackgroundColor(Color.rgb(224,238,238));
            direction.setBackgroundColor(Color.rgb(224,238,238));
            fav.setBackgroundColor(Color.rgb(224,238,238));
            toolbar.setBackgroundColor(Color.rgb(0,154,205));
        }
    }
    public void btnDirection(View view) {
// Create the text message with a string
        //String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
        TextView address = (TextView) findViewById(R.id.txtAddress1);
        TextView city = (TextView) findViewById(R.id.txtCity1);
        String addressText = address.getText().toString();
        String cityText = city.getText().toString();
        //String geoUri = "http://maps.google.com/maps?q=" + addressText + "," + cityText + " (" + pname + ")";
        String geoUri = "http://maps.google.com/maps?q=" + addressText + "," + cityText;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        //context.startActivity(intent);
        //Intent sendIntent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        //sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
        //sendIntent.setType("text/plain");

// Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void btnAddFav(View view) {
        ContentResolver cr = getContentResolver();
        //Cursor c = cr.query(PlacesContentProvider.CONTENT_URI,
          //      new String[]{"location", "address", "city", "state", "zip", "latitude", "longitude", "available_spots", "price"}, null, null, null);

        ContentValues values = new ContentValues();
        /*
        values.put("location", ((TextView) findViewById(R.id.txtName1)).getText().toString());
        values.put("address", ((TextView) findViewById(R.id.txtAddress1)).getText().toString());
        values.put("city", ((TextView) findViewById(R.id.txtCity1)).getText().toString());
        values.put("state", ((TextView) findViewById(R.id.txtState1)).getText().toString());
        values.put("zip", ((TextView) findViewById(R.id.txtZip1)).getText().toString());
        values.put("latitude", ((TextView) findViewById(R.id.txtLatitude1)).getText().toString());
        values.put("longitude", ((TextView) findViewById(R.id.txtLongitude1)).getText().toString());
        values.put("available_spots", ((TextView) findViewById(R.id.txtSpotsAvailable1)).getText().toString());
        values.put("price", ((TextView) findViewById(R.id.txtPrice1)).getText().toString());
*/
        values.put("favourite", "true");
        /*try {


            if (!c.moveToFirst()) {
                cr.insert(FavouriteContentProvider.CONTENT_URI, values);
            }
            //finish();
        } */
        try {
            String where=null;
            where="latitude="+ lat_Lists+" AND longitude = "+lng_Lists;
            if(call_activity!=0){
                cr.update(PlacesContentProvider.CONTENT_URI, values,where , null);
            }
            else{
                cr.update(PlacesContentProvider.CONTENT_URI.buildUpon().appendPath(Long.toString(rowid)).build(), values, null, null);
            }
                //

                //finish();
            }

        catch (SQLException e) {
            Toast.makeText(this, "Error inserting database.", Toast.LENGTH_LONG).show();
        }
        Intent favouriteIntent = new Intent(this, Favorite.class);
        startActivity(favouriteIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_parkinfo, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent1 = new Intent(this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);

                return true;

            case R.id.action_search:


                Intent intent2 = new Intent(this, ParkSearch.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected	void	onPause()	{
        super.onPause();
        //   if	(asyncHttpRequest	!=	null)	{
        //     asyncHttpRequest.cancel(true);
        //}

    }

}