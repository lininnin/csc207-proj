package use_case.goalManage.today_goal;

public class TodayGoalInputData {
    private final String goalName;
    private final boolean confirmed;

    public TodayGoalInputData(String goalName, boolean confirmed) {
        this.goalName = goalName;
        this.confirmed = confirmed;
    }

    // Getters
    public String getGoalName() { return goalName; }
    public boolean isConfirmed() { return confirmed; }
}