package app.yabna.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A class keeping track of the read items of a channel.
 */
public class ReadItemsList implements Serializable {

    // /////////////////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////////////////

    private Map<FeedItemDAO, Boolean> readMap = new HashMap<FeedItemDAO, Boolean>();

    // /////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Create a new empty instance.
     */
    public ReadItemsList() {
    }

    /**
     * Create a new instance of this class. Will automatically sync the list with the items
     * of the feed.
     *
     * @param feed Feed that will be represented.
     */
    public ReadItemsList(FeedDAO feed) {
        syncWithFeed(feed);
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Logic
    // /////////////////////////////////////////////////////////////////////////////////////

    public void markItemAsRead(FeedItemDAO item) {
        readMap.put(item, true);
    }

    /**
     * Check if the given item is marked as read. If item is not found false is returned.
     *
     * @param item item to check read status
     * @return false if not known otherwise the relative value
     */
    public boolean isItemRead(FeedItemDAO item) {
        boolean result = false;
        if (!readMap.containsKey(item)) {
            result = false; // item not known, therefore not read
        } else {
            result = readMap.get(item); // item known, use value
        }
        return result;
    }

    /**
     * Removes not longer known items and adds all not known ones.
     *
     * @param feed object to use for syncing.
     */
    public void syncWithFeed(FeedDAO feed) {
        HashMap<FeedItemDAO, Boolean> newReadMap = new HashMap<FeedItemDAO, Boolean>();

        for (FeedItemDAO item : feed.getItems()) {

            // value is already known so add it to the new map with the known value
            if (readMap.containsKey(item)) {
                newReadMap.put(item, readMap.get(item));
            } else {
                newReadMap.put(item, false); // new item not read yet
            }
        }
    }
}
