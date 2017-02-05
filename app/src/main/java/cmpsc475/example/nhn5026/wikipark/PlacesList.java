    package cmpsc475.example.nhn5026.wikipark;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

    public class PlacesList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

        static int flag=0;
        String place_ids[]= null;
//        static	final	String	SERVER	=	"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
  //      static final String GP_SERVER_KEY="AIzaSyAkuDEKu0HQOqDf_-qUXWOdS08-8hOZZfc";
        static	final	String	TAG	=	"PlacesList";

        private SimpleCursorAdapter mAdapter;
    //    boolean filtered = false;
        AsyncHttpRequest asyncHttpRequest	=	null;
        ArrayList<Double> lat_Lists= new ArrayList<>();
        ArrayList<Double> lng_Lists= new ArrayList<>();
        ArrayList<String> name= new ArrayList<>();

        Bundle extras_bundle;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_places_list);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
            setSupportActionBar(toolbar);
            PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (savedInstanceState != null) {
  //              filtered = savedInstanceState.getBoolean("filtered");
            }

            mAdapter = new SimpleCursorAdapter(this, R.layout.list_item_parksearch_result, null,
                    new String[]{"location","address","price","available_spots"},
                    new int[] {R.id.txtName, R.id.txtAddress,R.id.txtPrice,R.id.txtSpotsAvailable}, 0);

            ListView listView = (ListView) findViewById(R.id.lstPlaces);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);
            //extras_bundle=
            //lat_Lists= getIntent().getSerializableExtra("LatitudeList");
            Intent intent = getIntent();
            lat_Lists = (ArrayList<Double>) intent.getExtras().getSerializable("LatitudeList");
            lng_Lists = (ArrayList<Double>) intent.getExtras().getSerializable("LongitudeList");
                    //extras_bundle.getSerializable("LatitudeList");
            //lng_Lists= extras_bundle.getDoubleArrayList("LongitudeList");
            //name=extras_bundle.getStringArrayList("name");
            //for(int i=0;i<lat_Lists.size();i++){
                getLoaderManager().initLoader(1, null, this);
            //}


            //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

/*            if	(asyncHttpRequest	!=	null)	{
                asyncHttpRequest.cancel(true);
            }*/
            /*
            //for(int i=0;i<lat_Lists.length;i++){
                asyncHttpRequest=new AsyncHttpRequest("GET",	SERVER+lat_Lists[1]+","+lng_Lists[1]+"&radius=50000&key="+GP_SERVER_KEY,null,null);
            //}
            asyncHttpRequest.execute();
*/
        }

        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

Log.i(TAG, lat_Lists.get(0).toString());
            // Create a new CursorLoader with the following query parameters.
            String where = null;
//            if (filtered) {
  //              where = "liked = 'Y'";
            where="latitude BETWEEN "+(lat_Lists.get(0)-0.05)+ " AND " +(lat_Lists.get(0)+0.05);//latitide="//"place="+ place.get(0)+
    //        }
            if(new CursorLoader(this, PlacesContentProvider.CONTENT_URI,
                    new String[]{"_id", "location", "address", "price", "available_spots"}, where, null, null)!=null){
   //             Toast.makeText(this,"data has been read from the db",Toast.LENGTH_LONG);
            }
     //       else
       //         Toast.makeText(this,"data has not been read from the db",Toast.LENGTH_LONG);
            return new CursorLoader(this, PlacesContentProvider.CONTENT_URI,
                    new String[]{"_id", "location", "address", "price", "available_spots"}, where, null, null);
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

            Intent intent = new Intent(PlacesList.this, PlaceInfo.class);
            intent.putExtra("rowid", id);
            startActivity(intent);
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            /*Intent intent = new Intent(PlacesList.this, PlacesInformation.class);//have to check the AddActivity to check how to insert values in the database
            intent.putExtra("rowid", id);
            startActivity(intent);*/
            Intent intent = new Intent(PlacesList.this, PlaceInfo.class);
            intent.putExtra("rowid", id);
            startActivity(intent);
            return true;
        }

        //for dark mode setting
        @Override
        protected void onResume() {
            super.onResume();


            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            boolean changeColor = sharedPref.getBoolean(getString(R.string.pref_filtered_key1), true);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
            RelativeLayout listScreen = (RelativeLayout) findViewById(R.id.placelist);
            ListView listView = (ListView) findViewById(R.id.lstPlaces);



            if(changeColor) {




                listScreen.setBackgroundColor(Color.rgb(105, 105, 105));
                listView.setBackgroundColor(Color.rgb(105,105,105));

               toolbar.setBackgroundColor(Color.rgb(51,51,51));
            }
            else{



                listScreen.setBackgroundColor(Color.rgb(224, 238, 238));
                toolbar.setBackgroundColor(Color.rgb(0,154,205));
                listView.setBackgroundColor(Color.rgb(224,238,238));
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
            if	(asyncHttpRequest	!=	null)	{
                asyncHttpRequest.cancel(true);
            }
        }

        class	AsyncHttpRequest	extends AsyncTask<Void,Void,String> {

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
            protected	String	doInBackground(Void...params) {
                String end_result=null;
                JSONObject result=	JSONViaHttp.get(method, url, queryStringParams, payload);
                if (flag == 0) {
                    try{
                        //end_result= result.getJSONArray("results").getJSONObject(0).getString("place_id");
                        end_result= result.getJSONArray("results").getJSONObject(1).getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                        flag=1;
                    }
                    catch	(JSONException	e)	{
                        Log.e(TAG,	"Error	retrieving	list:	"	+	e.getMessage());
                    }
                }
                else{

                }

                return end_result;
            }


                @Override
                protected	void	onPostExecute(String result)	{
                    super.onPostExecute(result);
                    if	(result	!=	null)	{
                        try	{
                            //((TextView) findViewById(R.id.txtResults)).setText(
                              //      result);
                        }
                        catch	(Exception	e)	{
                            Log.e(TAG,	"Error	retrieving	list:	"	+	e.getMessage());
                        }
                    }
                    else	{
                    //    ((TextView)	findViewById(R.id.txtResults)).setText("Bad	request");
                    }
                    asyncHttpRequest	=	null;
                }

            }

    }










