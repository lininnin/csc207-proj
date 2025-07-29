package use_case.create_goal;

/**
 * Output boundary for the Create Goal use case.
 */
public interface CreateGoalOutputBoundary {
    void presentSuccess(CreateGoalOutputData outputData);
    void presentError(String error);
}