package use_case.goalManage.edit_todays_goal;

/**
 * Defines the input boundary for editing today's goals.
 * This interface represents the contract for the interactor that handles goal editing operations.
 */
public interface EditTodaysGoalInputBoundary {
    /**
     * Executes the goal editing operation based on the provided input data.
     * @param inputData the data required for editing a goal
     */
    void execute(EditTodaysGoalInputData inputData);
}
