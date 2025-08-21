package use_case.Angela.task.edit_available;

import entity.Angela.Task.TaskAvailable;

/**
 * Interactor for the edit available task use case.
 * Implements the business logic for editing an available task.
 */
public class EditAvailableTaskInteractor implements EditAvailableTaskInputBoundary {
    private final EditAvailableTaskDataAccessInterface dataAccess;
    private final EditAvailableTaskCategoryDataAccessInterface categoryDataAccess;
    private final EditAvailableTaskOutputBoundary outputBoundary;

    public EditAvailableTaskInteractor(EditAvailableTaskDataAccessInterface dataAccess,
                                       EditAvailableTaskCategoryDataAccessInterface categoryDataAccess,
                                       EditAvailableTaskOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.categoryDataAccess = categoryDataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(EditAvailableTaskInputData inputData) {
        String taskId = inputData.getTaskId();
        String name = inputData.getName();
        String description = inputData.getDescription();
        String categoryId = inputData.getCategoryId();
        boolean isOneTime = inputData.isOneTime();
        
        System.out.println("DEBUG: EditAvailableTaskInteractor.execute - taskId: " + taskId + 
                         ", name: " + name + ", isOneTime: " + isOneTime);

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            outputBoundary.prepareFailView("Task name cannot be empty");
            return;
        }

        if (name.length() > 20) {
            outputBoundary.prepareFailView("The name of Task cannot exceed 20 letters");
            return;
        }

        // Validate description
        if (description != null && description.length() > 100) {
            outputBoundary.prepareFailView("Description cannot exceed 100 characters");
            return;
        }

        // Check if task exists
        TaskAvailable existingTask = dataAccess.getTaskAvailableById(taskId);
        if (existingTask == null) {
            outputBoundary.prepareFailView("Task not found in Available Tasks");
            return;
        }

        // Check for duplicate name in same category (excluding current task)
        if (dataAccess.taskExistsWithNameAndCategoryExcluding(name, categoryId, taskId)) {
            outputBoundary.prepareFailView("A task with this name already exists in the same category");
            return;
        }

        // Validate category if provided
        if (categoryId != null && !categoryId.isEmpty()) {
            var category = categoryDataAccess.getCategoryById(categoryId);
            if (category == null) {
                outputBoundary.prepareFailView("Invalid category selected");
                return;
            }
        }

        // Update the task
        boolean updated = dataAccess.updateAvailableTask(taskId, name, description, categoryId, isOneTime);
        System.out.println("DEBUG: Task update result: " + updated);

        if (updated) {
            EditAvailableTaskOutputData outputData = new EditAvailableTaskOutputData(
                    taskId, name, "Task updated successfully"
            );
            System.out.println("DEBUG: Calling prepareSuccessView");
            outputBoundary.prepareSuccessView(outputData);
        } else {
            System.out.println("DEBUG: Update failed, calling prepareFailView");
            outputBoundary.prepareFailView("Failed to update task");
        }
    }
}