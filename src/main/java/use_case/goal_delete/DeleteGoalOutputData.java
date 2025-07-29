package use_case.goal_delete;

public class DeleteGoalOutputData {
    private final String goalName;
    private final boolean wasInCurrentGoals;

    public DeleteGoalOutputData(String goalName, boolean wasInCurrentGoals) {
        this.goalName = goalName;
        this.wasInCurrentGoals = wasInCurrentGoals;
    }

    public String getGoalName() {
        return goalName;
    }

    public boolean wasInCurrentGoals() {
        return wasInCurrentGoals;
    }
}