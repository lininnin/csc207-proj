package interface_adapter.alex.event_related.todays_events_module.delete_todays_event;


/**
 * Represents the state after a delete event use case is executed.
 * This state is typically held in the ViewModel and passed to the View.
 */
public class DeleteTodaysEventState {

    private String deletedEventId;
    private String deletedEventName;
    private boolean isDeletedSuccessfully;
    private String deleteError; // null if no error

    public DeleteTodaysEventState() {
    }

    public DeleteTodaysEventState(String deletedEventId, String deletedEventName,
                                  boolean isDeletedSuccessfully, String deleteError) {
        this.deletedEventId = deletedEventId;
        this.deletedEventName = deletedEventName;
        this.isDeletedSuccessfully = isDeletedSuccessfully;
        this.deleteError = deleteError;
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

    @Override
    public String toString() {
        return "DeleteTodaysEventState{" +
                "deletedEventId='" + deletedEventId + '\'' +
                ", deletedEventName='" + deletedEventName + '\'' +
                ", isDeletedSuccessfully=" + isDeletedSuccessfully +
                ", deleteError='" + deleteError + '\'' +
                '}';
    }
}
