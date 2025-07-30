package use_case.Alex.Event_related.avaliable_events_module.edit_event;

public interface EditEventOutputBoundary {
    /**
     * Called when the edit event use case is successful.
     *
     * @param outputData Data to be passed to the presenter/view.
     */
    void prepareSuccessView(EditEventOutputData outputData);

    /**
     * Called when the edit event use case fails (e.g. invalid input, event not found).
     *
     * @param outputData Data containing failure info.
     */
    void prepareFailView(EditEventOutputData outputData);
}

