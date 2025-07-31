package use_case.goalManage.edit_todays_goal;

public class EditTodaysGoalOutputData {
    private final String goalName;
    private final String message;

    public EditTodaysGoalOutputData(String goalName, String message) {
        this.goalName = goalName;
        this.message = message;
    }

    // Getters
    public String getGoalName() { return goalName; }
    public String getMessage() { return message; }
}