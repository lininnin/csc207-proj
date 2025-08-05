package use_case.Angela.task.edit_today;

/**
 * Output boundary for the Edit Today Task use case.
 * Defines the interface for presenting the results of the use case.
 */
public interface EditTodayTaskOutputBoundary {
    /**
     * Prepares the success view when a today's task is successfully edited.
     * 
     * @param outputData The output data containing the updated task information
     */
    void prepareSuccessView(EditTodayTaskOutputData outputData);

    /**
     * Prepares the fail view when editing a today's task fails.
     * 
     * @param error The error message describing what went wrong
     */
    void prepareFailView(String error);
}