package use_case.Angela.task.remove_from_today;

/**
 * Output boundary for the remove task from today use case.
 */
public interface RemoveTaskFromTodayOutputBoundary {
    /**
     * Prepares the success view after task removal.
     *
     * @param outputData The output data
     */
    void prepareSuccessView(RemoveTaskFromTodayOutputData outputData);

    /**
     * Prepares the fail view with an error message.
     *
     * @param error The error message
     */
    void prepareFailView(String error);
}