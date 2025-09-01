package interface_adapter.Angela.task.edit_available;

import use_case.Angela.task.edit_available.EditAvailableTaskInputBoundary;
import use_case.Angela.task.edit_available.EditAvailableTaskInputData;

/**
 * Controller for editing available tasks.
 * Follows Clean Architecture pattern - converts UI input to use case input.
 */
public class EditAvailableTaskController {
    private final EditAvailableTaskInputBoundary editAvailableTaskInteractor;

    public EditAvailableTaskController(EditAvailableTaskInputBoundary editAvailableTaskInteractor) {
        this.editAvailableTaskInteractor = editAvailableTaskInteractor;
    }

    /**
     * Executes the edit available task use case.
     *
     * @param taskId The ID of the task to edit
     * @param name The new name for the task
     * @param description The new description for the task
     * @param categoryId The new category ID for the task
     * @param isOneTime Whether the task is a one-time task
     */
    public void execute(String taskId, String name, String description, 
                        String categoryId, boolean isOneTime) {
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                taskId, name, description, categoryId, isOneTime
        );
        
        editAvailableTaskInteractor.execute(inputData);
    }
}