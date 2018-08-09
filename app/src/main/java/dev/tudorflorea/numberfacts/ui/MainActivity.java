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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import dev.tudorflorea.numberfacts.NumberFactsApplication;
import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.database.FactContract;
import dev.tudorflorea.numberfacts.services.NotificationScheduler;
import dev.tudorflorea.numberfacts.tasks.FactDbAsyncTask;
import dev.tudorflorea.numberfacts.tasks.NotificationTasks;
import dev.tudorflorea.numberfacts.ui.fragments.DateFactFragment;
import dev.tudorflorea.numberfacts.ui.fragments.MathFactFragment;
import dev.tudorflorea.numberfacts.ui.fragments.TriviaFactFragment;
import dev.tudorflorea.numberfacts.ui.fragments.YearFactFragment;
import dev.tudorflorea.numberfacts.utilities.InterfaceUtils;
import dev.tudorflorea.numberfacts.utilities.NotificationUtils;
import dev.tudorflorea.numberfacts.utilities.PreferencesUtils;


public class MainActivity extends AppCompatActivity implements InterfaceUtils.FactListener, SharedPreferences.OnSharedPreferenceChangeListener {


    private Tracker mTracker;
    private BottomNavigationView mBottomNavigationView;
    private final Context mContext = this;
    private Fact mFact;
    private Intent mIntent;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener mDatepikerDialogListener;

    private final int TRIVIA_FRAGMENT_ID = 1;
    private final int MATH_FRAGMENT_ID = 2;
    private final int YEAR_FRAGMENT_ID = 3;
    private final int DATE_FRAGMENT_ID = 4;

    private final String CURRENT_FACT_STATE = "current_fact_state";
    private final String NUMBER_ARG = "number";
    private final String DAY_ARG = "day";
    private final String MOTH_ARG = "month";

    private int mCurrentFragmentID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesUtils.setupSharedPreferences(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mIntent = getIntent();

        setupBottomNavigation();
        setupNavigationDrawer();
        setupDatePickerListener();
        setupAds();
        setupAnalytics();
        setupFloatingActionButton();

        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(CURRENT_FACT_STATE)) {
                mFact = savedInstanceState.getParcelable(CURRENT_FACT_STATE);
            }
        } else {
            loadRandomTriviaFactFragment();
            Log.e("MainActivity", "random fact");
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
                //Toast.makeText(this,"Settings intent", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.action_favorite:

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
                            Toast.makeText(MainActivity.this, "Fact saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "An error occured! Pleas try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();

                break;
            case R.id.action_share:
                Toast.makeText(this,"Share fact", Toast.LENGTH_SHORT).show();
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("Share")
                        .build());

                break;
            default:
                return false;

        }
        return true;
    }

    private void setupBottomNavigation() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_trivia_fact:
                        loadRandomTriviaFactFragment();
                        return true;
                    case R.id.action_math_fact:
                        loadRandomMathFactFragment();
                        return true;
                    case R.id.action_year_fact:
                        loadRandomYearFactFragment();
                        return true;
                    case R.id.action_date_fact:
                        loadRandomDateFactFragment();
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
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_action);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCurrentFragmentID == DATE_FRAGMENT_ID) {
                    new DatePickerDialog(mContext, mDatepikerDialogListener, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH))
                            .show();
                } else {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    switch (mCurrentFragmentID) {
                        case TRIVIA_FRAGMENT_ID:
                            builder.setTitle("Trivia");
                            break;
                        case MATH_FRAGMENT_ID:
                            builder.setTitle("Math");
                            break;
                        case YEAR_FRAGMENT_ID:
                            builder.setTitle("Year");
                            break;
                        case DATE_FRAGMENT_ID:
                            builder.setTitle("Date");
                            break;
                        default:
                            builder.setTitle("Unknown");
                    }

                    builder.setIcon(R.drawable.beer);
                    builder.setView(dialogView);
                    final EditText input = (EditText) dialogView.findViewById(R.id.et_input);
                    builder
                            .setCancelable(true)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (input.getText().toString().equals("")) {
                                                return;
                                            }

                                            try {
                                                int value = Integer.valueOf(input.getText().toString());

                                                switch (mCurrentFragmentID) {

                                                    case TRIVIA_FRAGMENT_ID:
                                                        MainActivity.this.loadTriviaFactFragment(value);
                                                        break;
                                                    case MATH_FRAGMENT_ID:
                                                        MainActivity.this.loadMathFactFragment(value);
                                                        break;
                                                    case YEAR_FRAGMENT_ID:
                                                        MainActivity.this.loadYearFactFragment(value);
                                                        break;
                                                    case DATE_FRAGMENT_ID:

                                                }

                                            } catch (NumberFormatException nfe) {
                                                nfe.printStackTrace();
                                                return;
                                            }


                                        }
                                    })
                            .setNegativeButton("Cancel",
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
                loadDateFactFragment(monthOfYear + 1, dayOfMonth);
            }

        };
    }

    public void setupAds() {
        AdView adView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("418203295DEFF5A970AA99210699B6F7")
                .build();
        adView.loadAd(adRequest);
    }

    public void setupAnalytics() {
        NumberFactsApplication application = (NumberFactsApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    private void loadRandomTriviaFactFragment() {
        TriviaFactFragment fragment = new TriviaFactFragment();
        replaceFragment(fragment);
        mCurrentFragmentID = TRIVIA_FRAGMENT_ID;
    }

    private void loadTriviaFactFragment(int number) {
        Bundle args = new Bundle();
        args.putInt(NUMBER_ARG, number);
        TriviaFactFragment fragment = new TriviaFactFragment();
        fragment.setArguments(args);
        replaceFragment(fragment);

    }

    private void loadRandomMathFactFragment() {
        MathFactFragment fragment = new MathFactFragment();
        replaceFragment(fragment);
        mCurrentFragmentID = MATH_FRAGMENT_ID;
    }

    private void loadMathFactFragment(int number) {
        Bundle args = new Bundle();
        args.putInt(NUMBER_ARG, number);
        MathFactFragment fragment = new MathFactFragment();
        fragment.setArguments(args);
        replaceFragment(fragment);
    }

    private void loadRandomYearFactFragment() {
        YearFactFragment fragment = new YearFactFragment();
        replaceFragment(fragment);
        mCurrentFragmentID = YEAR_FRAGMENT_ID;
    }

    private void loadYearFactFragment(int number) {
        Bundle args = new Bundle();
        args.putInt(NUMBER_ARG, number);
        YearFactFragment fragment = new YearFactFragment();
        fragment.setArguments(args);
        replaceFragment(fragment);
    }

    private void loadRandomDateFactFragment() {
        DateFactFragment fragment = new DateFactFragment();
        replaceFragment(fragment);
        mCurrentFragmentID = DATE_FRAGMENT_ID;
    }

    private void loadDateFactFragment(int month, int day) {
        Bundle args = new Bundle();
        args.putInt(MOTH_ARG, month);
        args.putInt(DAY_ARG, day);
        DateFactFragment fragment = new DateFactFragment();
        fragment.setArguments(args);
        replaceFragment(fragment);
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
