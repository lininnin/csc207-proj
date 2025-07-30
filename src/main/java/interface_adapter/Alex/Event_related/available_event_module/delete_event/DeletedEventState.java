package interface_adapter.Alex.Event_related.available_event_module.delete_event;

/**
 * Represents the state after a delete event use case is executed.
 * This state is typically held in the ViewModel and passed to the View.
 */
public class DeletedEventState {

    private String deletedEventId;
    private String deletedEventName;
    private boolean isDeletedSuccessfully;
    private String deleteError; // null if no error

    public DeletedEventState() {
        // Default empty state
    }

    public String getDeletedEventId() {
        return deletedEventId;
    }

    public void setDeletedEventId(String deletedEventId) {
        this.deletedEventId = deletedEventId;
    }

    public String getDeletedEventName() {
        return deletedEventName;
    }

    public void setDeletedEventName(String deletedEventName) {
        this.deletedEventName = deletedEventName;
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

