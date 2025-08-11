package interface_adapter.Angela.task.delete;

import use_case.Angela.task.delete.DeleteTaskInputBoundary;
import use_case.Angela.task.delete.DeleteTaskInputData;

/**
 * Controller for the delete task use case.
 */
public class DeleteTaskController {
    private final DeleteTaskInputBoundary deleteTaskInteractor;

    public DeleteTaskController(DeleteTaskInputBoundary deleteTaskInteractor) {
        this.deleteTaskInteractor = deleteTaskInteractor;
    }

    /**
     * Executes the delete task use case.
     *
     * @param taskId The ID of the task to delete
     * @param isFromAvailable Whether deletion is from Available Tasks view
     */
    public void execute(String taskId, boolean isFromAvailable) {
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);
        deleteTaskInteractor.execute(inputData);
    }

    /**
     * Confirms deletion of a task that exists in both lists.
     *
     * @param taskId The ID of the task to delete
     */
    public void confirmDeleteFromBoth(String taskId) {
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, false);
        deleteTaskInteractor.execute(inputData);
    }
}