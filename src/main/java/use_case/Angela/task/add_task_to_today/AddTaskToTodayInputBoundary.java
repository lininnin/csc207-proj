package use_case.add_task_to_today;

/**
 * Input boundary for adding task to today use case.
 */
public interface AddTaskToTodayInputBoundary {
    void execute(AddTaskToTodayInputData inputData);
}