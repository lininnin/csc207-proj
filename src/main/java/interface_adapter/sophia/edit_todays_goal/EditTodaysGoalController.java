package interface_adapter.sophia.edit_todays_goal;

import java.time.LocalDate;

import use_case.goalManage.edit_todays_goal.EditTodaysGoalInputBoundary;
import use_case.goalManage.edit_todays_goal.EditTodaysGoalInputData;

/**
 * Controller for handling editing of today's goals.
 * Orchestrates the interaction between the view and use case layers.
 */
public class EditTodaysGoalController {
    private final EditTodaysGoalInputBoundary interactor;

    /**
     * Constructs an EditTodaysGoalController with the specified interactor.
     * @param interactor The use case interactor for editing goals
     */
    public EditTodaysGoalController(EditTodaysGoalInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the goal editing operation.
     * @param goalName Name of the goal to edit
     * @param newDueDate New due date for the goal
     * @param confirmed Whether the edit is confirmed
     */
    public void execute(String goalName, LocalDate newDueDate, boolean confirmed) {
        final EditTodaysGoalInputData inputData = new EditTodaysGoalInputData(
                goalName, newDueDate, confirmed
        );
        interactor.execute(inputData);
    }
}
