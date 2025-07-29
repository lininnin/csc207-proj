package use_case.add_task_to_today;

/**
 * Output boundary for adding task to today use case.
 */
public interface AddTaskToTodayOutputBoundary {
    void presentSuccess(AddTaskToTodayOutputData outputData);
    void presentError(String error);
}