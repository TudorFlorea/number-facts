package dev.tudorflorea.numberfacts.database;


import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tudor on 2/19/2018.
 */

public class FactContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "dev.tudorflorea.numberfacts";

    public static final String BASE_CONTENT_URI_STRING = "content://dev.tudorflorea.numberfacts";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FACTS = "facts";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FACTS).build();

    public static final class FactEntry implements BaseColumns {
        public static final String TABLE_NAME = "facts";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_FACT = "fact";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_FOUND = "found";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
