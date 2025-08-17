package use_case.goalManage.create_goal;

/**
 * Output boundary for the Create Goal use case.
 * Defines how the results of goal creation should be presented.
 */
public interface CreateGoalOutputBoundary {
    /**
     * Presents a success view with the created goal details.
     * @param outputData Contains success information about the created goal
     */
    void presentSuccess(CreateGoalOutputData outputData);

    /**
     * Presents an error view when goal creation fails.
     * @param error Error message describing the failure
     */
    void presentError(String error);
}
