package interface_adapter.Sophia.available_goals;

import use_case.goalManage.available_goals.AvailableGoalsInputBoundary;
import use_case.goalManage.delete_goal.DeleteGoalInputBoundary;
import use_case.goalManage.delete_goal.DeleteGoalInputData;
import use_case.goalManage.today_goal.TodayGoalInputBoundary;
import use_case.goalManage.today_goal.TodayGoalInputData;

import javax.swing.*;

public class AvailableGoalsController {
    private final AvailableGoalsInputBoundary availableGoalsInteractor;
    private final TodayGoalInputBoundary todayGoalInteractor;
    private final DeleteGoalInputBoundary deleteGoalInteractor;

    public AvailableGoalsController(AvailableGoalsInputBoundary availableGoalsInteractor,
                                    TodayGoalInputBoundary todayGoalInteractor,
                                    DeleteGoalInputBoundary deleteGoalInteractor) {
        this.availableGoalsInteractor = availableGoalsInteractor;
        this.todayGoalInteractor = todayGoalInteractor;
        this.deleteGoalInteractor = deleteGoalInteractor;
    }

    public void execute(String command) {
        if (command == null || command.isEmpty()) {
            this.availableGoalsInteractor.execute();
            return;
        }

        if (command.startsWith("add_to_today:")) {
            String goalName = command.substring("add_to_today:".length()).trim();
            // Default to 0 when adding to today's goals
            TodayGoalInputData inputData = new TodayGoalInputData(goalName, 0.0);
            this.todayGoalInteractor.addToToday(inputData);
        }
        else if (command.startsWith("delete:")) {
            String goalName = command.substring("delete:".length()).trim();
            DeleteGoalInputData inputData = new DeleteGoalInputData(goalName, false);
            this.deleteGoalInteractor.execute(inputData);
        }
        else {
            this.availableGoalsInteractor.execute();
        }
    }

    public void addSelectedGoalToToday(entity.Sophia.Goal goal) {
        use_case.goalManage.today_goal.TodayGoalInputData inputData = new use_case.goalManage.today_goal.TodayGoalInputData(
                goal.getGoalInfo().getInfo().getName(),
                goal.getCurrentProgress()
        );
        this.todayGoalInteractor.addToToday(inputData);
    }

    // NEW ADDITION: Method to delete a goal
    public void deleteGoal(entity.Sophia.Goal goal) {
        try {
            // Create input data for the delete use case
            use_case.goalManage.delete_goal.DeleteGoalInputData inputData = new use_case.goalManage.delete_goal.DeleteGoalInputData(
                    goal.getGoalInfo().getInfo().getName(),
                    true // true to indicate permanent deletion
            );

            this.deleteGoalInteractor.execute(inputData);
            this.availableGoalsInteractor.execute();

        } catch (Exception e) {
            System.err.println("Error deleting goal: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error deleting goal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}