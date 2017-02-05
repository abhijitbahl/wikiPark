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
public class FavouriteContentProvider extends ContentProvider {

    private FavouriteDB theDB;

    private static final String AUTHORITY = "com.example.nhn5026.wikipark";
    private static final String BASE_PATH = "favourites";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +
            BASE_PATH);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int FAVOURITES = 1;
    private static final int FAVOURITES_ID = 2;
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, FAVOURITES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FAVOURITES_ID);
    }

    @Override
    public boolean onCreate() {
        theDB = FavouriteDB.getInstance(getContext());
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long id;
        SQLiteDatabase db = theDB.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case FAVOURITES:
                id = db.insert("favourites", null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
// need to check for id, since its the primary key
        return Uri.parse("content://" + AUTHORITY + "/" +BASE_PATH + "/" + id);
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = theDB.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case FAVOURITES:
                cursor = db.query("favourites", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case FAVOURITES_ID:
                cursor = db.query("favourites", projection,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs, null, null, sortOrder);
                break;
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

        switch (uriMatcher.match(uri)){
            case FAVOURITES:
                count = db.update("favourites", values, selection, selectionArgs);
                break;
            case FAVOURITES_ID:
                count = db.update("favourites", values,
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
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase db = theDB.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case FAVOURITES:
                count = db.delete("favourites", selection, selectionArgs);
                break;
            case FAVOURITES_ID:
                count = db.delete("favourites",
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
