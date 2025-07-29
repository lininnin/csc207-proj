package use_case.Angela.task.create_task;

import entity.Angela.Task.Task;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.BeginAndDueDates.BeginAndDueDatesFactory;
import entity.info.Info;
import use_case.repository.TaskRepository;

/**
 * Use case interactor for creating tasks.
 * Implements the business logic for task creation per design requirements.
 */
public class CreateTaskInteractor implements CreateTaskInputBoundary {
    private final TaskRepository taskRepository;
    private final CreateTaskOutputBoundary outputBoundary;

    public CreateTaskInteractor(TaskRepository taskRepository,
                                CreateTaskOutputBoundary outputBoundary) {
        this.taskRepository = taskRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(CreateTaskInputData inputData) {
        try {
            // Validate input
            String validationError = validateInput(inputData);
            if (validationError != null) {
                outputBoundary.presentError(validationError);
                return;
            }

            // Check for duplicate task name
            if (isTaskNameDuplicate(inputData.getName())) {
                outputBoundary.presentError("The Task name already exists");
                return;
            }

            // Create task entity
            Task task = createTaskFromInput(inputData);

            // Save to repository
            taskRepository.save(task);

            // Prepare success output
            CreateTaskOutputData outputData = new CreateTaskOutputData(
                    task.getInfo().getId(),
                    task.getInfo().getName(),
                    "Task created successfully"
            );

            outputBoundary.presentSuccess(outputData);

        } catch (Exception e) {
            outputBoundary.presentError("Failed to create task: " + e.getMessage());
        }
    }

    /**
     * Validates the input data for task creation.
     *
     * @param inputData The input data to validate
     * @return Error message if validation fails, null if valid
     */
    private String validateInput(CreateTaskInputData inputData) {
        // Name validation
        if (inputData.getName() == null || inputData.getName().trim().isEmpty()) {
            return "Task name is required";
        }

        if (inputData.getName().length() > 20) {
            return "Task name must be 20 characters or less";
        }

        // Description validation (optional but has length limit)
        if (inputData.getDescription() != null && inputData.getDescription().length() > 100) {
            return "Task description must be 100 characters or less";
        }

        return null; // All validations passed
    }

    /**
     * Checks if a task with the given name already exists in Available tasks.
     *
     * @param name The task name to check
     * @return true if duplicate exists, false otherwise
     */
    private boolean isTaskNameDuplicate(String name) {
        return taskRepository.findAll().stream()
                .anyMatch(task -> task.getInfo().getName().equalsIgnoreCase(name.trim()));
    }

    /**
     * Creates a Task entity from the input data.
     * Note: Priority and dates are NOT set at creation per design.
     *
     * @param inputData The input data
     * @return The created task
     */
    private Task createTaskFromInput(CreateTaskInputData inputData) {
        // Create Info object using Builder pattern - name is required in constructor
        Info info = new Info.Builder(inputData.getName().trim())
                .description(inputData.getDescription() != null ? inputData.getDescription().trim() : "")
                .category(inputData.getCategory() != null ? inputData.getCategory().trim() : null)
                .build();

        // Create BeginAndDueDates with nulls (will be set when added to Today)
        BeginAndDueDates dates = BeginAndDueDatesFactory.createEmpty();

        // Create Task
        Task task = new Task(info, dates);
        task.setOneTime(inputData.isOneTime());

        // Priority is NOT set here - only when added to Today's tasks

        return task;
    }
}