package cmpsc475.example.nhn5026.wikipark;

/**
 * Created by Abhijit on 4/9/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class PlacesDB extends SQLiteOpenHelper {

    private static final String LOG_TAG = "PlacesDb";
    private String[] lat_Lists;
    String[] lng_Lists;
    String[] loc_name;
    String[] address;
    String[] city;
    String[] state;
    String[] zip;
    String[] recommendation;
    String[] av_spots;
    String[] price;
    String[] favourite;
    public interface OnDBReadyListener {
        public void onDBReady(SQLiteDatabase theDB);
    }

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "place.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE places (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "location TEXT, " +
                    "address TEXT, " +
                    "city TEXT," +
                    "state TEXT," +
                    "zip TEXT," +
                    "latitude REAL," +
                    "longitude REAL," +
                    "recommendations INTEGER," +
                    "available_spots INTEGER," +
                    "price TEXT," +
                    "favourite TEXT)";
                    //"place TEXT)";
                    //" UNIQUE (latitude, longitude) ON CONFLICT REPLACE)";
//have to modify the create table and make laitude and longitude the primary keys. Have to add this "price REAL," +
    //"PRIMARY KEY (latitude, longitude))";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS places";

    private static PlacesDB theDb;
    private Context appContext;

    private PlacesDB(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        appContext = context.getApplicationContext();
    }

    public static synchronized PlacesDB getInstance(Context context) {
        if (theDb == null) {
            theDb = new PlacesDB(context.getApplicationContext());
        }

        return theDb;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

        //lat_Lists= ParkSearch.lat_Lists;
        //lng_Lists=ParkSearch.lng_Lists;
        loc_name=ParkSearch.loc_name;
        address=ParkSearch.address;
        city=ParkSearch.city;
        state=ParkSearch.state;
        zip=ParkSearch.zip;
        recommendation=ParkSearch.recommendation;
        av_spots=ParkSearch.av_spots;
        price=ParkSearch.price;

        System.out.println(price[0]);
        db.beginTransaction();
        /*ContentValues values = new ContentValues();
        values.put("favourite","false");
        for (int i = 0; i < price.length; i++) {
            values.put("location", loc_name[i]);
            values.put("address", address[i]);
            values.put("city", city[i]);
            values.put("state", state[i]);
            values.put("zip", zip[i]);
            values.put("latitude", lat_Lists[i+1]);
            values.put("longitude", lng_Lists[i+1]);
            values.put("recommendations", recommendation[i]);
            values.put("available_spots", av_spots[i]);
            values.put("price", price[i]);
            values.put("favourite", "false");
            db.insert("places", null, values);
            //db.insert("favourite", null, values);
        }*/
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
