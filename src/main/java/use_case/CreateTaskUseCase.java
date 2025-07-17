package use_case;

import entity.*;
import java.time.LocalDate;

/**
 * Use case for creating a new task.
 * Implements the business logic for task creation according to Clean Architecture.
 */
public class CreateTaskUseCase {
    private final TaskRepository taskRepository;
    private final CreateTaskOutputBoundary outputBoundary;

    public CreateTaskUseCase(TaskRepository taskRepository, CreateTaskOutputBoundary outputBoundary) {
        this.taskRepository = taskRepository;
        this.outputBoundary = outputBoundary;
    }

    /**
     * Creates a new task based on the input data.
     *
     * @param inputData Contains all necessary information to create a task
     */
    public void execute(CreateTaskInputData inputData) {
        try {
            // Create Info object with task metadata
            Info taskInfo = new Info(
                    inputData.getName(),
                    inputData.getDescription(),
                    inputData.getCategory()
            );

            // Create date range
            BeginAndDueDates dates = new BeginAndDueDates(
                    inputData.getBeginDate(),
                    inputData.getDueDate()
            );

            // Create the task
            Task newTask = new Task(taskInfo, dates, inputData.getPriority());

            // Save the task
            taskRepository.save(newTask);

            // Add to today's available tasks if it starts today
            if (inputData.getBeginDate().equals(LocalDate.now())) {
                taskRepository.addToTodaysTasks(newTask);
            }

            // Prepare success output
            CreateTaskOutputData outputData = new CreateTaskOutputData(
                    newTask.getInfo().getId(),
                    newTask.getInfo().getName(),
                    "Task created successfully"
            );

            outputBoundary.presentSuccess(outputData);

        } catch (Exception e) {
            outputBoundary.presentError("Failed to create task: " + e.getMessage());
        }
    }
}

/**
 * Input data for creating a task.
 */
class CreateTaskInputData {
    private final String name;
    private final String description;
    private final String category;
    private final Task.Priority priority;
    private final LocalDate beginDate;
    private final LocalDate dueDate;

    public CreateTaskInputData(String name, String description, String category,
                               Task.Priority priority, LocalDate beginDate, LocalDate dueDate) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.beginDate = beginDate;
        this.dueDate = dueDate;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Task.Priority getPriority() { return priority; }
    public LocalDate getBeginDate() { return beginDate; }
    public LocalDate getDueDate() { return dueDate; }
}

/**
 * Output data for task creation.
 */
class CreateTaskOutputData {
    private final String taskId;
    private final String taskName;
    private final String message;

    public CreateTaskOutputData(String taskId, String taskName, String message) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.message = message;
    }

    // Getters
    public String getTaskId() { return taskId; }
    public String getTaskName() { return taskName; }
    public String getMessage() { return message; }
}

/**
 * Output boundary interface for task creation.
 */
interface CreateTaskOutputBoundary {
    void presentSuccess(CreateTaskOutputData outputData);
    void presentError(String error);
}
