package use_case.Alex.todays_events.edit_todays_event;

public interface EditTodaysEventInputBoundary {
    /**
     * Executes the edit event use case with the provided input data.
     *
     * @param inputData The data needed to perform the edit operation.
     */
    void execute(EditTodaysEventInputData inputData);
}
