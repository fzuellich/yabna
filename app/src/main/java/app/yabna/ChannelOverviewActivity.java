package app.yabna;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.net.MalformedURLException;
import java.net.URL;

import app.yabna.tasks.ParseFeedTask;
import app.yabna.utils.AsyncTaskFinishedListener;


public class ChannelOverviewActivity extends ListActivity implements AsyncTaskFinishedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            new ParseFeedTask(this).execute(new URL("http://todejutsu.org/rss.xml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void taskFinished(Object result) {

    }
}
