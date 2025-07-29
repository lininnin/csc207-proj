package use_case.Angela.task.create;

/**
 * Input boundary for the create task use case.
 */
public interface CreateTaskInputBoundary {
    /**
     * Creates a new task with the given data.
     *
     * @param inputData The task creation data
     */
    void execute(CreateTaskInputData inputData);
}