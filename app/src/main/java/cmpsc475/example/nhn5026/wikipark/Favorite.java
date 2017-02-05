package cmpsc475.example.nhn5026.wikipark;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;

public class Favorite extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    boolean favourite=false;

    private SimpleCursorAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);



        if (savedInstanceState != null) {
            favourite = savedInstanceState.getBoolean("favourite");
        }

        mAdapter = new SimpleCursorAdapter(this, R.layout.list_items_favourites, null,
                new String[]{"location","address","price","available_spots"},
                new int[] {R.id.txtNameF, R.id.txtAddressF,R.id.txtPriceF,R.id.txtSpotsAvailableF}, 0);

        ListView listView = (ListView) findViewById(R.id.lstPlacesF);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        getLoaderManager().initLoader(1, null, this);

    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String where = null;
            where = "favourite = 'true'";
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

        Intent intent = new Intent(this, PlaceInfoFav.class);
        intent.putExtra("rowid", id);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            /*Intent intent = new Intent(PlacesList.this, PlacesInformation.class);//have to check the AddActivity to check how to insert values in the database
            intent.putExtra("rowid", id);
            startActivity(intent);*/
        Intent intent = new Intent(this, PlaceInfoFav.class);
        intent.putExtra("rowid", id);
        startActivity(intent);
        return true;
    }

    //for dark mode setting
    protected void onResume() {
        super.onResume();


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean changeColor = sharedPref.getBoolean(getString(R.string.pref_filtered_key1), true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RelativeLayout favoriteScreen = (RelativeLayout) findViewById(R.id.favorite);
        ListView listView = (ListView) findViewById(R.id.lstPlacesF);


        if(changeColor) {



            favoriteScreen.setBackgroundColor(Color.rgb(105,105,105));
            listView.setBackgroundColor(Color.rgb(105,105,105));

            toolbar.setBackgroundColor(Color.rgb(51,51,51));
        }
        else{


            favoriteScreen.setBackgroundColor(Color.rgb(224,238,238));
            toolbar.setBackgroundColor(Color.rgb(0,154,205));
            listView.setBackgroundColor(Color.rgb(224,238,238));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favorite, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


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
        }
    }
