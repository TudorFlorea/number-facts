package dev.tudorflorea.numberfacts.database;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by tudor on 25.02.2018.
 */

public class FactProvider extends ContentProvider {

    public static final int FACTS = 100;
    public static final int FACT_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private FactDbHelper mFactDbHelper;

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FactContract.AUTHORITY, FactContract.PATH_FACTS, FACTS);
        uriMatcher.addURI(FactContract.AUTHORITY, FactContract.PATH_FACTS + "/#", FACT_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFactDbHelper = new FactDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mFactDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor cursor;

        switch (match) {
            case FACTS:
                cursor = db.query(FactContract.FactEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final SQLiteDatabase db = mFactDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case FACTS:
                long id = db.insert(FactContract.FactEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FactContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);


        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        final SQLiteDatabase db = mFactDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int factsDeleted;

        switch (match) {

            case FACT_WITH_ID:
                String id = uri.getPathSegments().get(1);

                factsDeleted = db.delete(FactContract.FactEntry.TABLE_NAME,
                        "_id=?",
                        new String[] {id});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if (factsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return factsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
