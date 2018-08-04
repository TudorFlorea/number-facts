package dev.tudorflorea.numberfacts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import dev.tudorflorea.numberfacts.database.FactContract.FactEntry;

/**
 * Created by Tudor on 2/19/2018.
 */

public class FactDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "facts.db";

    private static final int DATABASE_VERSION = 1;

    public FactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FACTS_TABLE = "CREATE TABLE " + FactEntry.TABLE_NAME + " (" +
                FactEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FactEntry.COLUMN_NUMBER + " TEXT NOT NULL, " +
                FactEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                FactEntry.COLUMN_FACT + " TEXT NOT NULL, " +
                FactEntry.COLUMN_FOUND + " TINYINT NOT NULL, " +
                FactEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FactEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
