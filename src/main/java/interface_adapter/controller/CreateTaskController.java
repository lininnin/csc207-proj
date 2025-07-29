package interface_adapter.controller;

import use_case.create_task.CreateTaskInputBoundary;
import use_case.create_task.CreateTaskInputData;

/**
 * Controller for creating tasks.
 * Handles user input from the view and passes it to the use case.
 */
public class CreateTaskController {
    private final CreateTaskInputBoundary createTaskUseCase;

    public CreateTaskController(CreateTaskInputBoundary createTaskUseCase) {
        this.createTaskUseCase = createTaskUseCase;
    }

    /**
     * Creates a new task with the provided information.
     * Note: Priority and dates are NOT set at creation per design.
     *
     * @param name Task name (required, max 20 characters)
     * @param description Task description (optional, max 100 characters)
     * @param category Task category (optional)
     * @param oneTime Whether this is a one-time task
     */
    public void createTask(String name, String description, String category, boolean oneTime) {
        CreateTaskInputData inputData = new CreateTaskInputData(name, description, category, oneTime);
        createTaskUseCase.execute(inputData);
    }
}