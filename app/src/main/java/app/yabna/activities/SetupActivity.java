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

public class SetupActivity extends Activity {

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

    /**
     * Called when user hits the 'ok' button in the action bar. Will disable first start property and call
     * the overview activity to show the subscribed channels to the user.
     * @param item
     */
    public void setupReady(MenuItem item) {
        SharedPreferences appPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putBoolean(getString(R.string.app_preferences_first_start), false);
        editor.commit();

        startActivity(new Intent(SetupActivity.this, OverviewActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setup_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}