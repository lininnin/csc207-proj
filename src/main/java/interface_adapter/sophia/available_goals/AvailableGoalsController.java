package interface_adapter.sophia.available_goals;

import javax.swing.JOptionPane;

import entity.Sophia.Goal;
import interface_adapter.sophia.order_goal.OrderGoalController;
import use_case.goalManage.available_goals.AvailableGoalsInputBoundary;
import use_case.goalManage.delete_goal.DeleteGoalInputBoundary;
import use_case.goalManage.delete_goal.DeleteGoalInputData;
import use_case.goalManage.today_goal.TodayGoalInputBoundary;
import use_case.goalManage.today_goal.TodayGoalInputData;

/**
 * Controller for managing available goals in the Sophia goal management system.
 * Handles operations such as adding goals to today's list, deleting goals,
 * and reordering goals based on specified criteria.
 */
public class AvailableGoalsController {
    private final AvailableGoalsInputBoundary availableGoalsInteractor;
    private final TodayGoalInputBoundary todayGoalInteractor;
    private final DeleteGoalInputBoundary deleteGoalInteractor;
    private final OrderGoalController orderGoalController;

    /**
     * Constructs an AvailableGoalsController with the specified interactors.
     *
     * @param availableGoalsInteractor The interactor for managing available goals
     * @param todayGoalInteractor The interactor for managing today's goals
     * @param deleteGoalInteractor The interactor for deleting goals
     * @param orderGoalController The controller for ordering goals
     */
    public AvailableGoalsController(AvailableGoalsInputBoundary availableGoalsInteractor,
                                    TodayGoalInputBoundary todayGoalInteractor,
                                    DeleteGoalInputBoundary deleteGoalInteractor,
                                    OrderGoalController orderGoalController) {
        this.availableGoalsInteractor = availableGoalsInteractor;
        this.todayGoalInteractor = todayGoalInteractor;
        this.deleteGoalInteractor = deleteGoalInteractor;
        this.orderGoalController = orderGoalController;
    }

    /**
     * Executes the appropriate action based on the command string.
     *
     * @param command The action command to execute
     */
    public void execute(String command) {
        boolean shouldRefresh = true;

        if (command != null && !command.isEmpty()) {
            if (command.startsWith("add_to_today:")) {
                final String goalName = command.substring("add_to_today:".length()).trim();
                final TodayGoalInputData inputData = new TodayGoalInputData(goalName, 0.0);
                this.todayGoalInteractor.addToToday(inputData);
            }
            else if (command.startsWith("delete:")) {
                final String goalName = command.substring("delete:".length()).trim();
                final DeleteGoalInputData inputData = new DeleteGoalInputData(goalName, false);
                this.deleteGoalInteractor.execute(inputData);
            }
            else {
                shouldRefresh = false;
            }
        }

        if (shouldRefresh) {
            this.availableGoalsInteractor.execute();
        }
    }

    /**
     * Adds the specified goal to today's goals list with its current progress.
     *
     * @param goal The goal to add to today's list
     */
    public void addSelectedGoalToToday(Goal goal) {
        final TodayGoalInputData inputData = new TodayGoalInputData(
                goal.getGoalInfo().getInfo().getName(),
                goal.getCurrentProgress()
        );
        this.todayGoalInteractor.addToToday(inputData);
    }

    /**
     * Deletes the specified goal from the system.
     *
     * @param goal The goal to delete
     */
    public void deleteGoal(Goal goal) {
        try {
            final DeleteGoalInputData inputData = new DeleteGoalInputData(
                    goal.getGoalInfo().getInfo().getName(),
                    true
            );
            this.deleteGoalInteractor.execute(inputData);
            this.availableGoalsInteractor.execute();
        }
        catch (IllegalArgumentException | IllegalStateException ex) {
            System.err.println("Error deleting goal: " + ex.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Error deleting goal: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Reorders the goals based on the specified criteria and direction.
     *
     * @param criteria The sorting criteria to use
     * @param reverse Whether to sort in reverse order
     */
    public void orderGoals(String criteria, boolean reverse) {
        orderGoalController.execute(criteria, reverse);
        availableGoalsInteractor.execute();
    }
}
