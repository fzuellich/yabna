package app.yabna.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import app.yabna.R;

/**
 * Class used as adapter to display a listview of feed items of a channel.
 */
public class ChannelListViewAdapter extends ArrayAdapter<FeedItemDAO> {

    private final ReadItemsList readItemsList;

    /**
     * Create a new adapter and use the given context. Needs the ReadItemsList to determine how
     * to render the items.
     * @param context
     * @param readItemsList
     */
    public ChannelListViewAdapter(Context context, ReadItemsList readItemsList) {
        super(context, R.layout.simple_list_item_read);
        this.readItemsList = readItemsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get inflater service to inflate the layout files
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // this will be the resource used to inflate
        int resource;

        // determine resource
        boolean isRead = readItemsList.isItemRead(getItem(position));
        if(isRead) {
            resource = R.layout.simple_list_item_read;
        } else {
            resource = R.layout.simple_list_item_unread;
        }

        // get item
        FeedItemDAO feedItem = getItem(position);

        // setup the TextView
        View view = inflater.inflate(resource, parent, false);
        TextView item = (TextView) view.findViewById(R.id.text1);
        item.setText(feedItem.getTitle());

        return view;
    }
}
