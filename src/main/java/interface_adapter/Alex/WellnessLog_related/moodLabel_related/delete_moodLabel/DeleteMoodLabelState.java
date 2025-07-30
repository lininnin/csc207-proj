package interface_adapter.Alex.WellnessLog_related.moodLabel_related.delete_moodLabel;

/**
 * Represents the state after a delete mood label use case is executed.
 * This state is typically held in the ViewModel and passed to the View.
 */
public class DeleteMoodLabelState {

    private String deletedMoodLabelName;
    private boolean isDeletedSuccessfully;
    private String deleteError; // null if no error

    public DeleteMoodLabelState() {
        // Default empty state
    }

    public String getDeletedMoodLabelName() {
        return deletedMoodLabelName;
    }

    public void setDeletedMoodLabelName(String deletedMoodLabelName) {
        this.deletedMoodLabelName = deletedMoodLabelName;
    }

    public boolean isDeletedSuccessfully() {
        return isDeletedSuccessfully;
    }

    public void setDeletedSuccessfully(boolean deletedSuccessfully) {
        isDeletedSuccessfully = deletedSuccessfully;
    }

    public String getDeleteError() {
        return deleteError;
    }

    public void setDeleteError(String deleteError) {
        this.deleteError = deleteError;
    }
}

