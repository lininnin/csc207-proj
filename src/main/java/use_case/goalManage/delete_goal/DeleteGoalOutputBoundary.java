package use_case.goalManage.delete_goal;

/**
 * Output boundary for goal deletion use case
 */
public interface DeleteGoalOutputBoundary {
    /**
     * Prepare confirmation view before deletion
     * @param outputData Contains goal name and status
     */
    void prepareConfirmationView(DeleteGoalOutputData outputData);

    /**
     * Prepare view for successful deletion
     * @param outputData Contains goal name and whether it was in current goals
     */
    void prepareSuccessView(DeleteGoalOutputData outputData);

    /**
     * Prepare view for failed operation
     * @param error Error message to display
     */
    void prepareFailView(String error);
}