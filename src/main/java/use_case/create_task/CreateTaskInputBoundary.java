package use_case.create_task;

/**
 * Input boundary for the create task use case.
 */
public interface CreateTaskInputBoundary {
    /**
     * Executes the create task use case.
     *
     * @param inputData The input data for creating a task
     */
    void execute(CreateTaskInputData inputData);
}