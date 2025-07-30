package interface_adapter.Alex.Event_related.available_event_module.delete_event;

import use_case.Alex.Event_related.avaliable_events_module.delete_event.DeleteEventInputBoundary;
import use_case.Alex.Event_related.avaliable_events_module.delete_event.DeleteEventInputData;

/**
 * Controller for the DeleteAvailableEvent use case.
 * Invoked by the View (e.g., AvailableEventsView) when a user triggers a delete action.
 */
public class DeleteEventController {

    private final DeleteEventInputBoundary interactor;

    /**
     * Constructs the controller with a given interactor (use case).
     * @param interactor The input boundary for the delete use case.
     */
    public DeleteEventController(DeleteEventInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the delete use case with the given event ID.
     * @param eventId The ID of the event to delete.
     */
    public void delete(String eventId) {
        DeleteEventInputData inputData = new DeleteEventInputData(eventId);
        interactor.execute(inputData);
    }
}

