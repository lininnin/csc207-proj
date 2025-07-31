package use_case.goalManage.delete_goal;

/**
 * Output data for goal deletion
 */
public class DeleteGoalOutputData {
    private final String goalName;
    private final boolean wasInCurrentGoals;
    private final String message;

    public DeleteGoalOutputData(String goalName, boolean wasInCurrentGoals, String message) {
        this.goalName = goalName;
        this.wasInCurrentGoals = wasInCurrentGoals;
        this.message = message;
    }

    // Getters
    public String getGoalName() { return goalName; }
    public boolean wasInCurrentGoals() { return wasInCurrentGoals; }
    public String getMessage() { return message; }
}