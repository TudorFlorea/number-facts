package dev.tudorflorea.numberfacts.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.services.NotificationScheduler;

public class PreferencesUtils {

    private static final String THEME_GREEN = "green";
    private static final String THEME_RED = "red";
    private static final String THEME_BLUE = "blue";

    public static void setupSharedPreferences(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        if (sharedPreferences.getBoolean(context.getResources().getString(R.string.pref_notifications_key), context.getResources().getBoolean(R.bool.pref_notifications_default_value))) {
            NotificationScheduler.scheduleFactNotification(context);
        }

        String theme = sharedPreferences.getString(context.getResources().getString(R.string.pref_theme_key), context.getResources().getString(R.string.pref_theme_default_value));

        switch (theme) {

            case THEME_GREEN:
                context.setTheme(R.style.AppTheme_Green);
                break;
            case THEME_RED:
                context.setTheme(R.style.AppTheme_Red);
                break;

            case THEME_BLUE:
                context.setTheme(R.style.AppTheme_Blue);
                break;

            default:
                context.setTheme(R.style.AppTheme_Green);
        }

    }

    public static void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key, Context context) {

        if (key.equals(context.getResources().getString(R.string.pref_notifications_key))) {
            boolean isNotificationOn = sharedPreferences.getBoolean(key, context.getResources().getBoolean(R.bool.pref_notifications_default_value));
            if (isNotificationOn) {
                NotificationScheduler.scheduleFactNotification(context);
            } else {
                NotificationScheduler.cancelFactNotification(context);
            }
        }

        if (key.equals(context.getResources().getString(R.string.pref_theme_key))) {

            String theme = sharedPreferences.getString(key, context.getResources().getString(R.string.pref_theme_default_value));


            switch (theme) {

                case THEME_GREEN:
                    context.setTheme(R.style.AppTheme_Green);

                    break;
                case THEME_RED:
                    context.setTheme(R.style.AppTheme_Red);
                    break;

                case THEME_BLUE:
                    context.setTheme(R.style.AppTheme_Blue);
                    break;
                default:
                    context.setTheme(R.style.AppTheme_Green);
            }
        }

    }

}
