package interface_adapter.sophia.delete_goal;

/**
 * Represents the state of a deleted goal operation.
 * Tracks the goal name, whether it was in current goals, and any errors.
 */
public class DeletedGoalState {
    private String goalName;
    private boolean wasInCurrentGoals;
    private String error;

    /**
     * Constructs a new DeletedGoalState with default values.
     */
    public DeletedGoalState() {
        this.goalName = "";
        this.wasInCurrentGoals = false;
        this.error = null;
    }

    /**
     * Gets the name of the deleted goal.
     * @return The goal name
     */
    public String getGoalName() {
        return goalName;
    }

    /**
     * Sets the name of the deleted goal.
     * @param goalName The goal name to set
     */
    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    /**
     * Checks if the goal was in current goals.
     * @return true if the goal was in current goals
     */
    public boolean wasInCurrentGoals() {
        return wasInCurrentGoals;
    }

    /**
     * Sets whether the goal was in current goals.
     * @param wasInCurrentGoals The flag to set
     */
    public void setWasInCurrentGoals(boolean wasInCurrentGoals) {
        this.wasInCurrentGoals = wasInCurrentGoals;
    }

    /**
     * Gets the error message if deletion failed.
     * @return The error message
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message for failed deletion.
     * @param error The error message to set
     */
    public void setError(String error) {
        this.error = error;
    }
}
