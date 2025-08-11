package use_case.goalManage.today_goal;

public interface TodayGoalOutputBoundary {
    void prepareSuccessView(TodayGoalOutputData outputData);
    void prepareFailView(String error);
}
