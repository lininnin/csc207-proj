package interface_adapter.Sophia.today_goal;

import use_case.goalManage.today_goal.TodayGoalsInputBoundary;
import use_case.goalManage.today_goal.TodayGoalInputData;

/**
 * Receives UI events and delegates to the interactor
 */
public class TodayGoalController {
    private final TodayGoalsInputBoundary todayGoalsInteractor;

    public TodayGoalController(TodayGoalsInputBoundary todayGoalsInteractor) {
        this.todayGoalsInteractor = todayGoalsInteractor;
    }

    // For adding to Today's Goals (no confirmation needed)
    public void addToToday(String goalName) {
        TodayGoalInputData inputData = new TodayGoalInputData(goalName, false);
        todayGoalsInteractor.addToToday(inputData);
    }

    // For removing from Today's Goals (with confirmation flow)
    public void removeFromToday(String goalName, boolean confirmed) {
        TodayGoalInputData inputData = new TodayGoalInputData(goalName, confirmed);
        todayGoalsInteractor.removeFromToday(inputData);
    }
}