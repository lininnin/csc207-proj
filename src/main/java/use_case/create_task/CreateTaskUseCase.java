package use_case.create_task;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Info.Info;
import entity.Angela.Task.Task;
import use_case.repository.TaskRepository;

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
            // Step 1: Build Info object using builder pattern
            Info taskInfo = new Info.Builder(inputData.getName())
                    .description(inputData.getDescription())
                    .category(inputData.getCategory())
                    .build();

            // Step 2: Build BeginAndDueDates object
            BeginAndDueDates dates = new BeginAndDueDates(
                    inputData.getBeginDate(),
                    inputData.getDueDate()
            );

            // Step 3: Create Task (assume Task does not use Builder but accepts Info + Dates + Priority)
            Task newTask = new Task(taskInfo, dates, inputData.getPriority());

            // Step 4: Save task to repository
            taskRepository.save(newTask);

            // Step 5: Notify output boundary of success
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


