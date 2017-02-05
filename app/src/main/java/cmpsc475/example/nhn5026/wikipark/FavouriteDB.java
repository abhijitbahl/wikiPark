package cmpsc475.example.nhn5026.wikipark;

/**
 * Created by Abhijit on 4/9/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FavouriteDB extends SQLiteOpenHelper {

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
    public interface OnDBReadyListener {
        public void onDBReady(SQLiteDatabase theDB);
    }

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "favourite.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE favourites (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "location TEXT, " +
                    "address TEXT, " +
                    "city TEXT," +
                    "state TEXT," +
                    "zip TEXT," +
                    "latitude TEXT," +
                    "longitude TEXT," +
                    "recommendations INTEGER," +
                    "available_spots INTEGER," +
                    "price REAL)";
    //" UNIQUE (latitude, longitude) ON CONFLICT REPLACE)";
//have to modify the create table and make laitude and longitude the primary keys. Have to add this "price REAL," +
    //"PRIMARY KEY (latitude, longitude))";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS favourites";

    private static FavouriteDB theDb;
    private Context appContext;

    private FavouriteDB(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        appContext = context.getApplicationContext();
    }

    public static synchronized FavouriteDB getInstance(Context context) {
        if (theDb == null) {
            theDb = new FavouriteDB(context.getApplicationContext());
        }

        return theDb;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

        db.beginTransaction();
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
