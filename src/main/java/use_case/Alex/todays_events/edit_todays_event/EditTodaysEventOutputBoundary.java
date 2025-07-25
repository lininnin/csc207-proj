package use_case.Alex.todays_events.edit_todays_event;

public interface EditTodaysEventOutputBoundary {
    /**
     * Called when the edit event use case is successful.
     *
     * @param outputData Data to be passed to the presenter/view.
     */
    void prepareSuccessView(EditTodaysEventOutputData outputData);

    /**
     * Called when the edit event use case fails (e.g. invalid input, event not found).
     *
     * @param outputData Data containing failure info.
     */
    void prepareFailView(EditTodaysEventOutputData outputData);
}
