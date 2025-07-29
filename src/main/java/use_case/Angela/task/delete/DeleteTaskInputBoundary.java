package use_case.Angela.task.delete;

/**
 * Input boundary for the delete task use case.
 */
public interface DeleteTaskInputBoundary {
    /**
     * Deletes a task with the given data.
     *
     * @param inputData The task deletion data
     */
    void execute(DeleteTaskInputData inputData);
}