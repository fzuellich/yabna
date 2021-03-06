package app.yabna.fragements;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.yabna.R;
import app.yabna.tasks.ChannelPreferencesTask;
import app.yabna.utils.AsyncTaskFinishedListener;
import app.yabna.utils.ChannelDAO;
import app.yabna.utils.PreferenceHelper;

/**
 * TODO: check if oncreate and onresume is mixed up
 * Fragment implementing main settings behaviour to subscribed/unsubscribe from channels.
 */
public class SettingsFragment extends PreferenceFragment implements AsyncTaskFinishedListener {

    // /////////////////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////////////////

    private ProgressDialog progressDialog;

    // our list of checkbox preferences
    private List<CheckBoxPreference> preferences = new ArrayList<CheckBoxPreference>();

    // /////////////////////////////////////////////////////////////////////////////////////
    // Lifecycle methods
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.dialog_title_fetching_data));
        progressDialog.setMessage(getString(R.string.dialog_msg_fetching_data));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // add base preference
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();

        progressDialog.show();

        try {
            // load all channel preferences from web file
            ChannelPreferencesTask task = new ChannelPreferencesTask(this);
            task.execute(new URL(getString(R.string.channel_csv_file_url)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // save all subscribed channels
        List<String> saveThis = new ArrayList<String>();
        for (CheckBoxPreference preference : preferences) {
            if (preference.isChecked()) {
                saveThis.add(preference.getKey());
            }
        }

        PreferenceHelper.saveSubscribedChannelsAsString(saveThis, getActivity().getApplicationContext());
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Logic
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void taskFinished(Object result) {
        // set all available channels
        List<ChannelDAO> availableChannels = (List<ChannelDAO>) result;

        // add a new preference
        PreferenceCategory category = (PreferenceCategory) findPreference("pref_channel");
        for (ChannelDAO channel : availableChannels) {
            CheckBoxPreference pref = new CheckBoxPreference(getActivity().getApplicationContext());
            pref.setTitle(channel.getTitle());
            pref.setKey(channel.getPreferenceString());

            category.addPreference(pref);
            preferences.add(pref);
        }

        // dismiss dialog
        progressDialog.dismiss();
    }
}
