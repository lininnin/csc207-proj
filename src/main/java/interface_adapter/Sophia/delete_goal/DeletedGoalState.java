package interface_adapter.Sophia.delete_goal;

public class DeletedGoalState {
    private String goalName = "";
    private boolean wasInCurrentGoals = false;
    private String error = null;

    // Getters and setters
    public String getGoalName() { return goalName; }
    public void setGoalName(String goalName) { this.goalName = goalName; }
    public boolean wasInCurrentGoals() { return wasInCurrentGoals; }
    public void setWasInCurrentGoals(boolean wasInCurrentGoals) { this.wasInCurrentGoals = wasInCurrentGoals; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}