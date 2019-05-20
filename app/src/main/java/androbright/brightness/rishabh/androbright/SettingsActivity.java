package androbright.brightness.rishabh.androbright;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG=SettingsActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Fade());
        }
        setContentView(R.layout.activity_settings);
        Log.i(TAG, "onCreate() method fired ");

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MY_PREFERENCES())
                .commit();
    }


    public static class MY_PREFERENCES extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        private static final String TAG=MY_PREFERENCES.class.getSimpleName();
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
            Log.i(TAG, "onCreate() method fired");

            Preference minMagnitude;

            minMagnitude=findPreference(getString(R.string.username_key));
            bindPreferenceSummaryToValue(minMagnitude);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            Log.i(TAG, "onPreferenceChange() method fired ");
            String stringValue = newValue.toString();
//
            //   if (preference instanceof ListPreference) {
            //       ListPreference listPreference = (ListPreference) preference;
            //       int prefIndex = listPreference.findIndexOfValue(stringValue);
            //       if (prefIndex >= 0) {
            //           CharSequence[] labels = listPreference.getEntries();
            //           preference.setSummary(labels[prefIndex]);
            //       }
            //   } else {
            preference.setSummary(stringValue);
            //   }
//
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            Log.i(TAG, "bindPreferenceSummaryToValue() method fired ");
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);

        }


    }

} // Class ends here