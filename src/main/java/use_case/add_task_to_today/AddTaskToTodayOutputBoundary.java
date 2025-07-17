package use_case.add_task_to_today;

/**
 * Output boundary interface.
 */
public interface AddTaskToTodayOutputBoundary {
    void presentSuccess(AddTaskToTodayOutputData outputData);
    void presentError(String error);
}
