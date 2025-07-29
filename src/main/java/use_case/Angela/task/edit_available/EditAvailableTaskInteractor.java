package use_case.Angela.task.edit_available;

import entity.info.Info;
import use_case.Angela.task.TaskGateway;
import use_case.Angela.category.CategoryGateway;

/**
 * Interactor for the edit available task use case.
 * Implements the business logic for editing an available task.
 */
public class EditAvailableTaskInteractor implements EditAvailableTaskInputBoundary {
    private final TaskGateway taskGateway;
    private final CategoryGateway categoryGateway;
    private final EditAvailableTaskOutputBoundary outputBoundary;

    public EditAvailableTaskInteractor(TaskGateway taskGateway,
                                       CategoryGateway categoryGateway,
                                       EditAvailableTaskOutputBoundary outputBoundary) {
        this.taskGateway = taskGateway;
        this.categoryGateway = categoryGateway;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(EditAvailableTaskInputData inputData) {
        String taskId = inputData.getTaskId();
        String name = inputData.getName();
        String description = inputData.getDescription();
        String categoryId = inputData.getCategoryId();

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
        if (!taskGateway.existsInAvailable(taskId)) {
            outputBoundary.prepareFailView("Task not found in Available Tasks");
            return;
        }

        // Get current task name for comparison
        String currentName = taskGateway.getTaskName(taskId);

        // Check if new name already exists (unless it's the same task)
        if (!name.equals(currentName) && taskGateway.availableTaskNameExists(name)) {
            outputBoundary.prepareFailView("The Task name already exists");
            return;
        }

        // Validate category if provided
        String categoryName = "";
        if (categoryId != null && !categoryId.isEmpty()) {
            var category = categoryGateway.getCategoryById(categoryId);
            if (category == null) {
                outputBoundary.prepareFailView("Invalid category selected");
                return;
            }
            categoryName = category.getName();
        }

        // Create updated Info object
        Info.Builder builder = new Info.Builder(name);
        if (description != null && !description.trim().isEmpty()) {
            builder.description(description);
        }
        if (!categoryName.isEmpty()) {
            builder.category(categoryName);
        }

        Info updatedInfo = builder.build();
        // Preserve the original ID
        updatedInfo.setName(name); // Using setter to preserve ID

        // Update the task
        boolean updated = taskGateway.updateAvailableTask(updatedInfo);

        if (updated) {
            EditAvailableTaskOutputData outputData = new EditAvailableTaskOutputData(
                    taskId, name, "Task updated successfully"
            );
            outputBoundary.prepareSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Failed to update task");
        }
    }
}