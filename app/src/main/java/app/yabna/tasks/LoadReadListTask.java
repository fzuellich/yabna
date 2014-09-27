package app.yabna.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.NoSuchAlgorithmException;

import app.yabna.utils.AsyncTaskFinishedListener;
import app.yabna.utils.FeedDAO;
import app.yabna.utils.FileSystemHelper;
import app.yabna.utils.ReadItemsList;

/**
 * Load the list of read items for a given feed.
 */
public class LoadReadListTask extends AsyncTask<FeedDAO, Void, ReadItemsList> {

    // async listener to call when finished
    private final AsyncTaskFinishedListener finishedListener;

    // context used to get a new file in the internal storage
    private final Context context;

    /**
     * Create a new instance without an async listener. Maybe used if you just wanna call the
     * doInBackground method from another async task.
     *
     * @param context ApplicationContext instance to get a file handle in the internal storage.
     */
    public LoadReadListTask(Context context) {
        this.finishedListener = new AsyncTaskFinishedListener() {
            @Override
            public void taskFinished(Object result) {
            }
        };

        this.context = context;
    }

    /**
     * Create a new instance with an async listener.
     *
     * @param finishedListener listener to be notified when finished.
     * @param context ApplicationContext instance to get a file handle in the internal storage.
     */
    public LoadReadListTask(AsyncTaskFinishedListener finishedListener, Context context) {
        this.finishedListener = finishedListener;
        this.context = context;
    }

    /**
     * Reads the list for the given feed from the disk.
     *
     * @param feedDAOs feed to load data for.
     *
     * @return ReadItemsList instance. Maybe empty if file not found.
     */
    @Override
    protected ReadItemsList doInBackground(FeedDAO... feedDAOs) {
        ReadItemsList result = new ReadItemsList();
        try {
            // get a file handle
            String feedUrl = feedDAOs[0].getUrl();
            File file = new File(context.getFilesDir(), FileSystemHelper.createFileNameFromURI(feedUrl));

            // load streams...
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            // load object
            result = (ReadItemsList) ois.readObject();

            // close
            fis.close();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("I demand nice exception handling.");
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(ReadItemsList list) {
        super.onPostExecute(list);
        finishedListener.taskFinished(list);
    }
}
