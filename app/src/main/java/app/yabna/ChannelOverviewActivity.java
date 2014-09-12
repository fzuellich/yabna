package app.yabna;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.net.URL;

import app.yabna.tasks.ParseFeedTask;
import app.yabna.utils.AsyncTaskFinishedListener;
import app.yabna.utils.FeedDAO;
import app.yabna.utils.FeedItemDAO;


public class ChannelOverviewActivity extends ListActivity implements AsyncTaskFinishedListener {

    private ArrayAdapter<FeedItemDAO> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // execute task. check callback function below.
            new ParseFeedTask(this).execute(new URL("http://todejutsu.org/rss.xml"));

            // create adapter and use base list item
            myAdapter = new ArrayAdapter<FeedItemDAO>(getApplicationContext(), android.R.layout.simple_list_item_1);

            // set adapter as main source
            setListAdapter(myAdapter);

            System.out.println("Hello World, I'm talking to you!");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.channel_overview, menu);

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

    @Override
    public void taskFinished(Object result) {
        System.out.println("Task finished...");
        FeedDAO feed = (FeedDAO) result;

        // fill adapter
        myAdapter.addAll(feed.getItems());
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
