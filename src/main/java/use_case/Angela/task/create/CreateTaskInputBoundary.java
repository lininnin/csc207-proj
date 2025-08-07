package use_case.Angela.task.create;

/**
 * Input boundary for the create task use case.
 * Defines the interface that the Controller will use to invoke the use case.
 */
public interface CreateTaskInputBoundary {
    /**
     * Executes the create task use case.
     *
     * @param inputData The input data containing task information
     */
    void execute(CreateTaskInputData inputData);
}