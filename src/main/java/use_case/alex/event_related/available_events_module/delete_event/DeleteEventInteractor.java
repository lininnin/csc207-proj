package use_case.alex.event_related.available_events_module.delete_event;

import entity.info.InfoInterf;

/**
 * Interactor for the DeleteAvailableEvent use case.
 * Executes deletion logic and calls presenter with result.
 */
public class DeleteEventInteractor implements DeleteEventInputBoundary {

    private final DeleteEventDataAccessInterf dataAccess;
    private final DeleteEventOutputBoundary outputBoundary;

    /**
     * Constructs the interactor with dependencies.
     *
     * @param dataAccess     Data access interface for available events.
     * @param outputBoundary Presenter for handling output data.
     */
    public DeleteEventInteractor(DeleteEventDataAccessInterf dataAccess,
                                 DeleteEventOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(DeleteEventInputData inputData) {
        String eventId = inputData.getEventId();

        // Step 1: 查找事件
        InfoInterf eventToDelete = dataAccess.getEventById(eventId);
        if (eventToDelete == null || !dataAccess.contains(eventToDelete)) {
            DeleteEventOutputData failOutput = new DeleteEventOutputData(
                    eventId, "Event not found or already deleted", false);
            outputBoundary.prepareFailView(failOutput);
            return;
        }

        // Step 2: 删除事件
        boolean success = dataAccess.remove(eventToDelete);
        if (success) {
            DeleteEventOutputData successOutput = new DeleteEventOutputData(
                    eventToDelete.getId(), eventToDelete.getName());
            outputBoundary.prepareSuccessView(successOutput);
        } else {
            DeleteEventOutputData failOutput = new DeleteEventOutputData(
                    eventToDelete.getId(), "Deletion failed due to unknown error", false);
            outputBoundary.prepareFailView(failOutput);
        }
    }
}

