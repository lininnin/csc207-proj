package use_case.Angela.task.remove_from_today;

import use_case.Angela.task.TaskGateway;

/**
 * Interactor for the remove task from today use case.
 * Implements the business logic for removing a task from Today's Tasks only.
 */
public class RemoveTaskFromTodayInteractor implements RemoveTaskFromTodayInputBoundary {
    private final TaskGateway taskGateway;
    private final RemoveTaskFromTodayOutputBoundary outputBoundary;

    public RemoveTaskFromTodayInteractor(TaskGateway taskGateway,
                                         RemoveTaskFromTodayOutputBoundary outputBoundary) {
        this.taskGateway = taskGateway;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(RemoveTaskFromTodayInputData inputData) {
        String taskId = inputData.getTaskId();

        // Check if task exists in Today's Tasks
        if (!taskGateway.existsInToday(taskId)) {
            outputBoundary.prepareFailView("Task not found in Today's Tasks");
            return;
        }

        // Get task name for output
        String taskName = taskGateway.getTaskName(taskId);

        // Remove the task from Today's Tasks only
        boolean removed = taskGateway.removeFromToday(taskId);

        if (removed) {
            RemoveTaskFromTodayOutputData outputData = new RemoveTaskFromTodayOutputData(
                    taskId,
                    taskName,
                    "Task removed from Today's Tasks. It remains available in Available Tasks."
            );
            outputBoundary.prepareSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Failed to remove task from today");
        }
    }
}