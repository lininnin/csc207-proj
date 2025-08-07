package interface_adapter.Angela.task.edit_available;

/**
 * State for the edit available task view model.
 */
public class EditAvailableTaskState {
    private String editingTaskId;
    private String error;
    private String successMessage;

    public EditAvailableTaskState() {
        // Default constructor
    }

    public EditAvailableTaskState(EditAvailableTaskState copy) {
        this.editingTaskId = copy.editingTaskId;
        this.error = copy.error;
        this.successMessage = copy.successMessage;
    }

    public String getEditingTaskId() {
        return editingTaskId;
    }

    public void setEditingTaskId(String editingTaskId) {
        this.editingTaskId = editingTaskId;
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
    
    @Override
    public String toString() {
        return "EditAvailableTaskState{" +
                "editingTaskId='" + editingTaskId + '\'' +
                ", error='" + error + '\'' +
                ", successMessage='" + successMessage + '\'' +
                '}';
    }
}