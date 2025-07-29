package use_case.Angela.task.edit_available;

/**
 * Input boundary for the edit available task use case.
 */
public interface EditAvailableTaskInputBoundary {
    /**
     * Edits an available task with the given data.
     *
     * @param inputData The task edit data
     */
    void execute(EditAvailableTaskInputData inputData);
}