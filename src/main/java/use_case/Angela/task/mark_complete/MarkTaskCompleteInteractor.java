package use_case.Angela.task.mark_complete;

import use_case.Angela.task.TaskGateway;
import java.time.LocalDateTime;

/**
 * Interactor for marking a task complete.
 */
public class MarkTaskCompleteInteractor implements MarkTaskCompleteInputBoundary {
    private final TaskGateway taskGateway;
    private final MarkTaskCompleteOutputBoundary outputBoundary;

    public MarkTaskCompleteInteractor(TaskGateway taskGateway,
                                      MarkTaskCompleteOutputBoundary outputBoundary) {
        this.taskGateway = taskGateway;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(MarkTaskCompleteInputData inputData) {
        String taskId = inputData.getTaskId();

        // Check if task exists in today
        if (!taskGateway.existsInToday(taskId)) {
            outputBoundary.presentError("Task not found in Today's Tasks");
            return;
        }

        // Mark complete
        boolean marked = taskGateway.markTaskComplete(taskId);

        if (marked) {
            String taskName = taskGateway.getTaskName(taskId);
            double completionRate = taskGateway.getTodaysCompletionRate();

            MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(
                    taskId, taskName, LocalDateTime.now(), completionRate
            );
            outputBoundary.presentSuccess(outputData);
        } else {
            outputBoundary.presentError("Failed to mark task as complete");
        }
    }
}