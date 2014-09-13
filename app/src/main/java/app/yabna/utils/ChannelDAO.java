package app.yabna.utils;

/**
 *
 */
public class ChannelDAO {

    private String title;

    private String link;

    public ChannelDAO() {
    }

    /**
     * Creates a new dao using a string like title;url
     * @param dao
     * @return
     */
    public static ChannelDAO parse(String dao) {
        String[] parts = dao.split(";");

        ChannelDAO result = new ChannelDAO();
        result.setTitle(parts[0]);
        result.setLink(parts[1]);

        return result;
    }

    /**
     * @return a string that can be used to be stored in a SharedPreference.
     */
    public String getPreferenceString() {
        return String.format("%s;%s", getTitle(), getLink());
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChannelDAO that = (ChannelDAO) o;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
