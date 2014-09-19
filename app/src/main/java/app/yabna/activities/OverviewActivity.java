package app.yabna.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import app.yabna.R;
import app.yabna.utils.ChannelDAO;
import app.yabna.utils.PreferenceHelper;


public class OverviewActivity extends ListActivity {

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

        // get url
        String url = subscribedChannelAdapter.getItem(position).getLink();

        // create an intent and start detail activity to display stuff
        Intent intent = new Intent(OverviewActivity.this, ViewChannelActivity.class);
        intent.putExtra(getString(R.string.intent_channelDetailUrlExtra), url);
        startActivity(intent);
    }

    // //////////////////////////////////////
    // Menu related stuff
    // /////////////////////////////////////

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
