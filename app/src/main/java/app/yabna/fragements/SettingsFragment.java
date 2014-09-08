package app.yabna.fragements;


import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import app.yabna.R;
import app.yabna.tasks.ChannelPreferencesTask;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // add base preference
        addPreferencesFromResource(R.xml.preferences);

        try {
            // load all channel preferences from web file
            ChannelPreferencesTask task = new ChannelPreferencesTask();
            task.execute(new URL("http://deploy.zuellich.de/yabna/channels.csv"));
            try {
                PreferenceCategory channelCategory = (PreferenceCategory) findPreference("pref_channel");

                // process map
                Map<String, String> channelMap = task.get(); // todo make async
                for(String key : channelMap.keySet()) {
                    CheckBoxPreference pref =  new CheckBoxPreference(getActivity().getApplicationContext());
                    pref.setTitle(key);
                    pref.setKey(channelMap.get(key));

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
}
