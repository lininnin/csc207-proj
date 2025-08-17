package use_case.goalManage.delete_goal;

/**
 * Input data for goal deletion operations.
 * Contains the goal name and confirmation status.
 */
public class DeleteGoalInputData {
    private final String goalName;
    private final boolean confirmed;

    /**
     * Constructs a new DeleteGoalInputData instance.
     * @param goalName Name of the goal to delete
     * @param confirmed Whether deletion is confirmed
     */
    public DeleteGoalInputData(String goalName, boolean confirmed) {
        this.goalName = goalName;
        this.confirmed = confirmed;
    }

    /**
     * Gets the goal name.
     * @return The name of the goal
     */
    public String getGoalName() {
        return goalName;
    }

    /**
     * Checks if deletion is confirmed.
     * @return true if deletion is confirmed
     */
    public boolean isConfirmed() {
        return confirmed;
    }
}
