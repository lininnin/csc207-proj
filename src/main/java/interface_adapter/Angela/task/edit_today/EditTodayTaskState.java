package interface_adapter.Angela.task.edit_today;

/**
 * State for the edit today task view model.
 * Immutable state object that holds the current state of the edit today task UI.
 */
public class EditTodayTaskState {
    private String editingTaskId;
    private String lastEditedTaskId;
    private String successMessage;
    private String error;
    private boolean showDialog;

    /**
     * Default constructor for creating an empty state.
     */
    public EditTodayTaskState() {
        // Default empty state
    }

    /**
     * Copy constructor for creating a new state from an existing one.
     * 
     * @param copy The state to copy
     */
    public EditTodayTaskState(EditTodayTaskState copy) {
        this.editingTaskId = copy.editingTaskId;
        this.lastEditedTaskId = copy.lastEditedTaskId;
        this.successMessage = copy.successMessage;
        this.error = copy.error;
        this.showDialog = copy.showDialog;
    }

    // Getters and setters

    public String getEditingTaskId() {
        return editingTaskId;
    }

    public void setEditingTaskId(String editingTaskId) {
        this.editingTaskId = editingTaskId;
    }

    public String getLastEditedTaskId() {
        return lastEditedTaskId;
    }

    public void setLastEditedTaskId(String lastEditedTaskId) {
        this.lastEditedTaskId = lastEditedTaskId;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }
}