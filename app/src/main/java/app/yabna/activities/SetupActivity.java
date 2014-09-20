package app.yabna.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.animation.OvershootInterpolator;

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
     * Called when user hits the 'ready' button. Will disable first start property and call
     * the overview activity to show the subscribed channels to the user.
     * @param view
     */
    public void setupReady(View view) {
        SharedPreferences appPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putBoolean(getString(R.string.app_preferences_first_start), false);
        editor.commit();

        startActivity(new Intent(SetupActivity.this, OverviewActivity.class));
    }
}