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

