package interface_adapter.Angela.task.create;

import use_case.Angela.task.create.CreateTaskInputBoundary;
import use_case.Angela.task.create.CreateTaskInputData;

/**
 * Controller for the create task use case.
 * Handles user input from the view and invokes the use case.
 */
public class CreateTaskController {
    private final CreateTaskInputBoundary createTaskInteractor;

    public CreateTaskController(CreateTaskInputBoundary createTaskInteractor) {
        this.createTaskInteractor = createTaskInteractor;
    }

    /**
     * Executes the create task use case.
     *
     * @param taskName The name of the task
     * @param description The task description (can be null or empty)
     * @param categoryId The selected category ID (can be null or empty)
     * @param isOneTime Whether this is a one-time task
     */
    public void execute(String taskName, String description,
                        String categoryId, boolean isOneTime) {
        CreateTaskInputData inputData = new CreateTaskInputData(
                taskName, description, categoryId, isOneTime
        );
        createTaskInteractor.execute(inputData);
    }
}