package use_case.Alex.avaliable_events_module.delete_event;

/**
 * Output boundary for the DeleteAvailableEvent use case.
 * Presenter implements this interface to receive results from the Interactor.
 */
public interface DeleteEventOutputBoundary {

    /**
     * Called when the delete operation succeeds.
     *
     * @param outputData The output data containing event info.
     */
    void prepareSuccessView(DeleteEventOutputData outputData);

    /**
     * Called when the delete operation fails.
     *
     * @param outputData The output data containing failure reason.
     */
    void prepareFailView(DeleteEventOutputData outputData);
}

