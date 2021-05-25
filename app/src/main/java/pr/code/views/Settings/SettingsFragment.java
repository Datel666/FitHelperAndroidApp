package pr.code.views.Settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import pr.code.R;

/**
 * This fragment is used to configure application settings
 */
public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings,rootKey);


    }
}
