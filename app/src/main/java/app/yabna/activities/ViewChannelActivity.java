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
import app.yabna.utils.AsyncTaskFinishedListener;
import app.yabna.utils.FeedDAO;
import app.yabna.utils.FeedItemDAO;

/**
 * List all news items of a channel
 */
public class ViewChannelActivity extends ListActivity implements AsyncTaskFinishedListener {

    private ProgressDialog progressDialog;

    private ArrayAdapter<FeedItemDAO> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // display "< | Title ..."
        getActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra(getString(R.string.intent_channelDetailTitleExtra));
        setTitle(title);

        try {
            String feedUrl = getIntent().getStringExtra(getString(R.string.intent_channelDetailUrlExtra));

            // create a progress dialog
            progressDialog = new ProgressDialog(ViewChannelActivity.this);
            progressDialog.setTitle(getString(R.string.dialog_title_fetching_data));
            progressDialog.setMessage(getString(R.string.dialog_msg_fetching_data));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


            // execute task. check callback function below.
            new ParseFeedTask(this).execute(new URL(feedUrl));

            // create adapter and use base list item
            myAdapter = new ArrayAdapter<FeedItemDAO>(getApplicationContext(), android.R.layout.simple_list_item_1);

            // set adapter as main source
            setListAdapter(myAdapter);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.show();
    }

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

    @Override
    public void taskFinished(Object result) {
        FeedDAO feed = (FeedDAO) result;

        // fill adapter
        myAdapter.addAll(feed.getItems());

        // dismiss dialog
        progressDialog.dismiss();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // feed item
        FeedItemDAO feedItem = (FeedItemDAO) l.getAdapter().getItem(position);

        // open a browser
        Uri itemUrl = Uri.parse(feedItem.getLink());
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, itemUrl);
        startActivity(browserIntent);
    }
}
