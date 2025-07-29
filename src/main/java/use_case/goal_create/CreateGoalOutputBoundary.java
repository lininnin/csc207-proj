package use_case.goal_create;

public interface CreateGoalOutputBoundary {
    void presentSuccess(CreateGoalOutputData outputData);
    void presentFailure(String error);
}

