package interface_adapter.Sophia.today_goal;

import entity.Sophia.Goal;
import use_case.goalManage.today_goal.TodayGoalInputData;
import use_case.goalManage.today_goal.TodayGoalInteractor;

public class TodayGoalController {
    private final TodayGoalInteractor todayGoalInteractor;

    public TodayGoalController(TodayGoalInteractor todayGoalInteractor) {
        this.todayGoalInteractor = todayGoalInteractor;
    }

    public void execute() {
        todayGoalInteractor.execute();
    }

    public void addToToday(String goalName) {
        TodayGoalInputData inputData = new TodayGoalInputData(goalName, 1.0);
        todayGoalInteractor.addToToday(inputData);
    }
    public void removeFromToday(String goalName, double amount) {
        TodayGoalInputData inputData = new TodayGoalInputData(goalName, amount);
        todayGoalInteractor.removeFromToday(inputData);
    }

    public void updateProgress(String goalName, double newAmount) {
        TodayGoalInputData inputData = new TodayGoalInputData(goalName, newAmount);
        todayGoalInteractor.updateProgress(inputData);
    }
}