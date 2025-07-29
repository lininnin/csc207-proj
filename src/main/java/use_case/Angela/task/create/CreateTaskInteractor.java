package use_case.Angela.task.create;

import entity.info.Info;
import use_case.Angela.task.TaskGateway;
import use_case.Angela.category.CategoryGateway;

/**
 * Interactor for the create task use case.
 * Implements the business logic for creating a new task.
 */
public class CreateTaskInteractor implements CreateTaskInputBoundary {
    private final TaskGateway taskGateway;
    private final CategoryGateway categoryGateway;
    private final CreateTaskOutputBoundary outputBoundary;

    public CreateTaskInteractor(TaskGateway taskGateway,
                                CategoryGateway categoryGateway,
                                CreateTaskOutputBoundary outputBoundary) {
        this.taskGateway = taskGateway;
        this.categoryGateway = categoryGateway;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(CreateTaskInputData inputData) {
        String taskName = inputData.getTaskName();

        // Validate input
        if (taskName == null || taskName.trim().isEmpty()) {
            outputBoundary.presentError("Task name cannot be empty");
            return;
        }

        if (taskName.length() > 20) {
            outputBoundary.presentError("The name of Task cannot exceed 20 letters");
            return;
        }

        // Check if task name already exists
        if (taskGateway.availableTaskNameExists(taskName)) {
            outputBoundary.presentError("The Task name already exists");
            return;
        }

        // Validate category if provided
        String categoryName = "";
        if (inputData.getCategoryId() != null && !inputData.getCategoryId().isEmpty()) {
            var category = categoryGateway.getCategoryById(inputData.getCategoryId());
            if (category == null) {
                outputBoundary.presentError("Invalid category selected");
                return;
            }
            categoryName = category.getName();
        }

        // Create new task info
        Info.Builder builder = new Info.Builder(taskName);
        if (inputData.getDescription() != null && !inputData.getDescription().trim().isEmpty()) {
            builder.description(inputData.getDescription());
        }
        if (!categoryName.isEmpty()) {
            builder.category(categoryName);
        }

        Info taskInfo = builder.build();

        // Save the task
        String taskId = taskGateway.saveAvailableTask(taskInfo);

        CreateTaskOutputData outputData = new CreateTaskOutputData(
                taskId, taskName, "Task created successfully"
        );
        outputBoundary.presentSuccess(outputData);
    }
}