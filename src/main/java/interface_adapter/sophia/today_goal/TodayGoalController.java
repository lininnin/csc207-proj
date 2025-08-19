package interface_adapter.sophia.today_goal;

import use_case.goalManage.today_goal.TodayGoalInputData;
import use_case.goalManage.today_goal.TodayGoalInteractor;

/**
 * Controller for managing today's goals operations.
 * Handles adding, removing, and updating progress of goals for today.
 */
public class TodayGoalController {
    private final TodayGoalInteractor todayGoalInteractor;

    /**
     * Constructs a TodayGoalController with the specified interactor.
     * @param todayGoalInteractor The interactor for today's goals operations
     */
    public TodayGoalController(TodayGoalInteractor todayGoalInteractor) {
        this.todayGoalInteractor = todayGoalInteractor;
    }

    /**
     * Executes the default operation to fetch today's goals.
     */
    public void execute() {
        todayGoalInteractor.execute();
    }

    /**
     * Adds a goal to today's list.
     * @param goalName The name of the goal to add
     */
    public void addToToday(String goalName) {
        final TodayGoalInputData inputData = new TodayGoalInputData(goalName, 1.0);
        todayGoalInteractor.addToToday(inputData);
    }

    /**
     * Removes a goal from today's list.
     * @param goalName The name of the goal to remove
     * @param amount The amount to remove
     */
    public void removeFromToday(String goalName, double amount) {
        final TodayGoalInputData inputData = new TodayGoalInputData(goalName, amount);
        todayGoalInteractor.removeFromToday(inputData);
    }

    /**
     * Updates the progress of a goal in today's list.
     * @param goalName The name of the goal to update
     * @param newAmount The new progress amount
     */
    public void updateProgress(String goalName, double newAmount) {
        final TodayGoalInputData inputData = new TodayGoalInputData(goalName, newAmount);
        todayGoalInteractor.updateProgress(inputData);
    }
}
