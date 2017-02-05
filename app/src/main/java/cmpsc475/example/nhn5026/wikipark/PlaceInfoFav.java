package cmpsc475.example.nhn5026.wikipark;

/**
 * Created by nhn5026 on 4/26/16.
 */

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class PlaceInfoFav extends AppCompatActivity {


    static Boolean favourite=false;
    long rowid;
    String latitude;
    String longitude;
    String pname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_infofav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        ImageButton button = (ImageButton) findViewById(R.id.button_delete);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse(PlacesContentProvider.CONTENT_URI + "/" + rowid);
                getContentResolver().delete(uri,null, null);
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //Bundle extras_rowId=getIntent().getExtras();
        if (getIntent().hasExtra("rowid")) {
            rowid = getIntent().getLongExtra("rowid", 0);
            ContentResolver cr = getContentResolver();
            //cr.delete(PlacesContentProvider.CONTENT_URI,null, null);
            //finish();
            Cursor c = cr.query(PlacesContentProvider.CONTENT_URI.buildUpon().appendPath(Long.toString(rowid)).build(),
                    new String[]{"location", "address", "city", "state", "zip", "latitude", "longitude", "available_spots", "price"}, null, null, null);

            if (c.moveToFirst()) {
                pname=c.getString(0);
                ((TextView) findViewById(R.id.txtName1)).setText(c.getString(0));
                ((TextView) findViewById(R.id.txtAddress1)).setText(c.getString(1));
                ((TextView) findViewById(R.id.txtCity1)).setText(c.getString(2));
                ((TextView) findViewById(R.id.txtState1)).setText(c.getString(3));
                ((TextView) findViewById(R.id.txtZip1)).setText(c.getString(4));
                ((TextView) findViewById(R.id.txtSpotsAvailable1)).setText(c.getString(7));
                ((TextView) findViewById(R.id.txtPrice1)).setText(c.getString(8));
                latitude=c.getString(6);
                longitude=c.getString(5);

                c.close();
            }
        }
//            return builder.create();


        if (savedInstanceState != null) {
            favourite = savedInstanceState.getBoolean("favourite");
        }
/*
            mAdapter = new SimpleCursorAdapter(this, R.layout.list_item_parksearch_result, null,
                    new String[]{"location","address","price","available_spots","recommendations"},
                    new int[] {R.id.txtName, R.id.txtAddress,R.id.txtPrice,R.id.txtSpotsAvailable,R.id.txtReview}, 0);
*
            ListView listView = (ListView) findViewById(R.id.lstPlaces);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);
*/
//            getLoaderManager().initLoader(1, null, this);
    }
/*
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {


            // Create a new CursorLoader with the following query parameters.
            String where = null;
            //            if (filtered) {
            //              where = "liked = 'Y'";
            //        }
            return new CursorLoader(this, PlacesContentProvider.CONTENT_URI,
                    new String[]{"_id", "location", "address", "city", "state", "zip", "latitude", "longitude", "available_spots", "price"}, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            mAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //DisplaySetupDialog setupDialog = new DisplaySetupDialog();
            //Bundle args = new Bundle();
            //args.putLong("rowid", id);

            // setupDialog.setArguments(args);
            //setupDialog.show(getFragmentManager(), "setupDialog");

            //        Intent intent = new Intent(PlacesList.this, PlaceInfo.class);
            //      intent.putExtra("rowid", id);
            //    startActivity(intent);
        }
*/
    //      @Override
    //    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                /*Intent intent = new Intent(PlacesList.this, PlacesInformation.class);//have to check the AddActivity to check how to insert values in the database
                intent.putExtra("rowid", id);
                startActivity(intent);*/
    //      return true;
    //}

    //for dark mode setting
    @Override
    protected void onResume(){
        super.onResume();





        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean changeColor = sharedPref.getBoolean(getString(R.string.pref_filtered_key1), true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ScrollView parkInfoScreen = (ScrollView) findViewById(R.id.parkInfo);
        LinearLayout buttons = (LinearLayout) findViewById(R.id.buttons);
        ImageButton direction =(ImageButton)findViewById(R.id.button_directions);
        ImageButton del =(ImageButton)findViewById(R.id.button_delete);

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
            del.setBackgroundColor(Color.rgb(105, 105, 105));

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
            del.setBackgroundColor(Color.rgb(224, 238, 238));
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gotohome, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

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