package use_case.goalManage.today_goal;

public interface TodayGoalInputBoundary {
    void addToToday(TodayGoalInputData inputData);
    void removeFromToday(TodayGoalInputData inputData);
    void execute();  // Add this new method for refreshing Today's Goals
}