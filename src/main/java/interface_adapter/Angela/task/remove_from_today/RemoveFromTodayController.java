package interface_adapter.Angela.task.remove_from_today;

import use_case.Angela.task.remove_from_today.RemoveFromTodayInputBoundary;
import use_case.Angela.task.remove_from_today.RemoveFromTodayInputData;

/**
 * Controller for the remove from today use case.
 */
public class RemoveFromTodayController {
    private final RemoveFromTodayInputBoundary interactor;

    public RemoveFromTodayController(RemoveFromTodayInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the remove from today use case.
     *
     * @param taskId the ID of the task to remove from today
     */
    public void execute(String taskId) {
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(taskId);
        interactor.execute(inputData);
    }
}