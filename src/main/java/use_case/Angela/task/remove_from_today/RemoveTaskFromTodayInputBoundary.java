package use_case.Angela.task.remove_from_today;

/**
 * Input boundary for the remove task from today use case.
 */
public interface RemoveTaskFromTodayInputBoundary {
    /**
     * Removes a task from today with the given data.
     *
     * @param inputData The task removal data
     */
    void execute(RemoveTaskFromTodayInputData inputData);
}
