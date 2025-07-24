package use_case.task.create;

/**
 * Input boundary for task creation use case.
 * Defines the interface that the controller will call.
 */
public interface CreateTaskInputBoundary {
    /**
     * Creates a new available task.
     *
     * @param inputData The task creation data
     */
    void createTask(CreateTaskInputData inputData);
}