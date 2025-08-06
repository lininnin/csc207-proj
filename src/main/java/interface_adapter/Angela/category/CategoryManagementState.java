package interface_adapter.Angela.category;

/**
 * State for category management operations.
 */
public class CategoryManagementState {
    private String lastAction;
    private String message;
    private String error;
    private boolean refreshNeeded;
    private boolean dialogOpen;

    // Getters and setters
    public String getLastAction() { return lastAction; }
    public void setLastAction(String lastAction) { this.lastAction = lastAction; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public boolean isRefreshNeeded() { return refreshNeeded; }
    public void setRefreshNeeded(boolean refreshNeeded) { this.refreshNeeded = refreshNeeded; }

    public boolean isDialogOpen() { return dialogOpen; }
    public void setDialogOpen(boolean dialogOpen) { this.dialogOpen = dialogOpen; }
}