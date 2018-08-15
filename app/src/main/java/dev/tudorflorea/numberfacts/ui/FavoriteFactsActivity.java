package dev.tudorflorea.numberfacts.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.adapters.FavoriteFactsCursorAdapter;
import dev.tudorflorea.numberfacts.adapters.FavoriteFactsListAdapter;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.data.FactFactory;
import dev.tudorflorea.numberfacts.database.FactContract;
import dev.tudorflorea.numberfacts.services.NotificationScheduler;
import dev.tudorflorea.numberfacts.utilities.Constants;
import dev.tudorflorea.numberfacts.utilities.InterfaceUtils;
import dev.tudorflorea.numberfacts.utilities.PreferencesUtils;

public class FavoriteFactsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener, InterfaceUtils.FavoriteFactListener{

    private final int FAVORITE_FACTS_LOADER_ID = 100;
    private final String SELECTION_TAG = "selection";
    private final String SELECTION_ARGS_TAG = "selection_args";
    private final String TRIVIA_FACT_TYPE = "trivia";
    private final String MATH_FACT_TYPE = "math";
    private final String YEAR_FACT_TYPE = "year";
    private final String DATE_FACT_TYPE = "date";
    private final String FACTS_STATE_KEY = "facts_state";
    private final String RECYCLER_VIEW_STATE_KEY = "recycler_view_state";

    private ArrayList<Fact> mFacts;
    private Parcelable mRecyclerViewState;
    private LinearLayoutManager mFavoriteFactsLayoutManager;

    private FavoriteFactsCursorAdapter mCursorAdapter;
    private FavoriteFactsListAdapter mListAdapter;

    @BindView(R.id.favorite_facts_toolbar) Toolbar mToolbar;
    @BindView(R.id.favorite_facts_rv) RecyclerView mFavoriteFactsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupSharedPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_facts);

        ButterKnife.bind(this);

        setupToolbar();

        if (savedInstanceState != null) {
            mFavoriteFactsRV.setHasFixedSize(true);
            mFavoriteFactsLayoutManager = new LinearLayoutManager(this);
            mFavoriteFactsRV.setLayoutManager(mFavoriteFactsLayoutManager);
            mFacts = savedInstanceState.getParcelableArrayList(FACTS_STATE_KEY);
            mListAdapter = new FavoriteFactsListAdapter(this, mFacts, this);
            mFavoriteFactsRV.setAdapter(mListAdapter);

        } else {
            mFavoriteFactsRV.setHasFixedSize(true);
            mFavoriteFactsLayoutManager = new LinearLayoutManager(this);
            mFavoriteFactsRV.setLayoutManager(mFavoriteFactsLayoutManager);
            mCursorAdapter = new FavoriteFactsCursorAdapter(this, null, this);
            mFavoriteFactsRV.setAdapter(mCursorAdapter);
            getSupportLoaderManager().restartLoader(FAVORITE_FACTS_LOADER_ID, null, this);
        }


    }

    private void setupToolbar() {
        if (mToolbar != null) {
            try {
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(FACTS_STATE_KEY, mFacts);

        mRecyclerViewState = mFavoriteFactsLayoutManager.onSaveInstanceState();

        outState.putParcelable(RECYCLER_VIEW_STATE_KEY, mRecyclerViewState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mRecyclerViewState = savedInstanceState.getParcelable(RECYCLER_VIEW_STATE_KEY);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mFactData;

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
        mFacts = FactFactory.listFromCursor(cursor);
        mCursorAdapter.swapCursor(cursor);
        mFavoriteFactsRV.setAdapter(mCursorAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        PreferencesUtils.onSharedPreferenceChanged(sharedPreferences, key, this);

    }

    public void setupSharedPreferences() {

        PreferencesUtils.setupSharedPreferences(this, this);

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