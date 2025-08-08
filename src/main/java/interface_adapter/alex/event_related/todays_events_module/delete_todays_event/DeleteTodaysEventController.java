package interface_adapter.alex.event_related.todays_events_module.delete_todays_event;

import use_case.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventInputBoundary;
import use_case.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventInputData;

/**
 * Controller for the DeleteAvailableEvent use case.
 * Invoked by the View (e.g., AvailableEventsView) when a user triggers a delete action.
 */
public class DeleteTodaysEventController {

    private final DeleteTodaysEventInputBoundary interactor;

    /**
     * Constructs the controller with a given interactor (use case).
     * @param interactor The input boundary for the delete use case.
     */
    public DeleteTodaysEventController(DeleteTodaysEventInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the delete use case with the given event ID.
     * @param eventId The ID of the event to delete.
     */
    public void delete(String eventId) {
        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);
        interactor.execute(inputData);
    }
}
