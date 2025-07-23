package use_case.Alex.delete_event;

/**
 * Input data class for the DeleteAvailableEvent use case.
 * Contains only the ID of the event to be deleted.
 */
public class DeleteEventInputData {

    private final String eventId;

    /**
     * Constructor for DeleteAvailableEventInputData.
     *
     * @param eventId Unique identifier of the event to be deleted (required)
     */
    public DeleteEventInputData(String eventId) {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new IllegalArgumentException("eventId cannot be null or empty");
        }
        this.eventId = eventId.trim();
    }

    /**
     * Returns the ID of the event to delete.
     */
    public String getEventId() {
        return eventId;
    }
}

