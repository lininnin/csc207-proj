package use_case.goalManage.today_goal;

// In use_case/goalManage/today_goal/TodayGoalInputBoundary.java
public interface TodayGoalInputBoundary {
    void execute();
    void addToToday(TodayGoalInputData inputData);
    void removeFromToday(TodayGoalInputData inputData);
    void updateProgress(TodayGoalInputData inputData);
}