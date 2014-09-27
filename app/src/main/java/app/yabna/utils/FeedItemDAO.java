package app.yabna.utils;

import java.io.Serializable;

/**
 * Encapsulating a feed item.
 */
public class FeedItemDAO implements Serializable {
    private String title;
    private String link;

    public FeedItemDAO() {
    }

    public FeedItemDAO(String title, String link) {
        setTitle(title);
        setLink(link);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedItemDAO that = (FeedItemDAO) o;

        if (link != null ? !link.equals(that.link) : that.link != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
