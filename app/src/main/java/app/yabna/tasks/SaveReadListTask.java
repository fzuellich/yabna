package app.yabna.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

import app.yabna.utils.AsyncTaskFinishedListener;
import app.yabna.utils.FileSystemHelper;

/**
 * Save a list of read items on the disk. First parameter should is a string describing the file path.
 * Second parameter is the ReadItemsList instance.
 */
public class SaveReadListTask extends AsyncTask<Object, Void, Object> {

    // /////////////////////////////////////////////////////////////////////////////////////
    // Variables
    // /////////////////////////////////////////////////////////////////////////////////////

    private final AsyncTaskFinishedListener finishedListener;

    private final Context context;

    // /////////////////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////////////////

    public SaveReadListTask(Context context) {
        this.context = context;
        this.finishedListener = new AsyncTaskFinishedListener() {
            @Override
            public void taskFinished(Object result) {
            }
        };
    }

    /**
     * Create a new async task to save the list of read items to disk and call the listener when finished.
     *
     * @param context          context used to obtain internal storage file handle
     * @param finishedListener listener will be called if finished. receives a ReadItemsList instance.
     */
    public SaveReadListTask(Context context, AsyncTaskFinishedListener finishedListener) {
        this.context = context;
        this.finishedListener = finishedListener;
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // Lifecycle methods
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Save the list.
     *
     * @param param param[0] string = path; param[1] instance to save.
     * @return
     */
    @Override
    protected Object doInBackground(Object... param) {
        try {
            // get file handle
            File file = new File(context.getFilesDir(), FileSystemHelper.createFileNameFromURI(param[0].toString()));

            // load streams...
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // write object
            oos.writeObject(param[1]);

            // close
            fos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return new Object();
    }

    @Override
    protected void onPostExecute(Object object) {
        super.onPostExecute(object);
        finishedListener.taskFinished(object);
    }
}
