package interface_adapter.Angela.task.available;

/**
 * State for the available tasks view.
 * This is a placeholder - you'll need to implement fully when creating the view_available_tasks use case.
 */
public class AvailableTasksState {
    private boolean refreshNeeded = false;

    public boolean isRefreshNeeded() {
        return refreshNeeded;
    }

    public void setRefreshNeeded(boolean refreshNeeded) {
        this.refreshNeeded = refreshNeeded;
    }
}