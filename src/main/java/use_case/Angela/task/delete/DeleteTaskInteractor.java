package use_case.Angela.task.delete;

import use_case.Angela.task.TaskGateway;

/**
 * Interactor for the delete task use case.
 * Implements the business logic for deleting a task.
 */
public class DeleteTaskInteractor implements DeleteTaskInputBoundary {
    private final TaskGateway taskGateway;
    private final DeleteTaskOutputBoundary outputBoundary;

    public DeleteTaskInteractor(TaskGateway taskGateway,
                                DeleteTaskOutputBoundary outputBoundary) {
        this.taskGateway = taskGateway;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(DeleteTaskInputData inputData) {
        String taskId = inputData.getTaskId();

        // Check if task exists in Available Tasks
        if (!taskGateway.existsInAvailable(taskId)) {
            outputBoundary.prepareFailView("Task not found in Available Tasks");
            return;
        }

        // Check if task also exists in Today's Tasks
        boolean existsInToday = taskGateway.existsInToday(taskId);

        if (existsInToday && inputData.isFromAvailable()) {
            // Show warning dialog
            String taskName = taskGateway.getTaskName(taskId);
            outputBoundary.showDeleteFromBothWarning(taskId, taskName);
            return;
        }

        // Delete the task
        boolean deleted = taskGateway.deleteTaskCompletely(taskId);

        if (deleted) {
            String message = existsInToday ?
                    "Task deleted from both Available and Today's lists" :
                    "Task deleted successfully";

            DeleteTaskOutputData outputData = new DeleteTaskOutputData(
                    taskId, message, existsInToday
            );
            outputBoundary.prepareSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Failed to delete task");
        }
    }
}