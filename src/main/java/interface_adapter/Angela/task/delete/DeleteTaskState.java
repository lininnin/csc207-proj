package interface_adapter.Angela.task.delete;

/**
 * State for delete task operations.
 */
public class DeleteTaskState {
    private String lastDeletedTaskId;
    private String successMessage;
    private String error;
    private boolean showWarningDialog;
    private String pendingDeleteTaskId;
    private String pendingDeleteTaskName;

    // Getters and setters
    public String getLastDeletedTaskId() {
        return lastDeletedTaskId;
    }

    public void setLastDeletedTaskId(String lastDeletedTaskId) {
        this.lastDeletedTaskId = lastDeletedTaskId;
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

    public boolean isShowWarningDialog() {
        return showWarningDialog;
    }

    public void setShowWarningDialog(boolean showWarningDialog) {
        this.showWarningDialog = showWarningDialog;
    }

    public String getPendingDeleteTaskId() {
        return pendingDeleteTaskId;
    }

    public void setPendingDeleteTaskId(String pendingDeleteTaskId) {
        this.pendingDeleteTaskId = pendingDeleteTaskId;
    }

    public String getPendingDeleteTaskName() {
        return pendingDeleteTaskName;
    }

    public void setPendingDeleteTaskName(String pendingDeleteTaskName) {
        this.pendingDeleteTaskName = pendingDeleteTaskName;
    }
}