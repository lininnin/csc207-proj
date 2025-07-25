package use_case.Alex.todays_events.delete_todays_event;

public interface DeleteTodaysEventInputBoundary {
    /**
     * Executes the delete use case with the provided input data.
     *
     * @param inputData The input data containing event ID (and possibly more info).
     */
    void execute(DeleteTodaysEventInputData inputData);
}
