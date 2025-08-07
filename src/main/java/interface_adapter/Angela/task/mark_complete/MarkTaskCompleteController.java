package interface_adapter.Angela.task.mark_complete;

import use_case.Angela.task.mark_complete.MarkTaskCompleteInputBoundary;
import use_case.Angela.task.mark_complete.MarkTaskCompleteInputData;

/**
 * Controller for marking tasks as complete or incomplete.
 */
public class MarkTaskCompleteController {
    private final MarkTaskCompleteInputBoundary markTaskCompleteInteractor;

    public MarkTaskCompleteController(MarkTaskCompleteInputBoundary markTaskCompleteInteractor) {
        this.markTaskCompleteInteractor = markTaskCompleteInteractor;
    }

    /**
     * Marks a task as complete or incomplete.
     *
     * @param taskId The ID of the task to update
     * @param markAsComplete true to mark as complete, false to mark as incomplete
     */
    public void execute(String taskId, boolean markAsComplete) {
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);
        markTaskCompleteInteractor.execute(inputData);
    }
}