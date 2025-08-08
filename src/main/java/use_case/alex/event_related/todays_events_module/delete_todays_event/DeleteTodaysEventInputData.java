package use_case.alex.event_related.todays_events_module.delete_todays_event;

public class DeleteTodaysEventInputData {

    private final String eventId;

    /**
     * Constructor for DeleteAvailableEventInputData.
     *
     * @param eventId Unique identifier of the event to be deleted (required)
     */
    public DeleteTodaysEventInputData(String eventId) {
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
