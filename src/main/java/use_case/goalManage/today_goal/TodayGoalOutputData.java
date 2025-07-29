package use_case.goalManage.today_goal;

public class TodayGoalOutputData {
    private final String goalName;
    private final String message;

    public TodayGoalOutputData(String goalName) {
        this(goalName, "");
    }

    public TodayGoalOutputData(String goalName, String message) {
        this.goalName = goalName;
        this.message = message;
    }

    // Getters
    public String getGoalName() { return goalName; }
    public String getMessage() { return message; }
}
