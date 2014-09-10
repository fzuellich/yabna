package app.yabna.utils;

import java.util.List;

/**
 * Object encapsulating a feed.
 */
public class FeedDAO {

    private String feedTitle;

    private List<FeedItemDAO> items;

    public FeedDAO() {
    }

    public FeedDAO(String title, List<FeedItemDAO> items) {
        this.feedTitle = title;
        this.items = items;
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
