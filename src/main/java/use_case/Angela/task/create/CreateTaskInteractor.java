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

        // Validate task name
        if (taskName == null || taskName.trim().isEmpty()) {
            outputBoundary.presentError("Task name cannot be empty");
            return;
        }

        if (taskName.length() > 20) {
            outputBoundary.presentError("The name of Task cannot exceed 20 letters");
            return;
        }

        // Validate description length
        String description = inputData.getDescription();
        if (description != null && description.length() > 100) {
            outputBoundary.presentError("Description cannot exceed 100 characters");
            return;
        }

        // Validate category if provided and get category name
        String categoryName = "";
        if (inputData.getCategoryId() != null && !inputData.getCategoryId().isEmpty()) {
            var category = categoryGateway.getCategoryById(inputData.getCategoryId());
            if (category == null) {
                outputBoundary.presentError("Invalid category selected");
                return;
            }
            categoryName = category.getName();
        }

        // Check for duplicate names with same category (case-insensitive)
        if (taskGateway.taskExistsWithNameAndCategory(taskName, categoryName)) {
            outputBoundary.presentError("A task with this name and category already exists");
            return;
        }

        // Create task info (no begin date - that's only for Today's tasks)
        Info.Builder builder = new Info.Builder(taskName);

        if (description != null && !description.trim().isEmpty()) {
            builder.description(description);
        }

        if (!categoryName.isEmpty()) {
            builder.category(categoryName);
        }

        Info taskInfo = builder.build();

        // Save the task (Info already has a unique ID)
        // Note: isOneTime flag is not stored yet - this will be handled when
        // you implement the full TaskRepository that saves TaskAvailable entities
        String taskId = taskGateway.saveAvailableTask(taskInfo);
        System.out.println("DEBUG: Task saved with ID: " + taskId);

        // Present success
        CreateTaskOutputData outputData = new CreateTaskOutputData(
                taskId, taskName, "Task created successfully"
        );
        System.out.println("DEBUG: Calling outputBoundary.presentSuccess");
        outputBoundary.presentSuccess(outputData);
    }
}