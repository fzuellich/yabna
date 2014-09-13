package app.yabna.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.yabna.R;
import app.yabna.utils.ChannelDAO;
import app.yabna.utils.PreferenceHelper;


public class ChannelOverviewActivity extends ListActivity {

    private ArrayAdapter<ChannelDAO> subscribedChannelAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resuming...");
        loadChannels();
    }

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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    // //////////////////////////////////////
    // Menu related stuff
    // /////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
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
