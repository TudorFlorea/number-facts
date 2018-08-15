package dev.tudorflorea.numberfacts.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.data.DisplayFactBuilder;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.database.FactContract;
import dev.tudorflorea.numberfacts.tasks.FactDbAsyncTask;
import dev.tudorflorea.numberfacts.ui.fragments.FactFragment;
import dev.tudorflorea.numberfacts.ui.fragments.FavoriteFactFragment;
import dev.tudorflorea.numberfacts.utilities.Constants;
import dev.tudorflorea.numberfacts.utilities.InterfaceUtils;
import dev.tudorflorea.numberfacts.utilities.PreferencesUtils;

public class FavoriteFactDetailsActivity extends AppCompatActivity implements InterfaceUtils.FactListener, SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.favorite_fact_toolbar) Toolbar mToolbar;
    @BindView(R.id.favorite_fact_adView) AdView mAdView;
    @BindView(R.id.favorite_fact_fragment_frame) FrameLayout mFragmentFrame;
    @BindView(R.id.favorite_fact_fab) FloatingActionButton mFab;

    private final String FAVORITE_FACT_STATE_TAG = "fact_state_tag";
    private final String FAVORITE_FACT_STATE = "fact_state";

    private Fact mFact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferencesUtils.setupSharedPreferences(this, this);
        setContentView(R.layout.activity_favorite_fact_details);

        ButterKnife.bind(this);


        setupToolbar();
        setupAds();
        setupFab();
        extractFact(getIntent());

        if (savedInstanceState == null) {
            DisplayFactBuilder builder = DisplayFactBuilder.withFact(mFact);
            loadFavoriteFactFragment(builder);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite_fact_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_item_action_delete_fact:
                new FactDbAsyncTask(){
                    @Override
                    protected Boolean doInBackground(Void... voids) {

                        int deletedRows = getContentResolver().delete(
                                Uri.withAppendedPath(FactContract.CONTENT_URI, String.valueOf(mFact.getDatabaseId())),
                                null,
                                null
                        );

                        return deletedRows > 0 ? true : false;

                    }

                    @Override
                    protected void onPostExecute(Boolean deletedRow) {
                        if (deletedRow) {
                            Toast.makeText(FavoriteFactDetailsActivity.this, getString(R.string.activity_favorite_fact_delete), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(FavoriteFactDetailsActivity.this, FavoriteFactsActivity.class);
                            startActivity(i);

                        }
                    }
                }.execute();
                break;
                default:
                    return false;
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FAVORITE_FACT_STATE_TAG, FAVORITE_FACT_STATE);
    }

    private void setupAds() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("418203295DEFF5A970AA99210699B6F7") //set your deviceId
                .build();
        mAdView.loadAd(adRequest);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupFab() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, mFact.getText());
                shareIntent.setType(getResources().getString(R.string.main_action_share__intent_type));
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.main_action_share__chooser_heading)));
            }
        });
    }

    private void extractFact(Intent i) {
        try {
            mFact = i.getExtras().getParcelable(Constants.INTENT_FACT_EXTRA);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

    }

    private void loadFavoriteFactFragment(DisplayFactBuilder builder) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.FRAGMENT_ARGS_FACT_BUILDER, builder);
        FavoriteFactFragment factFragment = new FavoriteFactFragment();
        factFragment.setArguments(args);
        replaceFragment(factFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.favorite_fact_fragment_frame, fragment);
        ft.commit();
    }

    @Override
    public void onFactRetrieved(Fact fact) {
        mFact = fact;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PreferencesUtils.onSharedPreferenceChanged(sharedPreferences, key, this);
        recreate();
    }
}
