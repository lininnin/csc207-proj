package use_case.goalManage.delete_goal;

/**
 * Input boundary for goal deletion.
 * Defines the contract for executing goal deletion operations.
 */
public interface DeleteGoalInputBoundary {
    /**
     * Executes the goal deletion operation with the provided input data.
     * @param inputData Contains the goal name and confirmation status
     */
    void execute(DeleteGoalInputData inputData);
}
