package app.yabna.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import app.yabna.R;
import app.yabna.fragements.SettingsFragment;

/**
 * Activity is started when the app has not been started before. Will prompt for the channels a
 * user might want to subscribe to.
 */
public class SetupActivity extends Activity {

    // /////////////////////////////////////////////////////////////////////////////////////
    // Lifecycle methods
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new SettingsFragment())
                    .commit();
        }
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Logic
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Called when user hits the 'ok' button in the action bar. Will disable first start property and call
     * the overview activity to show the subscribed channels to the user.
     *
     * @param item menu item that was "clicked"
     */
    public void setupReady(MenuItem item) {
        // disable first start setup behaviour
        SharedPreferences appPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putBoolean(getString(R.string.app_preferences_first_start), false);
        editor.commit();

        // start the overview activity again and show the subscribed channels
        startActivity(new Intent(SetupActivity.this, OverviewActivity.class));
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Menu stuff
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setup_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}