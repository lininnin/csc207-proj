package interface_adapter.controller;

import use_case.Angela.task.edit_available.EditAvailableTaskInputBoundary;
import use_case.Angela.task.edit_available.EditAvailableTaskInputData;

/**
 * Controller for editing available tasks.
 */
public class EditAvailableTaskController {
    private final EditAvailableTaskInputBoundary editTaskUseCase;

    public EditAvailableTaskController(EditAvailableTaskInputBoundary editTaskUseCase) {
        this.editTaskUseCase = editTaskUseCase;
    }

    /**
     * Edits an available task.
     *
     * @param taskId The ID of the task to edit
     * @param name The new name
     * @param description The new description
     * @param categoryId The new category ID
     */
    public void editTask(String taskId, String name, String description, String categoryId) {
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                taskId, name, description, categoryId
        );
        editTaskUseCase.execute(inputData);
    }
}