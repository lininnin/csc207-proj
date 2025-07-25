package use_case.Alex.avaliable_events_module.delete_event;

/**
 * Output data class for the DeleteAvailableEvent use case.
 * Contains the result of the deletion operation and relevant event info.
 */
public class DeleteEventOutputData {

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
    public DeleteEventOutputData(String eventId, String eventName) {
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
    public DeleteEventOutputData(String eventId, String errorMessage, boolean failed) {
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

