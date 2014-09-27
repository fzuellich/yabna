package app.yabna.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import app.yabna.R;
import app.yabna.utils.ChannelDAO;
import app.yabna.utils.PreferenceHelper;

/**
 * Activity gives the user a general overview of all his subscribed channels.
 */
public class OverviewActivity extends ListActivity {

    // /////////////////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////////////////

    // adapter used to fill the ListView
    private ArrayAdapter<ChannelDAO> subscribedChannelAdapter;

    // /////////////////////////////////////////////////////////////////////////////////////
    // Lifecycle methods
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onResume() {
        super.onResume();

        // check if first start ...
        if (isFirstStart()) {
            startActivity(new Intent(OverviewActivity.this, SetupActivity.class));
            finish();
        }

        // not first start so load subscribed channels
        loadChannels();
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Logic
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Load all subscribed channels from the preferences and add them to the ListView adapter.
     */
    private void loadChannels() {
        // fetch subscribed channels
        List<ChannelDAO> subscribedChannels = PreferenceHelper
                .getSubscribedChannelsAsDAO(getApplicationContext());

        // create adapter so we can add subscribed channels
        subscribedChannelAdapter = new ArrayAdapter<ChannelDAO>(getApplicationContext()
                , android.R.layout.simple_list_item_1);
        subscribedChannelAdapter.addAll(subscribedChannels);

        // set views' adapter
        setListAdapter(subscribedChannelAdapter);
    }

    /**
     * Check if this is the first start.
     *
     * @return
     */
    private boolean isFirstStart() {
        boolean result = true;

        // load preferences and get to know if first start. return true if not found.
        SharedPreferences appPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
        result = appPreferences.getBoolean(getString(R.string.app_preferences_first_start), true);

        return result;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // get basic information
        String url = subscribedChannelAdapter.getItem(position).getLink();
        String title = subscribedChannelAdapter.getItem(position).getTitle();

        // create an intent and start detail activity to display stuff
        Intent intent = new Intent(OverviewActivity.this, ViewChannelActivity.class);
        intent.putExtra(getString(R.string.intent_channelDetailUrlExtra), url);
        intent.putExtra(getString(R.string.intent_channelDetailTitleExtra), title);
        startActivity(intent);
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Menu stuff
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
