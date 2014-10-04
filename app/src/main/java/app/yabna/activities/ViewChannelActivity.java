package app.yabna.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.net.URL;

import app.yabna.R;
import app.yabna.tasks.ParseFeedTask;
import app.yabna.tasks.SaveReadListTask;
import app.yabna.utils.AsyncTaskFinishedListener;
import app.yabna.utils.ChannelListViewAdapter;
import app.yabna.utils.FeedDAO;
import app.yabna.utils.FeedItemDAO;

/**
 * TODO: check if onresume and oncreate is mixed up
 * Activity will list all items found for a given channel. The channel to be displayed is controlled
 * by an Intent. There is an extra (R.string.intent_channelDetailUrlExtra) to define the url to call
 * and an extra to define the activities title (R.string.intent_channelDetailTitleExtra).
 */
public class ViewChannelActivity extends ListActivity implements AsyncTaskFinishedListener {

    // /////////////////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////////////////

    // feed instance loaded by this activity
    private FeedDAO feed;

    // progress dialog indicating download progress
    private ProgressDialog progressDialog;

    // adapter used to display feed entries
    private ArrayAdapter<FeedItemDAO> myAdapter;

    // /////////////////////////////////////////////////////////////////////////////////////
    // Lifecycle methods
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // display "< | Title ..."
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // retrieve the title for the activity from the intent
        String title = getIntent().getStringExtra(getString(R.string.intent_channelDetailTitleExtra));
        setTitle(title);

        // parse the feed
        try {
            String feedUrl = getIntent().getStringExtra(getString(R.string.intent_channelDetailUrlExtra));

            // create a progress dialog
            progressDialog = new ProgressDialog(ViewChannelActivity.this);
            progressDialog.setTitle(getString(R.string.dialog_title_fetching_data));
            progressDialog.setMessage(getString(R.string.dialog_msg_fetching_data));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            // execute task. check callback function below.
            new ParseFeedTask(this, getApplicationContext()).execute(new URL(feedUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // save the read items list
        new SaveReadListTask(getApplicationContext())
                .execute(new Object[]{feed.getUrl(), feed.getReadItems()});
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Activity logic
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void taskFinished(Object result) {
        feed = (FeedDAO) result;

        System.out.println("Loading finished.");
        for (FeedItemDAO item : feed.getItems()) {
            System.out.println(item.getTitle() + " -> " + feed.getReadItems().isItemRead(item));
        }


        // create custom adapter
        myAdapter = new ChannelListViewAdapter(getApplicationContext(), feed.getReadItems());
        myAdapter.addAll(feed.getItems());

        // set adapter as main source
        setListAdapter(myAdapter);

        // dismiss dialog
        progressDialog.dismiss();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // feed item
        FeedItemDAO feedItem = (FeedItemDAO) l.getAdapter().getItem(position);

        // mark read
        feed.getReadItems().markItemAsRead(feedItem);
        myAdapter.notifyDataSetChanged();

        // open a browser
        Uri itemUrl = Uri.parse(feedItem.getLink());
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, itemUrl);
        startActivity(browserIntent);
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Menu stuff
    // /////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
