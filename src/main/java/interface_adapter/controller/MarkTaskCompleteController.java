package interface_adapter.controller;

import use_case.Angela.task.mark_task_complete.MarkTaskCompleteInteractor;
import use_case.Angela.task.mark_task_complete.MarkTaskCompleteInputData;

/**
 * Controller for marking tasks as complete.
 */
public class MarkTaskCompleteController {
    private final MarkTaskCompleteInteractor markTaskCompleteUseCase;

    public MarkTaskCompleteController(MarkTaskCompleteInteractor markTaskCompleteUseCase) {
        this.markTaskCompleteUseCase = markTaskCompleteUseCase;
    }

    /**
     * Marks a task as complete.
     *
     * @param taskId The ID of the task to complete
     */
    public void markTaskComplete(String taskId) {
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId);
        markTaskCompleteUseCase.execute(inputData);
    }
}
