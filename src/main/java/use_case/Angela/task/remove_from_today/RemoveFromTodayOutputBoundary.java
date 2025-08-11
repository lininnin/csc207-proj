package use_case.Angela.task.remove_from_today;

/**
 * Output boundary interface for the remove from today use case.
 */
public interface RemoveFromTodayOutputBoundary {
    /**
     * Prepares the success view after successfully removing a task from today.
     *
     * @param outputData the output data containing the result
     */
    void prepareSuccessView(RemoveFromTodayOutputData outputData);

    /**
     * Prepares the fail view when removal fails.
     *
     * @param error the error message
     */
    void prepareFailView(String error);
}