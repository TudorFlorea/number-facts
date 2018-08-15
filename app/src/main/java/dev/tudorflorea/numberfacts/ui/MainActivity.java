package dev.tudorflorea.numberfacts.ui;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import java.util.Calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.tudorflorea.numberfacts.NumberFactsApplication;
import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.data.DisplayFactBuilder;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.database.FactContract;
import dev.tudorflorea.numberfacts.tasks.FactDbAsyncTask;
import dev.tudorflorea.numberfacts.ui.fragments.FactFragment;
import dev.tudorflorea.numberfacts.utilities.Constants;
import dev.tudorflorea.numberfacts.utilities.InterfaceUtils;
import dev.tudorflorea.numberfacts.utilities.InternetUtils;
import dev.tudorflorea.numberfacts.utilities.NetworkUtils;
import dev.tudorflorea.numberfacts.utilities.PreferencesUtils;


public class MainActivity extends AppCompatActivity implements InterfaceUtils.FactListener, SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;
    @BindView(R.id.new_number_fab) FloatingActionButton mNewNumberFab;

    private Tracker mTracker;
    private final Context mContext = this;
    private Fact mFact;
    private Intent mIntent;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener mDatepikerDialogListener;

    private final String CURRENT_FACT_STATE = "current_fact_state";
    private final String CURRENT_FACT_TYPE_STATE_KEY = "current_fact_type_state_key";


    private int mCurrentFactType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesUtils.setupSharedPreferences(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mIntent = getIntent();

        setupBottomNavigation();
        setupNavigationDrawer();
        setupDatePickerListener();
        setupAds();
        setupAnalytics();
        setupFloatingActionButton();

        if (savedInstanceState != null) {
                mFact = savedInstanceState.getParcelable(CURRENT_FACT_STATE);
                mCurrentFactType = savedInstanceState.getInt(CURRENT_FACT_TYPE_STATE_KEY);

        } else {

            DisplayFactBuilder builder;


            if (mIntent.hasExtra(getString(R.string.intent_fact_extra))) {
                mFact = mIntent.getExtras().getParcelable(Constants.INTENT_FACT_EXTRA);
                builder = DisplayFactBuilder.withFact(mFact);
                loadFactFragment(builder);
            } else {
                builder = DisplayFactBuilder.queryRandom(DisplayFactBuilder.QUERY_RANDOM_TRIVIA);
                loadFactFragment(builder);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(getString(R.string.intent_fact_extra))) {
            try {
                mFact = intent.getExtras().getParcelable(getString(R.string.intent_fact_extra));

                DisplayFactBuilder builder = DisplayFactBuilder.withFact(mFact);
                mCurrentFactType = builder.getQueryType();
                loadFactFragment(builder);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.action_favorite:

                if (InternetUtils.isNetworkAvailable(this)) {
                    new FactDbAsyncTask() {

                        @Override
                        protected Boolean doInBackground(Void... voids) {

                            ContentValues cv = new ContentValues();

                            cv.put(FactContract.FactEntry.COLUMN_FACT, mFact.getText());
                            cv.put(FactContract.FactEntry.COLUMN_NUMBER, mFact.getNumber());
                            int found = mFact.isFound() ? 1 : 0;
                            cv.put(FactContract.FactEntry.COLUMN_FOUND, found);
                            cv.put(FactContract.FactEntry.COLUMN_TYPE, mFact.getType());

                            Uri insertedUri = getContentResolver().insert(FactContract.CONTENT_URI, cv);
                            int lastPathSegment = Integer.valueOf(insertedUri.getLastPathSegment());

                            if (lastPathSegment != -1) {
                                return true;
                            } else {
                                return false;
                            }

                        }

                        @Override
                        protected void onPostExecute(Boolean factInserted) {
                            if (factInserted) {
                                Toast.makeText(MainActivity.this, getString(R.string.main_activity_fact_saved), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, getString(R.string.main_activity_fact_error_while_saving), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();
                } else {
                    Toast.makeText(this, getString(R.string.err_no_network_available), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.action_share:
                if (InternetUtils.isNetworkAvailable(this)) {
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory(getResources().getString(R.string.analytics_category))
                            .setAction(getResources().getString(R.string.analytics_action))
                            .build());

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, mFact.getText());
                    shareIntent.setType(getResources().getString(R.string.main_action_share__intent_type));
                    startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.main_action_share__chooser_heading)));
                } else {
                    Toast.makeText(this, getString(R.string.err_no_network_available), Toast.LENGTH_LONG).show();
                }

                break;
            default:
                return false;

        }
        return true;
    }

    private void setupBottomNavigation() {

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                DisplayFactBuilder builder;

                switch (item.getItemId()) {
                    case R.id.action_trivia_fact:
                        builder = DisplayFactBuilder.queryRandom(DisplayFactBuilder.QUERY_RANDOM_TRIVIA);
                        loadFactFragment(builder);
                        return true;
                    case R.id.action_math_fact:
                        builder = DisplayFactBuilder.queryRandom(DisplayFactBuilder.QUERY_RANDOM_MATH);
                        loadFactFragment(builder);
                        return true;
                    case R.id.action_year_fact:
                        builder = DisplayFactBuilder.queryRandom(DisplayFactBuilder.QUERY_RANDOM_YEAR);
                        loadFactFragment(builder);
                        return true;
                    case R.id.action_date_fact:
                        builder = DisplayFactBuilder.queryRandom(DisplayFactBuilder.QUERY_RANDOM_DATE);
                        loadFactFragment(builder);
                        return true;
                }

                return false;
            }
        });
    }

    public void setupNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.drawer_action_favorite:
                        Intent favoriteIntent = new Intent(MainActivity.this, FavoriteFactsActivity.class);
                        startActivity(favoriteIntent);
                        break;
                    case R.id.drawer_action_settings:
                        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.drawer_action_about:
                        Toast.makeText(MainActivity.this, "About the app", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.drawer_action_rate:
                        Toast.makeText(MainActivity.this, "Will be implemented when the app is on google play", Toast.LENGTH_SHORT).show();
                        break;
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public void setupFloatingActionButton() {
        mNewNumberFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCurrentFactType == DisplayFactBuilder.QUERY_RANDOM_DATE || mCurrentFactType == DisplayFactBuilder.QUERY_DATE_NUMBER) {
                    new DatePickerDialog(mContext, mDatepikerDialogListener, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH))
                            .show();
                } else {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    TextView dialogText = dialogView.findViewById(R.id.number_dialog_tv);
                    switch (mCurrentFactType) {
                        case DisplayFactBuilder.QUERY_RANDOM_TRIVIA:
                            builder.setTitle(getString(R.string.number_picker_trivia_title));
                            dialogText.setText(getString(R.string.number_picker_trivia_text));
                            builder.setIcon(R.drawable.ic_brain_48);
                            break;
                        case DisplayFactBuilder.QUERY_TRIVIA_NUMBER:
                            builder.setTitle(getString(R.string.number_picker_trivia_title));
                            dialogText.setText(getString(R.string.number_picker_trivia_text));
                            builder.setIcon(R.drawable.ic_brain_48);
                            break;
                        case DisplayFactBuilder.QUERY_RANDOM_MATH:
                            builder.setTitle(getString(R.string.number_picker_math_title));
                            dialogText.setText(getString(R.string.number_picker_math_text));
                            builder.setIcon(R.drawable.ic_pi_48);
                            break;
                        case DisplayFactBuilder.QUERY_MATH_NUMBER:
                            builder.setTitle(getString(R.string.number_picker_math_title));
                            dialogText.setText(getString(R.string.number_picker_math_text));
                            builder.setIcon(R.drawable.ic_pi_48);
                            break;
                        case DisplayFactBuilder.QUERY_RANDOM_YEAR:
                            builder.setTitle(getString(R.string.number_picker_year_title));
                            dialogText.setText(getString(R.string.number_picker_year_text));
                            builder.setIcon(R.drawable.ic_hourglass_48);
                            break;
                        case DisplayFactBuilder.QUERY_YEAR_NUMBER:
                            builder.setTitle(getString(R.string.number_picker_year_title));
                            dialogText.setText(getString(R.string.number_picker_year_text));
                            builder.setIcon(R.drawable.ic_hourglass_48);
                            break;
                        default:
                            builder.setTitle(getString(R.string.number_picker_unknown_title));
                    }

                    builder.setView(dialogView);
                    final EditText input = (EditText) dialogView.findViewById(R.id.number_dialog_et);
                    builder
                            .setCancelable(true)
                            .setPositiveButton(getString(R.string.number_picker_dialog_ok),
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (input.getText().toString().trim().equals("")) {
                                                return;
                                            }

                                            try {
                                                int value = Integer.valueOf(input.getText().toString().trim());
                                                DisplayFactBuilder numberBuilder;
                                                switch (mCurrentFactType) {

                                                    case DisplayFactBuilder.QUERY_TRIVIA_NUMBER:
                                                        numberBuilder = DisplayFactBuilder.queryNumber(value, DisplayFactBuilder.QUERY_TRIVIA_NUMBER);
                                                        MainActivity.this.loadFactFragment(numberBuilder);
                                                        break;
                                                    case DisplayFactBuilder.QUERY_RANDOM_TRIVIA:
                                                        numberBuilder = DisplayFactBuilder.queryNumber(value, DisplayFactBuilder.QUERY_TRIVIA_NUMBER);
                                                        MainActivity.this.loadFactFragment(numberBuilder);
                                                        break;
                                                    case DisplayFactBuilder.QUERY_MATH_NUMBER:
                                                        numberBuilder = DisplayFactBuilder.queryNumber(value, DisplayFactBuilder.QUERY_MATH_NUMBER);
                                                        MainActivity.this.loadFactFragment(numberBuilder);
                                                        break;
                                                    case DisplayFactBuilder.QUERY_RANDOM_MATH:
                                                        numberBuilder = DisplayFactBuilder.queryNumber(value, DisplayFactBuilder.QUERY_MATH_NUMBER);
                                                        MainActivity.this.loadFactFragment(numberBuilder);
                                                        break;
                                                    case DisplayFactBuilder.QUERY_YEAR_NUMBER:
                                                        numberBuilder = DisplayFactBuilder.queryNumber(value, DisplayFactBuilder.QUERY_YEAR_NUMBER);
                                                        MainActivity.this.loadFactFragment(numberBuilder);
                                                        break;
                                                    case DisplayFactBuilder.QUERY_RANDOM_YEAR:
                                                        numberBuilder = DisplayFactBuilder.queryNumber(value, DisplayFactBuilder.QUERY_YEAR_NUMBER);
                                                        MainActivity.this.loadFactFragment(numberBuilder);
                                                        break;
                                                }

                                            } catch (NumberFormatException nfe) {
                                                nfe.printStackTrace();
                                            }


                                        }
                                    })
                            .setNegativeButton(getString(R.string.number_picker_dialog_cancel),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }



            }
        });
    }

    public void setupDatePickerListener() {
        mDatepikerDialogListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DisplayFactBuilder builder = DisplayFactBuilder.queryDate(dayOfMonth, monthOfYear + 1, DisplayFactBuilder.QUERY_DATE_NUMBER);
                loadFactFragment(builder);
            }

        };
    }

    public void setupAds() {
        AdView adView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
    }

    public void setupAnalytics() {
        NumberFactsApplication application = (NumberFactsApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    private void loadFactFragment(DisplayFactBuilder builder) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.FRAGMENT_ARGS_FACT_BUILDER, builder);
        FactFragment factFragment = new FactFragment();
        factFragment.setArguments(args);
        mCurrentFactType = builder.getQueryType();
        replaceFragment(factFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mFact != null) outState.putParcelable(CURRENT_FACT_STATE, mFact);

        outState.putInt(CURRENT_FACT_TYPE_STATE_KEY, mCurrentFactType);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
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
