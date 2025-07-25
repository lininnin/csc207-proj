package use_case.Alex.todays_events.delete_todays_event;

public interface DeleteTodaysEventOutputBoundary {

    /**
     * Called when the delete operation succeeds.
     *
     * @param outputData The output data containing event info.
     */
    void prepareSuccessView(DeleteTodaysEventOutputData outputData);

    /**
     * Called when the delete operation fails.
     *
     * @param outputData The output data containing failure reason.
     */
    void prepareFailView(DeleteTodaysEventOutputData outputData);

}
