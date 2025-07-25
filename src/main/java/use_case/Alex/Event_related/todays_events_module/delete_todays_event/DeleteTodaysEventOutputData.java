package use_case.Alex.Event_related.todays_events_module.delete_todays_event;

public class DeleteTodaysEventOutputData {

    private final String eventId;
    private final String eventName;
    private final boolean deletionSuccess;
    private final String errorMessage;

    /**
     * Constructor for successful deletion.
     *
     * @param eventId   ID of the deleted event
     * @param eventName Name of the deleted event
     */
    public DeleteTodaysEventOutputData(String eventId, String eventName) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.deletionSuccess = true;
        this.errorMessage = null;
    }

    /**
     * Constructor for failed deletion.
     *
     * @param eventId      ID of the attempted event
     * @param errorMessage Reason why deletion failed
     */
    public DeleteTodaysEventOutputData(String eventId, String errorMessage, boolean failed) {
        this.eventId = eventId;
        this.eventName = null;
        this.deletionSuccess = false;
        this.errorMessage = errorMessage;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public boolean isDeletionSuccess() {
        return deletionSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
