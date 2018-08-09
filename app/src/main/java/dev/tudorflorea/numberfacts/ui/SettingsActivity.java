package dev.tudorflorea.numberfacts.ui;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.services.NotificationScheduler;


public class SettingsActivity extends AppCompatActivity implements  SharedPreferences.OnSharedPreferenceChangeListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupSharedPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

}
