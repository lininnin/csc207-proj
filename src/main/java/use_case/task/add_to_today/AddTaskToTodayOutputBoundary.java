package use_case.task.add_to_today;

/**
 * Output boundary for adding tasks to today's list.
 */
public interface AddTaskToTodayOutputBoundary {
    /**
     * Presents successful addition to today's list.
     *
     * @param outputData The result data
     */
    void presentSuccess(AddTaskToTodayOutputData outputData);

    /**
     * Presents addition failure.
     *
     * @param error The error message
     */
    void presentError(String error);
}