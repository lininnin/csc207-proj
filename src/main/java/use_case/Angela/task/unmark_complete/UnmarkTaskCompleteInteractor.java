package use_case.Angela.task.unmark_complete;

import entity.Task;
import use_case.Angela.task.TaskGateway;

/**
 * Interactor for the unmark task complete use case.
 * Implements the business logic for reverting task completion.
 */
public class UnmarkTaskCompleteInteractor implements UnmarkTaskCompleteInputBoundary {
    private final TaskGateway taskGateway;
    private final UnmarkTaskCompleteOutputBoundary outputBoundary;

    public UnmarkTaskCompleteInteractor(TaskGateway taskGateway,
                                        UnmarkTaskCompleteOutputBoundary outputBoundary) {
        this.taskGateway = taskGateway;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(UnmarkTaskCompleteInputData inputData) {
        String taskId = inputData.getTaskId();

        // Check if task exists in Today's Tasks
        if (!taskGateway.existsInToday(taskId)) {
            outputBoundary.prepareFailView("Task not found in Today's Tasks");
            return;
        }

        // Get current task and check if it's actually completed
        Task currentTask = null;
        for (Task task : taskGateway.getTodaysTasks()) {
            if (task.getInfo().getId().equals(taskId)) {
                currentTask = task;
                break;
            }
        }

        if (currentTask == null) {
            outputBoundary.prepareFailView("Task not found");
            return;
        }

        if (!currentTask.isComplete()) {
            outputBoundary.prepareFailView("Task is not marked as complete");
            return;
        }

        // Unmark the task as complete
        boolean unmarked = taskGateway.unmarkTaskComplete(taskId);

        if (unmarked) {
            // The task should be moved back to its original position
            // For now, we'll return position 0, but in a real implementation,
            // we would track the original position
            UnmarkTaskCompleteOutputData outputData = new UnmarkTaskCompleteOutputData(
                    taskId,
                    currentTask.getInfo().getName(),
                    0 // Original position placeholder
            );
            outputBoundary.prepareSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Failed to unmark task as complete");
        }
    }
}