package use_case.Angela.task.edit_available;

/**
 * Output boundary for the edit available task use case.
 */
public interface EditAvailableTaskOutputBoundary {
    /**
     * Prepares the success view with the edited task data.
     *
     * @param outputData The output data
     */
    void prepareSuccessView(EditAvailableTaskOutputData outputData);

    /**
     * Prepares the fail view with an error message.
     *
     * @param error The error message
     */
    void prepareFailView(String error);
}