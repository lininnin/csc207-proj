package use_case.Angela.task.remove_from_today;

/**
 * Input boundary interface for the remove from today use case.
 */
public interface RemoveFromTodayInputBoundary {
    /**
     * Removes a task from today's list.
     *
     * @param inputData the input data containing the task ID
     */
    void execute(RemoveFromTodayInputData inputData);
}