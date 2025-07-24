package use_case.task.add_to_today;

/**
 * Input boundary for adding tasks to today's list.
 */
public interface AddTaskToTodayInputBoundary {
    /**
     * Adds an available task to today's active tasks.
     *
     * @param inputData The input data containing task ID, priority, and due date
     */
    void addTaskToToday(AddTaskToTodayInputData inputData);
}