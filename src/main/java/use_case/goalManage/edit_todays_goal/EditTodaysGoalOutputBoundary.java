package use_case.goalManage.edit_todays_goal;

public interface EditTodaysGoalOutputBoundary {
    void prepareSuccessView(EditTodaysGoalOutputData outputData);
    void prepareConfirmationView(EditTodaysGoalOutputData outputData);
    void prepareFailView(String error);
}