package cmpsc475.example.nhn5026.wikipark;

/**
 * Created by Abhijit on 4/9/2016.
 */

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;


@SuppressWarnings("ConstantConditions")
public class PlacesContentProvider extends ContentProvider {

    private PlacesDB theDB;
    //private FavouriteDB theDB1;

    private static final String AUTHORITY = "com.example.nhn5026.wikipark";
    private static final String BASE_PATH = "places";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +
            BASE_PATH);

  //  private static final String AUTHORITY1 = "com.example.nhn5026.wikipark";
   // private static final String BASE_PATH1 = "favourites";
    //public static final Uri CONTENT_URI1 = Uri.parse("content://" + AUTHORITY1 + "/" +
      //      BASE_PATH1);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int PLACES = 1;
    private static final int PLACES_ID = 2;
   // private static final int FAVOURITES = 3;
    private static final int FAVOURITES_ID = 4;
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, PLACES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/*", PLACES_ID);
//        uriMatcher.addURI(AUTHORITY1, BASE_PATH1, FAVOURITES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FAVOURITES_ID);
    }

    @Override
    public boolean onCreate() {
        theDB = PlacesDB.getInstance(getContext());
//        theDB1 = FavouriteDB.getInstance(getContext());
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long id;
        SQLiteDatabase db = theDB.getWritableDatabase();
  //      SQLiteDatabase db1 = theDB1.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case PLACES:
                id = db.insert("places", null, values);
                break;
            //case FAVOURITES:
              //  id = db1.insert("favourites", null, values);
                //break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
// need to check for id, since its the primary key
        //if(uriMatcher.match(uri)==PLACES)
            return Uri.parse("content://" + AUTHORITY + "/" +BASE_PATH + "/" + id);
        //else{
          //  return Uri.parse("content://" + AUTHORITY1 + "/" +BASE_PATH1 + "/" + id);
        //}

    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = theDB.getReadableDatabase();
      //  SQLiteDatabase db1 = theDB1.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case PLACES:
                cursor = db.query("places", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case PLACES_ID:
                cursor = db.query("places", projection,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs, null, null, sortOrder);
                break;
/*            case FAVOURITES:
                cursor = db1.query("favourites", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case FAVOURITES_ID:
                cursor = db1.query("favourites", projection,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs, null, null, sortOrder);
                break;
                */
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count;
        SQLiteDatabase db = theDB.getWritableDatabase();
        //SQLiteDatabase db1 = theDB1.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case PLACES:
                count = db.update("places", values, selection, selectionArgs);
                break;
            case PLACES_ID:
                count = db.update("places", values,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase db = theDB.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case PLACES:
                count = db.delete("places", selection, selectionArgs);
                break;
            case PLACES_ID:
                count = db.delete("places",
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    private String appendIdToSelection(String selection, String sId) {
        int id = Integer.valueOf(sId);

        if (selection == null || selection.trim().equals(""))
            return "_ID = " + id;
        else
            return selection + " AND _ID = " + id;
    }
}
