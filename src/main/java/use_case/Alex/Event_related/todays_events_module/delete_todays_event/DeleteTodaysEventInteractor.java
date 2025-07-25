package use_case.Alex.Event_related.todays_events_module.delete_todays_event;

import entity.Alex.Event.Event;

/**
 * Interactor for the DeleteAvailableEvent use case.
 * Executes deletion logic and calls presenter with result.
 */
public class DeleteTodaysEventInteractor implements DeleteTodaysEventInputBoundary {

    private final DeleteTodaysEventDataAccessInterf dataAccess;
    private final DeleteTodaysEventOutputBoundary outputBoundary;

    /**
     * Constructs the interactor with dependencies.
     *
     * @param dataAccess     Data access interface for available events.
     * @param outputBoundary Presenter for handling output data.
     */
    public DeleteTodaysEventInteractor(DeleteTodaysEventDataAccessInterf dataAccess,
                                       DeleteTodaysEventOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(DeleteTodaysEventInputData inputData) {
        String eventId = inputData.getEventId();

        // Step 1: 查找事件
        Event eventToDelete = dataAccess.getEventById(eventId);
        if (eventToDelete == null || !dataAccess.contains(eventToDelete)) {
            DeleteTodaysEventOutputData failOutput = new DeleteTodaysEventOutputData(
                    eventId, "Event not found or already deleted", false);
            outputBoundary.prepareFailView(failOutput);
            return;
        }

        // Step 2: 删除事件
        boolean success = dataAccess.remove(eventToDelete);
        if (success) {
            DeleteTodaysEventOutputData successOutput = new DeleteTodaysEventOutputData(
                    eventToDelete.getInfo().getId(), eventToDelete.getInfo().getName());
            outputBoundary.prepareSuccessView(successOutput);
        } else {
            DeleteTodaysEventOutputData failOutput = new DeleteTodaysEventOutputData(
                    eventToDelete.getInfo().getId(), "Deletion failed due to unknown error", false);
            outputBoundary.prepareFailView(failOutput);
        }
    }
}
