package app.yabna.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Object encapsulating a feed.
 */
public class FeedDAO {

    // /////////////////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////////////////

    private String feedTitle;

    private String url;

    private List<FeedItemDAO> items;

    private ReadItemsList readItems;

    // /////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////////////////

    public FeedDAO() {
        this.url = "";
        this.feedTitle = "";
        this.items = new ArrayList<FeedItemDAO>();
        this.readItems = new ReadItemsList();
    }

    public FeedDAO(String url, String title, List<FeedItemDAO> items) {
        this.url = url;
        this.feedTitle = title;
        this.items = items;

        this.readItems = new ReadItemsList(this);
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Getter / Setter
    // /////////////////////////////////////////////////////////////////////////////////////

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<FeedItemDAO> getItems() {
        return items;
    }

    public void setItems(List<FeedItemDAO> items) {
        this.items = items;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    public ReadItemsList getReadItems() {
        return readItems;
    }

    public void setReadItems(ReadItemsList readItems) {
        this.readItems = readItems;
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Java internals
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedDAO feedDAO = (FeedDAO) o;

        if (feedTitle != null ? !feedTitle.equals(feedDAO.feedTitle) : feedDAO.feedTitle != null)
            return false;
        if (items != null ? !items.equals(feedDAO.items) : feedDAO.items != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = feedTitle != null ? feedTitle.hashCode() : 0;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }
}
