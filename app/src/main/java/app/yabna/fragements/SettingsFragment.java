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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import app.yabna.R;
import app.yabna.tasks.ChannelPreferencesTask;

/**
 * TODO: check why color of checkbox text is ugly as hell
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // add base preference
        addPreferencesFromResource(R.xml.preferences);

        // load subscribed channels so we can set the preferences accordingly below
        SharedPreferences prefs = getActivity().getApplicationContext()
                .getSharedPreferences(getString(R.string.pref_subscribed_channels_file), Context.MODE_PRIVATE);
        Set<String> subscribedChannels = prefs.getStringSet(getString(R.string.pref_subscribed_channels_key), new HashSet<String>());

        try {
            // load all channel preferences from web file
            ChannelPreferencesTask task = new ChannelPreferencesTask();
            task.execute(new URL(getString(R.string.channel_csv_file_url)));
            try {
                PreferenceCategory channelCategory = (PreferenceCategory) findPreference("pref_channel");

                // process map
                CheckBoxChangeListener changeListener = new CheckBoxChangeListener();
                Map<String, String> channelMap = task.get(); // todo make async
                for(String key : channelMap.keySet()) {
                    CheckBoxPreference pref =  new CheckBoxPreference(getActivity().getApplicationContext());
                    pref.setOnPreferenceChangeListener(changeListener);
                    pref.setTitle(key);
                    pref.setKey(channelMap.get(key));
                    pref.setChecked(subscribedChannels.contains(channelMap.get(key)));

                    // add preference to category
                    channelCategory.addPreference(pref);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private class CheckBoxChangeListener implements Preference.OnPreferenceChangeListener {

        private final SharedPreferences prefs;

        public CheckBoxChangeListener() {
            prefs = getActivity().getApplicationContext()
                    .getSharedPreferences(getString(R.string.pref_subscribed_channels_file), Context.MODE_PRIVATE);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            Set<String> subscribedChannels = prefs.getStringSet(getString(R.string.pref_subscribed_channels_key), new HashSet<String>());

            boolean isSubscribed = (Boolean) o;
            // add channel if subscribed and not in set
            if(isSubscribed && !subscribedChannels.contains(preference.getKey())) {
                subscribedChannels.add(preference.getKey());
            } else if(!isSubscribed && subscribedChannels.contains(preference.getKey())) {
                // remove channel if not subscribed but still in set
                subscribedChannels.remove(preference.getKey());
            }

            // save the changes
            SharedPreferences.Editor prefEditor = prefs.edit();
            prefEditor.putStringSet(getString(R.string.pref_subscribed_channels_key), subscribedChannels);
            prefEditor.commit();

            // we want to update the preference as well
            return true;
        }
    }
}
