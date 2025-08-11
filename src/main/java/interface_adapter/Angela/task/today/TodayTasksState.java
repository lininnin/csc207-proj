package interface_adapter.Angela.task.today;

/**
 * State for the today tasks view model.
 */
public class TodayTasksState {
    private boolean refreshNeeded;
    private String error;
    private String successMessage;

    public TodayTasksState() {
        this.refreshNeeded = false;
    }

    public TodayTasksState(TodayTasksState copy) {
        this.refreshNeeded = copy.refreshNeeded;
        this.error = copy.error;
        this.successMessage = copy.successMessage;
    }

    public boolean isRefreshNeeded() {
        return refreshNeeded;
    }

    public void setRefreshNeeded(boolean refreshNeeded) {
        this.refreshNeeded = refreshNeeded;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public void clearMessages() {
        this.error = null;
        this.successMessage = null;
    }
}