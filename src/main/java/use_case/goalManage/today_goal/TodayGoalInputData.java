package use_case.goalManage.today_goal;

public class TodayGoalInputData {
    private final String goalName;
    private final double newAmount;

    public TodayGoalInputData(String goalName, double newAmount) {
        this.goalName = goalName;
        this.newAmount = newAmount;
    }

    public String getGoalName() {
        return goalName;
    }

    public double getNewAmount() {
        return newAmount;
    }
}