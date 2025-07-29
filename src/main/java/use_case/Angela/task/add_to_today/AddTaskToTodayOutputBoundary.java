package use_case.Angela.task.add_to_today;

/**
 * Output boundary for adding a task to today.
 */
public interface AddTaskToTodayOutputBoundary {
    /**
     * Prepares the success view.
     *
     * @param outputData The output data
     */
    void presentSuccess(AddTaskToTodayOutputData outputData);

    /**
     * Prepares the error view.
     *
     * @param error The error message
     */
    void presentError(String error);
}