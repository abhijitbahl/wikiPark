package cmpsc475.example.nhn5026.wikipark;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

//imports for google maps api, might need to add them to another class

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);



    }
    //for dark mode setting
    protected void onResume()
    {
        super.onResume();





        String uriNearby = "@drawable/nearby";
        String uriSearch = "@drawable/search";
        String uriFavorite = "@drawable/favorite";
        String uriSetting = "@drawable/setting";
        //String uriImage = "@drawable/main2";
        int nearby = getResources().getIdentifier(uriNearby, null, getPackageName());
        int search = getResources().getIdentifier(uriSearch, null, getPackageName());
        int favorite = getResources().getIdentifier(uriFavorite, null, getPackageName());
        int setting = getResources().getIdentifier(uriSetting, null, getPackageName());
        //int image = getResources().getIdentifier(uriImage, null, getPackageName());


        String uriNearby2 = "@drawable/nearby2";
        String uriSearch2 = "@drawable/search2";
        String uriFavorite2 = "@drawable/favorite2";
        String uriSetting2 = "@drawable/setting2";
       // String uriImage2 = "@drawable/main3";
        int nearby2 = getResources().getIdentifier(uriNearby2, null, getPackageName());
        int search2 = getResources().getIdentifier(uriSearch2, null, getPackageName());
        int favorite2 = getResources().getIdentifier(uriFavorite2, null, getPackageName());
        int setting2 = getResources().getIdentifier(uriSetting2, null, getPackageName());
        //int image2 = getResources().getIdentifier(uriImage2, null, getPackageName());




        Drawable resNearby = getResources().getDrawable(nearby);
        Drawable resSearch = getResources().getDrawable(search);
        Drawable resFavorite = getResources().getDrawable(favorite);
        Drawable resSetting = getResources().getDrawable(setting);
        //Drawable resImage = getResources().getDrawable(image);

        Drawable resNearby2 = getResources().getDrawable(nearby2);
        Drawable resSearch2 = getResources().getDrawable(search2);
        Drawable resFavorite2 = getResources().getDrawable(favorite2);
        Drawable resSetting2 = getResources().getDrawable(setting2);
       // Drawable resImage2 = getResources().getDrawable(image2);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean changeColor = sharedPref.getBoolean(getString(R.string.pref_filtered_key1), true);
        RelativeLayout mainScreen = (RelativeLayout) findViewById(R.id.main);
        ImageButton btn1 = (ImageButton) findViewById(R.id.button_nearby);
        ImageButton btn2 = (ImageButton) findViewById(R.id.button_search);
        ImageButton btn3 = (ImageButton) findViewById(R.id.button_favorite);
        ImageButton btn4 = (ImageButton) findViewById(R.id.button_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView pic = (ImageView) findViewById(R.id.image);

        if(changeColor) {

            mainScreen.setBackgroundColor(Color.rgb(105,105,105));
            btn1.setBackgroundColor(Color.rgb(105,105,105));
            btn2.setBackgroundColor(Color.rgb(105,105,105));
            btn3.setBackgroundColor(Color.rgb(105, 105, 105));
            btn4.setBackgroundColor(Color.rgb(105, 105, 105));
            btn1.setImageDrawable(resNearby2);
            btn2.setImageDrawable(resSearch2);
            btn3.setImageDrawable(resFavorite2);
            btn4.setImageDrawable(resSetting2);
            toolbar.setBackgroundColor(Color.rgb(51, 51, 51));

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                //Do some stuff
                pic.setImageResource(R.drawable.main3);

            }


            //parkSearchScreen.setBackgroundColor(Color.rgb(68, 219, 98));
        }
        else{
            mainScreen.setBackgroundColor(Color.rgb(224,238,238));
            btn1.setBackgroundColor(Color.rgb(224, 238, 238));
            btn2.setBackgroundColor(Color.rgb(224, 238, 238));
            btn3.setBackgroundColor(Color.rgb(224, 238, 238));
            btn4.setBackgroundColor(Color.rgb(224, 238, 238));
            btn1.setImageDrawable(resNearby);
            btn2.setImageDrawable(resSearch);
            btn3.setImageDrawable(resFavorite);
            btn4.setImageDrawable(resSetting);
            toolbar.setBackgroundColor(Color.rgb(0, 154, 205));

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                //Do some stuff
                pic.setImageResource(R.drawable.main1);

            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void search(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ParkSearch.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void favorite(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Favorite.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void settings(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Settings.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    public void nearbyPark(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, NearbyPlaces.class);
        //intent.putExtra("calling-activity", ActivityConstants.ACTIVITY_1);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
