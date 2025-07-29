package use_case.goal_delete;

public interface DeleteGoalOutputBoundary {
    void presentSuccess(DeleteGoalOutputData outputData);
    void presentError(String error);
}