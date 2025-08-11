package use_case.goalManage.delete_goal;

public class DeleteGoalInputData {
    private final String goalName;
    private final boolean confirmed;

    public DeleteGoalInputData(String goalName, boolean confirmed) {
        this.goalName = goalName;
        this.confirmed = confirmed;
    }

    public String getGoalName() { return goalName; }
    public boolean isConfirmed() { return confirmed; }
}