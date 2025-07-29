package use_case.goal_delete;

public class DeleteGoalInputData {
    private final String goalName;

    public DeleteGoalInputData(String goalName) {
        this.goalName = goalName;
    }

    public String getGoalName() {
        return goalName;
    }
}