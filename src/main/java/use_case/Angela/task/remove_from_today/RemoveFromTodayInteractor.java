package use_case.Angela.task.remove_from_today;

import entity.Angela.Task.TaskInterf;

/**
 * Interactor for the remove from today use case.
 * Handles the business logic for removing a task from today's list.
 */
public class RemoveFromTodayInteractor implements RemoveFromTodayInputBoundary {
    private final RemoveFromTodayDataAccessInterface dataAccess;
    private final RemoveFromTodayOutputBoundary presenter;

    public RemoveFromTodayInteractor(RemoveFromTodayDataAccessInterface dataAccess,
                                    RemoveFromTodayOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(RemoveFromTodayInputData inputData) {
        String taskId = inputData.getTaskId();

        // Get the task to verify it exists and get its name for the message
        TaskInterf task = dataAccess.getTodayTaskById(taskId);
        if (task == null) {
            presenter.prepareFailView("Task not found in Today's list");
            return;
        }

        String taskName = task.getInfo().getName();

        // Remove the task from today's list only
        boolean removed = dataAccess.removeFromTodaysList(taskId);
        
        if (removed) {
            String message = "Task '" + taskName + "' removed from Today's list";
            RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);
            presenter.prepareSuccessView(outputData);
        } else {
            presenter.prepareFailView("Failed to remove task from Today's list");
        }
    }
}