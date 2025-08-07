package interface_adapter.Angela.task.add_to_today;

/**
 * State for the add to today view model.
 */
public class AddTaskToTodayState {
    private String error;
    private String successMessage;
    private boolean refreshNeeded;

    public AddTaskToTodayState() {
        // Default constructor
        this.refreshNeeded = false;
    }

    public AddTaskToTodayState(AddTaskToTodayState copy) {
        this.error = copy.error;
        this.successMessage = copy.successMessage;
        this.refreshNeeded = copy.refreshNeeded;
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

    public boolean isRefreshNeeded() {
        return refreshNeeded;
    }

    public void setRefreshNeeded(boolean refreshNeeded) {
        this.refreshNeeded = refreshNeeded;
    }
}