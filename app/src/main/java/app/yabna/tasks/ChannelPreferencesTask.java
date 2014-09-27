package app.yabna.tasks;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.yabna.utils.AsyncTaskFinishedListener;
import app.yabna.utils.ChannelDAO;

/**
 * Fetch a list of available channels.
 * <p/>
 * TODO: Add caching mechanism
 * TODO: Add dialog to notify user
 */
public class ChannelPreferencesTask extends AsyncTask<URL, Integer, List<ChannelDAO>> {

    // /////////////////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////////////////

    private final AsyncTaskFinishedListener finishedListener;

    // /////////////////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Create a new async task to load all channels and call the listener when finished.
     *
     * @param finishedListener listener will be called if finished. receives a List<ChannelItemDAO>.
     */
    public ChannelPreferencesTask(AsyncTaskFinishedListener finishedListener) {
        this.finishedListener = finishedListener;
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Lifecycle methods
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Fetch a csv file and get all available channels.
     *
     * @param urls url to download the csv from
     * @return
     */
    @Override
    protected List<ChannelDAO> doInBackground(URL... urls) {
        return fetchChannelCSV(urls[0]);
    }

    @Override
    protected void onPostExecute(List<ChannelDAO> channels) {
        super.onPostExecute(channels);
        finishedListener.taskFinished(channels);
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Logic
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Fetch a csv from the given url and build a list containing all channels with their url.
     *
     * @param url for csv
     * @return
     */
    private List<ChannelDAO> fetchChannelCSV(URL url) {
        // open new connection and get content
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // parse the input and transform the csv content into a map
            List<ChannelDAO> channelList = new ArrayList<ChannelDAO>();
            String line = reader.readLine();
            while (line != null) {
                channelList.add(ChannelDAO.parse(line));
                line = reader.readLine();
            }

            return channelList;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<ChannelDAO>(0);
    }

}
