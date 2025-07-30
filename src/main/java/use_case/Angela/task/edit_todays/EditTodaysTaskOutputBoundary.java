package use_case.Angela.task.edit_todays;

/**
 * Output boundary for the edit today's task use case.
 */
public interface EditTodaysTaskOutputBoundary {
    /**
     * Prepares the success view with the edited task data.
     *
     * @param outputData The output data
     */
    void prepareSuccessView(EditTodaysTaskOutputData outputData);

    /**
     * Prepares the fail view with an error message.
     *
     * @param error The error message
     */
    void prepareFailView(String error);
}