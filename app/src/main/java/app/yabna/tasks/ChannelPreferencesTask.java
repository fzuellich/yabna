package app.yabna.tasks;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import app.yabna.utils.AsyncTaskFinishedListener;

/**
 * Fetch a list of available channels.
 *
 * TODO: Add caching mechanism
 * TODO: Add dialog to notify user
 */
public class ChannelPreferencesTask extends AsyncTask<URL, Integer, Map<String, String>> {

    private final AsyncTaskFinishedListener finishedListener;

    /**
     * Create a new async task to load all channels and call the listener when finished.
     * @param finishedListener listener will be called if finished. receives a Map<String, String>>.
     */
    public ChannelPreferencesTask(AsyncTaskFinishedListener finishedListener) {
        this.finishedListener = finishedListener;
    }


    /**
     * Fetch a csv file and get all available channels.
     *
     * @param urls url to download the csv from
     * @return Dictonary (Channel name -> Channel url)
     */
    @Override
    protected Map<String, String> doInBackground(URL... urls) {
        return fetchChannelCSV(urls[0]);
    }

    /**
     * Fetch a csv from the given url and build a map containing all channels with their url.
     * @param url for csv
     * @return map of channel -> url
     */
    private Map<String, String> fetchChannelCSV(URL url) {
        // open new connection and get content
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // parse the input and transform the csv content into a map
            HashMap<String, String> channelMap = new HashMap<String, String>();
            String line = reader.readLine();
            while (line != null) {
                String[] splits = line.split(";");
                channelMap.put(splits[0], splits[1]);
                line = reader.readLine();
            }

            return channelMap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HashMap<String, String>();
    }

    @Override
    protected void onPostExecute(Map<String, String> channels) {
        super.onPostExecute(channels);
        finishedListener.taskFinished(channels);
    }
}
