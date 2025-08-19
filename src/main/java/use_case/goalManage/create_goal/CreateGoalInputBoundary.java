package use_case.goalManage.create_goal;

/**
 * Input boundary for the Create Goal use case.
 * Defines the contract for executing goal creation operations.
 */
public interface CreateGoalInputBoundary {
    /**
     * Executes the goal creation operation with the provided input data.
     * @param inputData Contains all necessary information for goal creation
     */
    void execute(CreateGoalInputData inputData);
}
