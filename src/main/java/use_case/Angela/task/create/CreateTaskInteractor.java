package use_case.Angela.task.create;

import entity.info.Info;
import entity.info.InfoFactory;
import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskAvailableFactory;

/**
 * Interactor for the create task use case.
 * Implements the business logic for creating a new task.
 */
public class CreateTaskInteractor implements CreateTaskInputBoundary {
    private final CreateTaskDataAccessInterface dataAccess;
    private final CreateTaskCategoryDataAccessInterface categoryDataAccess;
    private final CreateTaskOutputBoundary outputBoundary;
    private final InfoFactory infoFactory;
    private final TaskAvailableFactory taskAvailableFactory;

    public CreateTaskInteractor(CreateTaskDataAccessInterface dataAccess,
                                CreateTaskCategoryDataAccessInterface categoryDataAccess,
                                CreateTaskOutputBoundary outputBoundary,
                                InfoFactory infoFactory,
                                TaskAvailableFactory taskAvailableFactory) {
        this.dataAccess = dataAccess;
        this.categoryDataAccess = categoryDataAccess;
        this.outputBoundary = outputBoundary;
        this.infoFactory = infoFactory;
        this.taskAvailableFactory = taskAvailableFactory;
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

        // Validate category if provided
        String categoryId = inputData.getCategoryId();
        String categoryName = "";
        if (categoryId != null && !categoryId.isEmpty()) {
            var category = categoryDataAccess.getCategoryById(categoryId);
            if (category == null) {
                outputBoundary.presentError("Invalid category selected");
                return;
            }
            categoryName = category.getName();
        }

        // Check for duplicate names with same category (case-insensitive)
        // Use category ID for duplicate check
        if (dataAccess.taskExistsWithNameAndCategory(taskName, categoryId)) {
            outputBoundary.presentError("A task with this name and category already exists");
            return;
        }

        // Create task info using factory (no begin date - that's only for Today's tasks)
        Info taskInfo = (Info) infoFactory.create(taskName, description, categoryId);

        // Create TaskAvailable using factory with immutable approach
        TaskAvailableInterf taskAvailable = taskAvailableFactory.create(taskInfo, inputData.isOneTime());

        // Save the task using the correct method that handles TaskAvailable
        String taskId = dataAccess.saveTaskAvailable(taskAvailable);

        // Present success
        CreateTaskOutputData outputData = new CreateTaskOutputData(
                taskId, taskName, "Task created successfully"
        );
        outputBoundary.presentSuccess(outputData);
    }
}