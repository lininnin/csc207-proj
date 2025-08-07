package interface_adapter.Sophia.today_goal;

import entity.Sophia.Goal;
import use_case.goalManage.today_goal.TodayGoalInputBoundary;
import use_case.goalManage.today_goal.TodayGoalInputData;

public class TodayGoalController {
    private final TodayGoalInputBoundary todayGoalsInteractor;

    public TodayGoalController(TodayGoalInputBoundary todayGoalsInteractor) {
        this.todayGoalsInteractor = todayGoalsInteractor;
    }

    public void addToToday(String goalName) {
        TodayGoalInputData inputData = new TodayGoalInputData(goalName, false);
        todayGoalsInteractor.addToToday(inputData);
    }

    public void removeFromToday(String goalName, boolean confirmed) {
        TodayGoalInputData inputData = new TodayGoalInputData(goalName, confirmed);
        todayGoalsInteractor.removeFromToday(inputData);
    }

    public void execute() {
        todayGoalsInteractor.execute();  // Calls the new execute method
    }
}