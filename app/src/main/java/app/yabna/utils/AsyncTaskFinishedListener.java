package app.yabna.utils;

/**
 * Interface representing a listener waiting for an async task to finish.
 */
public interface AsyncTaskFinishedListener {
    public void taskFinished(Object result);
}
