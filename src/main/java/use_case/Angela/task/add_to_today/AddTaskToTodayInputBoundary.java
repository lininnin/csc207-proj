package use_case.Angela.task.add_to_today;

/**
 * Input boundary for adding a task to today.
 */
public interface AddTaskToTodayInputBoundary {
    /**
     * Adds a task to today with the given data.
     *
     * @param inputData The input data
     */
    void execute(AddTaskToTodayInputData inputData);
}