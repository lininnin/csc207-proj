package interface_adapter.controller;

import use_case.Angela.task.create.CreateTaskInputBoundary;
import use_case.Angela.task.create.CreateTaskInputData;

/**
 * Controller for creating tasks.
 * Handles user input from the task creation form.
 */
public class CreateTaskController {
    private final CreateTaskInputBoundary createTaskUseCase;

    public CreateTaskController(CreateTaskInputBoundary createTaskUseCase) {
        this.createTaskUseCase = createTaskUseCase;
    }

    /**
     * Creates a new task with the given information.
     *
     * @param taskName The name of the task
     * @param description The task description
     * @param categoryId The category ID
     * @param isOneTime Whether this is a one-time task
     */
    public void createTask(String taskName, String description, String categoryId, boolean isOneTime) {
        CreateTaskInputData inputData = new CreateTaskInputData(
                taskName, description, categoryId, isOneTime
        );
        createTaskUseCase.execute(inputData);
    }
}