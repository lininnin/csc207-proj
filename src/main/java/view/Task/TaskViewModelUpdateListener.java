package view.Task;

/**
 * Interface for listening to TaskViewModel updates.
 * Implements the observer pattern for view updates.
 */
public interface TaskViewModelUpdateListener {
    /**
     * Called when the view model has been updated.
     * Views should refresh their display when this is called.
     */
    void onViewModelUpdated();
}