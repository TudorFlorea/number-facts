package dev.tudorflorea.numberfacts.ui;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.services.NotificationScheduler;
import dev.tudorflorea.numberfacts.utilities.PreferencesUtils;


public class SettingsActivity extends AppCompatActivity implements  SharedPreferences.OnSharedPreferenceChangeListener{

    @BindView(R.id.settings_toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesUtils.setupSharedPreferences(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PreferencesUtils.onSharedPreferenceChanged(sharedPreferences, key, this);
        recreate();
    }



}
