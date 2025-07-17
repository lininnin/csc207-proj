package interface_adapter;

import use_case.MarkTaskCompleteUseCase;
import use_case.MarkTaskCompleteInputData;

/**
 * Controller for marking tasks as complete.
 */
public class MarkTaskCompleteController {
    private final MarkTaskCompleteUseCase markTaskCompleteUseCase;

    public MarkTaskCompleteController(MarkTaskCompleteUseCase markTaskCompleteUseCase) {
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
