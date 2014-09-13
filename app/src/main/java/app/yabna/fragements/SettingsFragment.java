package app.yabna.fragements;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import app.yabna.R;
import app.yabna.tasks.ChannelPreferencesTask;
import app.yabna.utils.AsyncTaskFinishedListener;
import app.yabna.utils.ChannelDAO;
import app.yabna.utils.PreferenceHelper;

/**
 * Fragment implementing main settings behaviour to subscribed/unsubscribe from channels.
 */
public class SettingsFragment extends PreferenceFragment implements AsyncTaskFinishedListener {

    private static final String CHANNEL_DELEMITER = "^";

    // holds all subscribed channels. use string because its easier to work with preferences this way.
    private List<String> subscribedChannels = new ArrayList<String>();

    // list of all available channels. will be set by the callback function below.
    private List<ChannelDAO> availableChannels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // add base preference
        addPreferencesFromResource(R.xml.preferences);

        // load a fresh set of available channels
        loadChannelList();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadChannelList();
        makePreferenceUI();
    }

    @Override
    public void onPause() {
        super.onPause();

        // save the changes
        saveSubscribedChannels();
    }

    /**
     * Fetch a fresh list of available channels. Asynchronous.
     */
    private void loadChannelList() {
        try {
            // load all channel preferences from web file
            ChannelPreferencesTask task = new ChannelPreferencesTask(this);
            task.execute(new URL(getString(R.string.channel_csv_file_url)));
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load all subscribed channels from the shared preferences.
     */
    private void loadSubscribedChannels() {
       subscribedChannels = PreferenceHelper
               .getSubscribedChannelsAsStrings(getActivity().getApplicationContext());
    }

    /**
     * Save all subscribed channels to the shared preferences.
     */
    private void saveSubscribedChannels() {
        if(subscribedChannels.size() == 0) return;
        System.out.println("Save channels...");

        PreferenceHelper
                .saveSubscribedChannelsAsString(subscribedChannels, getActivity().getApplicationContext());
    }

    /**
     * Creates all checkbox preferences for the channels.
     */
    private void makePreferenceUI() {
        // fetch category to add new preferences to it
        PreferenceCategory channelCategory = (PreferenceCategory) findPreference("pref_channel");

        // create a listener to handle changes to the preferences
        CheckBoxChangeListener changeListener = new CheckBoxChangeListener();

        // process available channels into preferences
        for(ChannelDAO channel : availableChannels) {
            CheckBoxPreference pref =  new CheckBoxPreference(getActivity().getApplicationContext());
            pref.setOnPreferenceChangeListener(changeListener);
            pref.setTitle(channel.getTitle());
            pref.setKey(channel.getPreferenceString());
            pref.setChecked(subscribedChannels.contains(channel.getPreferenceString()));

            // add preference to category
            channelCategory.addPreference(pref);
        }
    }

    @Override
    public void taskFinished(Object result) {
        // set all available channels
        availableChannels = (List<ChannelDAO>) result;

        // first load all subscribed channels so we can check already subscribed prefs in the ui
        loadSubscribedChannels();

        // now load the ui
        makePreferenceUI();
    }

    private class CheckBoxChangeListener implements Preference.OnPreferenceChangeListener {

        public CheckBoxChangeListener() {
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String key = preference.getKey();
            CheckBoxPreference checkbox = (CheckBoxPreference) preference;

            // add channel because not already present
            if(checkbox.isChecked() && !subscribedChannels.contains(key)) {
                System.out.println("Subscribing to " + key);
                subscribedChannels.add(key);
            } else if(!checkbox.isChecked() && subscribedChannels.contains(key)) {
                // remove channel
                System.out.println("Unsubscribing from " + key);
                subscribedChannels.remove(key);
            }

            return true;
        }
    }
}
