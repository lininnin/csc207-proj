package use_case.goalManage.delete_goal;

/**
 * Output data for a goal deletion use case.
 * <p>
 * Encapsulates the name of the deleted goal, a flag indicating whether
 * the goal was part of the current goals, and a user-facing message.
 */
public class DeleteGoalOutputData {
    private final String goalName;
    private final boolean wasInCurrentGoals;
    private final String message;

    /**
     * Constructs an output data object for goal deletion.
     *
     * @param goalName          the name of the deleted goal
     * @param wasInCurrentGoals whether the goal was part of the current goals list
     * @param message           a user-facing message about the deletion result
     */
    public DeleteGoalOutputData(String goalName, boolean wasInCurrentGoals, String message) {
        this.goalName = goalName;
        this.wasInCurrentGoals = wasInCurrentGoals;
        this.message = message;
    }

    /**
     * @return the name of the deleted goal
     */
    public String getGoalName() {
        return goalName;
    }

    /**
     * @return true if the goal was in the current goals list before deletion
     */
    public boolean wasInCurrentGoals() {
        return wasInCurrentGoals;
    }

    /**
     * @return the result message about the deletion
     */
    public String getMessage() {
        return message;
    }
}
