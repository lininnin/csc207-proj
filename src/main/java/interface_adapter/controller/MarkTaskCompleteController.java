package interface_adapter.controller;

import use_case.Angela.task.mark_complete.MarkTaskCompleteInputBoundary;
import use_case.Angela.task.mark_complete.MarkTaskCompleteInputData;

/**
 * Controller for marking tasks complete.
 */
public class MarkTaskCompleteController {
    private final MarkTaskCompleteInputBoundary markTaskCompleteUseCase;

    public MarkTaskCompleteController(MarkTaskCompleteInputBoundary markTaskCompleteUseCase) {
        this.markTaskCompleteUseCase = markTaskCompleteUseCase;
    }

    /**
     * Marks a task as complete.
     *
     * @param taskId The ID of the task to mark complete
     */
    public void markTaskComplete(String taskId) {
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId);
        markTaskCompleteUseCase.execute(inputData);
    }
}