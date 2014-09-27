package app.yabna.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import app.yabna.utils.AsyncTaskFinishedListener;
import app.yabna.utils.FeedDAO;
import app.yabna.utils.FeedParser;
import app.yabna.utils.ReadItemsList;

/**
 * Class to parse a feed and generate a list of entries to be displayed in the UI. Therefore it is an
 * async task using the feed parser.
 */
public class ParseFeedTask extends AsyncTask<URL, Integer, FeedDAO> {

    private final AsyncTaskFinishedListener listener;
    private final Context context;

    public ParseFeedTask(AsyncTaskFinishedListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected FeedDAO doInBackground(URL... urls) {
        InputStream is = downloadFeed(urls[0]);
        FeedDAO feed = new  FeedParser(is, urls[0].toExternalForm()).parseFeed();

        ReadItemsList readItems = new LoadReadListTask(context).doInBackground(feed);
        feed.setReadItems(readItems);

        return feed;
    }

    @Override
    protected void onPostExecute(FeedDAO feedDAO) {
        super.onPostExecute(feedDAO);
        listener.taskFinished(feedDAO);
    }

    /**
     * We could just create a more abstract function and use it in ChannelPreferenceTask and this one,
     * but what about performance?
     *
     * @param url feed url
     *
     * @return stream containing file.
     */
    private InputStream downloadFeed(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            return connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // no null values...
        return new ByteArrayInputStream("".getBytes());
    }


}
