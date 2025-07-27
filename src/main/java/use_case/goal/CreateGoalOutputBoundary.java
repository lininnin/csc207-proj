package use_case.goal;

public interface CreateGoalOutputBoundary {
    void presentSuccess(CreateGoalOutputData outputData);
    void presentFailure(String error);
}

