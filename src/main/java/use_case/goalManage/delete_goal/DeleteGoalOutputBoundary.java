package use_case.goalManage.delete_goal;

/**
 * Output boundary for goal deletion use case.
 * Defines how the results of goal deletion operations should be presented.
 */
public interface DeleteGoalOutputBoundary {
    /**
     * Prepares confirmation view before deletion.
     * @param outputData Contains goal name and status
     */
    void prepareConfirmationView(DeleteGoalOutputData outputData);

    /**
     * Prepares view for successful deletion.
     * @param outputData Contains goal name and whether it was in current goals
     */
    void prepareSuccessView(DeleteGoalOutputData outputData);

    /**
     * Prepares view for failed operation.
     * @param error Error message to display
     */
    void prepareFailView(String error);
}
