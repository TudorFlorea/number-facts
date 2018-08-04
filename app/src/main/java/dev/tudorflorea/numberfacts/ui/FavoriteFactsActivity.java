package dev.tudorflorea.numberfacts.ui;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.adapters.FavoriteFactsAdapter;
import dev.tudorflorea.numberfacts.database.FactContract;

public class FavoriteFactsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView mFavoriteFactsRV;
    private final int FAVORITE_FACTS_LOADER_ID = 100;
    private FavoriteFactsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_facts);

        mFavoriteFactsRV = (RecyclerView) findViewById(R.id.favorite_facts_rv);
        mFavoriteFactsRV.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FavoriteFactsAdapter(this, null);

        mFavoriteFactsRV.setAdapter(mAdapter);


        getSupportLoaderManager().restartLoader(FAVORITE_FACTS_LOADER_ID, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
                    return getContentResolver().query(FactContract.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
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
        Log.v("FROM FFA", " FINISHED LOADING");
        Log.v("FROM FFA", cursor.toString());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        Log.v("FROM FFA", " FINISHED Resetting");
    }
}