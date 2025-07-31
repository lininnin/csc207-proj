// TodayGoalsInputBoundary.java
package use_case.goalManage.today_goal;

public interface TodayGoalsInputBoundary {
    void addToToday(TodayGoalInputData inputData);
    void removeFromToday(TodayGoalInputData inputData);
}