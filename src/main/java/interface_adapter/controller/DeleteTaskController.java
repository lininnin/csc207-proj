package interface_adapter.controller;

import use_case.Angela.task.delete.DeleteTaskInputBoundary;
import use_case.Angela.task.delete.DeleteTaskInputData;

/**
 * Controller for deleting tasks.
 * Handles user actions from the task view.
 */
public class DeleteTaskController {
    private final DeleteTaskInputBoundary deleteTaskUseCase;

    public DeleteTaskController(DeleteTaskInputBoundary deleteTaskUseCase) {
        this.deleteTaskUseCase = deleteTaskUseCase;
    }

    /**
     * Deletes a task from available tasks.
     *
     * @param taskId The ID of the task to delete
     */
    public void deleteFromAvailable(String taskId) {
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, true);
        deleteTaskUseCase.execute(inputData);
    }

    /**
     * Confirms deletion of a task that exists in both lists.
     *
     * @param taskId The ID of the task to delete
     */
    public void confirmDeleteFromBoth(String taskId) {
        // This would be called after user confirms the warning dialog
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, false);
        deleteTaskUseCase.execute(inputData);
    }
}