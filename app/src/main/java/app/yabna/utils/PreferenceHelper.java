package app.yabna.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import app.yabna.R;

/**
 * Methods helping working with preferences.
 */
public class PreferenceHelper {

    private static final String CHANNEL_DELEMITER = "^";

    /**
     * Read all subscribed channels from the shared properties and return them as string with the
     * format: title;link.
     *
     * @param context Context to fetch preferences and using the getString() method.
     *
     * @return a list of subscribed channels as string
     */
    public static List<String> getSubscribedChannelsAsStrings(Context context) {
        List<String> result = new ArrayList<String>();

        // get SharedPreferences
        SharedPreferences preferences = context
                .getSharedPreferences(context.getString(R.string.pref_subscribed_channels_file), Context.MODE_PRIVATE);

        // split up
        String[] channelsRaw = preferences
                .getString(context.getString(R.string.pref_subscribed_channels_key), "")
                .split(CHANNEL_DELEMITER);

        // parse all channels
        for(String channel : channelsRaw) {
            if(channel.isEmpty()) continue;
            result.add(channel);
        }

        return result;
    }

    /**
     * Read all subscribed channels from the shared properties and return them as a dao.
     *
     * @param context Context to fetch preferences and using the getString() method.
     *
     * @return list of daos
     */
    public static List<ChannelDAO> getSubscribedChannelsAsDAO(Context context) {
        // fetch subscribed channels raw
        List<String> raw = PreferenceHelper.getSubscribedChannelsAsStrings(context);
        if(raw.size() == 0) return new ArrayList<ChannelDAO>(0);

        // initialize with correct size and parse channels
        List<ChannelDAO> result = new ArrayList<ChannelDAO>(raw.size());
        for(String rawChannel : raw) {
            result.add(ChannelDAO.parse(rawChannel));
        }

        return result;
    }

    /**
     * Save a list of strings as subscribed channels.
     *
     * @param subscribedChannels list of subscribed channels in form title:link
     * @param context Context to fetch preferences and using the getString() method.
     */
    public static void saveSubscribedChannelsAsString(List<String> subscribedChannels, Context context) {
        StringBuilder preferenceString = new StringBuilder();
        for(String channel : subscribedChannels) {
            System.out.println("Processing subscribed channel: " + channel);
            preferenceString.append(channel + CHANNEL_DELEMITER);
        }

        // cut out last |
        preferenceString.deleteCharAt(preferenceString.length()-1);

        // save in SharedPreference
        SharedPreferences preferences = context
                .getSharedPreferences(context.getString(R.string.pref_subscribed_channels_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_subscribed_channels_key), preferenceString.toString());
        editor.commit();
    }
}
