package dev.tudorflorea.numberfacts.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.adapters.FavoriteFactsAdapter;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.database.FactContract;
import dev.tudorflorea.numberfacts.services.NotificationScheduler;
import dev.tudorflorea.numberfacts.utilities.Constants;
import dev.tudorflorea.numberfacts.utilities.InterfaceUtils;

public class FavoriteFactsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener, InterfaceUtils.FavoriteFactListener{

    private RecyclerView mFavoriteFactsRV;
    private final int FAVORITE_FACTS_LOADER_ID = 100;
    private final String SELECTION_TAG = "selection";
    private final String SELECTION_ARGS_TAG = "selection_args";
    private final String TRIVIA_FACT_TYPE = "trivia";
    private final String MATH_FACT_TYPE = "math";
    private final String YEAR_FACT_TYPE = "year";
    private final String DATE_FACT_TYPE = "date";
    private FavoriteFactsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupSharedPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_facts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.favorite_facts_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFavoriteFactsRV = (RecyclerView) findViewById(R.id.favorite_facts_rv);
        mFavoriteFactsRV.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FavoriteFactsAdapter(this, null, this);

        mFavoriteFactsRV.setAdapter(mAdapter);


        getSupportLoaderManager().restartLoader(FAVORITE_FACTS_LOADER_ID, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mFactData = null;

            @Override
            protected void onStartLoading() {
                if (mFactData != null) {
                    deliverResult(mFactData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    if (args != null) {

                            String selection = args.getString(SELECTION_TAG);
                            String[] selectionArgs = args.getStringArray(SELECTION_ARGS_TAG);

                            return getContentResolver().query(FactContract.CONTENT_URI,
                                    null,
                                    selection,
                                    selectionArgs,
                                    null);

                    } else {
                        return getContentResolver().query(FactContract.CONTENT_URI,
                                null,
                                null,
                                null,
                                null);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mFactData = data;
                super.deliverResult(data);
            }
        };
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite_facts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        Bundle args;
        switch (itemId) {

            case R.id.action_sort_trivia:
                args = buildArgs(TRIVIA_FACT_TYPE);
                getSupportLoaderManager().restartLoader(FAVORITE_FACTS_LOADER_ID, args, this);
                break;

            case R.id.action_sort_math:
                args = buildArgs(MATH_FACT_TYPE);
                getSupportLoaderManager().restartLoader(FAVORITE_FACTS_LOADER_ID, args, this);
                break;

            case R.id.action_sort_year:
                args = buildArgs(YEAR_FACT_TYPE);
                getSupportLoaderManager().restartLoader(FAVORITE_FACTS_LOADER_ID, args, this);
                break;

            case R.id.action_sort_date:
                args = buildArgs(DATE_FACT_TYPE);
                getSupportLoaderManager().restartLoader(FAVORITE_FACTS_LOADER_ID, args, this);
                break;

            case R.id.action_sort_clear:
                getSupportLoaderManager().restartLoader(FAVORITE_FACTS_LOADER_ID, null, this);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getResources().getString(R.string.pref_notifications_key))) {
            boolean isNotificationOn = sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.pref_notifications_default_value));
            if (isNotificationOn) {
                NotificationScheduler.scheduleFactNotification(this);
            } else {
                NotificationScheduler.cancelFactNotification(this);
            }
        }

        if (key.equals(getResources().getString(R.string.pref_theme_key))) {

            String theme = sharedPreferences.getString(key, getResources().getString(R.string.pref_theme_default_value));


            switch (theme) {

                case "green":
                    setTheme(R.style.AppTheme_Green);
                    recreate();
                    break;
                case "red":
                    setTheme(R.style.AppTheme_Red);
                    recreate();
                    break;

                case "blue":
                    setTheme(R.style.AppTheme_Blue);
                    recreate();
                    break;

                default:
                    setTheme(R.style.AppTheme_Green);
            }
        }
    }

    public void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if (sharedPreferences.getBoolean(getResources().getString(R.string.pref_notifications_key), getResources().getBoolean(R.bool.pref_notifications_default_value))) {
            NotificationScheduler.scheduleFactNotification(this);
        }

        String theme = sharedPreferences.getString(getResources().getString(R.string.pref_theme_key), getResources().getString(R.string.pref_theme_default_value));

        switch (theme) {

            case "green":
                setTheme(R.style.AppTheme_Green);
                break;
            case "red":
                setTheme(R.style.AppTheme_Red);
                break;

            case "blue":
                setTheme(R.style.AppTheme_Blue);
                break;

            default:
                setTheme(R.style.AppTheme_Green);
        }

    }

    private Bundle buildArgs(String factType) {
        String selection = FactContract.FactEntry.COLUMN_TYPE + "=?";
        String[] selectionArgs = {factType};
        Bundle bundle = new Bundle();
        bundle.putString(SELECTION_TAG, selection);
        bundle.putStringArray(SELECTION_ARGS_TAG, selectionArgs);

        return bundle;
    }

    @Override
    public void onFavoriteFactClick(Fact fact, Class activity) {
        Intent i = new Intent(this, activity);
        i.putExtra(Constants.INTENT_FACT_EXTRA, fact);
        startActivity(i);
    }
}