package use_case.Angela.task.edit_todays;

/**
 * Input boundary for the edit today's task use case.
 */
public interface EditTodaysTaskInputBoundary {
    /**
     * Edits a today's task with the given data.
     *
     * @param inputData The task edit data
     */
    void execute(EditTodaysTaskInputData inputData);
}