package use_case.Angela.task.mark_complete;

import entity.Angela.Task.Task;
import java.time.LocalDateTime;

/**
 * Interactor for marking a task complete or incomplete.
 * Handles the business logic including one-time task removal.
 */
public class MarkTaskCompleteInteractor implements MarkTaskCompleteInputBoundary {
    private final MarkTaskCompleteDataAccessInterface dataAccess;
    private final MarkTaskCompleteOutputBoundary outputBoundary;

    public MarkTaskCompleteInteractor(MarkTaskCompleteDataAccessInterface dataAccess,
                                      MarkTaskCompleteOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(MarkTaskCompleteInputData inputData) {
        String taskId = inputData.getTaskId();
        boolean markAsComplete = inputData.isMarkAsComplete();

        // Get the task from today's list
        Task task = dataAccess.getTodayTaskById(taskId);
        if (task == null) {
            outputBoundary.presentError("Task not found in Today's Tasks");
            return;
        }

        // Update the task's completion status
        boolean updated = dataAccess.updateTaskCompletionStatus(taskId, markAsComplete);
        if (!updated) {
            outputBoundary.presentError("Failed to update task completion status");
            return;
        }

        // One-time tasks are NOT removed when completed - they're removed at midnight reset

        // Create success output
        String taskName = task.getInfo().getName();
        String message = markAsComplete ? 
            "Task \"" + taskName + "\" marked as complete" : 
            "Task \"" + taskName + "\" marked as incomplete";

        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(
                taskId, taskName, LocalDateTime.now(), 0.0 // We'll calculate completion rate in presenter
        );
        outputBoundary.presentSuccess(outputData, message);
    }
}