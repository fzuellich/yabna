package app.yabna.activities;

import android.app.Activity;
import android.os.Bundle;

import app.yabna.fragements.SettingsFragment;

/**
 * SettingsActivity used to manipulate the applications settings. Using a PreferenceFragment as
 * mentioned in the developer docs.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // add settings fragment
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
