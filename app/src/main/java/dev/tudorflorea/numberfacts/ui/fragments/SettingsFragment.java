package dev.tudorflorea.numberfacts.ui.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import dev.tudorflorea.numberfacts.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_settings_fragment);
    }
}
