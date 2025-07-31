package use_case.goalManage.today_goal;

public interface TodayGoalsOutputBoundary {
    enum OperationType { ADD, REMOVE }

    void prepareSuccessView(OperationType operation, TodayGoalOutputData outputData);
    void prepareConfirmationView(TodayGoalOutputData outputData);
    void prepareFailView(OperationType operation, String error);
}