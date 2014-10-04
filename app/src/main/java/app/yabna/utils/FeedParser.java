package app.yabna.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser to extract useful information from a feed. Mainly following the developer.android.com tutorial
 * here.
 * <p/>
 * TODO close stream. but without throwing around all the ioexceptions
 */
public class FeedParser {

    // /////////////////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////////////////

    // file content of the feed to parse.
    private final InputStream inputStream;

    private final String url;

    // /////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param stream feed as an input stream.
     */
    public FeedParser(InputStream stream, String url) {
        this.inputStream = stream;
        this.url = url;
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Logic
    // /////////////////////////////////////////////////////////////////////////////////////

    public FeedDAO parseFeed() {
        FeedDAO result = new FeedDAO();

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();

            //parser.require(XmlPullParser.START_TAG, null, "rss");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                if (name != null && name.equals("channel") && parser.getEventType() != XmlPullParser.END_TAG) {
                    result = parseChannel(parser);
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Parse a feed (and all its items...).
     *
     * @param parser
     * @return the feed encapsulated in a dao.
     */
    private FeedDAO parseChannel(XmlPullParser parser) throws IOException, XmlPullParserException {
        String title = "";
        List<FeedItemDAO> items = new ArrayList<FeedItemDAO>();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            if (name != null && name.equals("item")) {
                FeedItemDAO parsedItem = parseItem(parser);
                if(!parsedItem.getTitle().isEmpty() || !parsedItem.getLink().isEmpty()) {
                    items.add(parsedItem);
                }
            } else if (name != null && name.equals("title")) {
                title = extractText(parser);
            }
        }
        return new FeedDAO(url, title, items);
    }

    /**
     * Parse an item.
     *
     * @param parser to use
     * @return a item encapsulated in an dao.
     * @throws XmlPullParserException
     * @throws IOException
     */
    private FeedItemDAO parseItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        String link = "";
        String title = "";

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();
            if (tagName == null) {
                continue;
            }

            if (tagName.equals("title")) {
                title = extractText(parser);
            } else if (tagName.equals("link")) {
                link = extractText(parser);
            } else {
                continue;
            }

            if (link != null && !link.equals("") && title != null && !title.equals("")) break;
        }

        return new FeedItemDAO(title, link);
    }

    private String extractText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
