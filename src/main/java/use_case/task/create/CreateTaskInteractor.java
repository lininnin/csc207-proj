package use_case.task.create;

import entity.AvailableTask;
import entity.Info;
import use_case.task.TaskRepository;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Interactor for creating available tasks.
 * Contains the business logic for task creation without UI concerns.
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
    public void createTask(CreateTaskInputData inputData) {
        try {
            // Validate business rules
            if (taskRepository.availableTaskNameExists(inputData.getName())) {
                outputBoundary.presentError("Task name already exists");
                return;
            }

            // Generate unique ID
            String taskId = generateTaskId();

            // Create the task entity
            Info info = new Info(
                    taskId,
                    inputData.getName(),
                    inputData.getDescription(),
                    inputData.getCategory(),
                    LocalDate.now()
            );

            AvailableTask task = new AvailableTask(info, inputData.isOneTime());

            // Save to repository
            taskRepository.saveAvailableTask(task);

            // Present success
            CreateTaskOutputData outputData = new CreateTaskOutputData(
                    taskId,
                    inputData.getName(),
                    true
            );
            outputBoundary.presentSuccess(outputData);

        } catch (IllegalArgumentException e) {
            // Handle validation errors from entity
            outputBoundary.presentError(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            outputBoundary.presentError("Failed to create task: " + e.getMessage());
        }
    }

    private String generateTaskId() {
        return "TASK-" + UUID.randomUUID().toString();
    }
}