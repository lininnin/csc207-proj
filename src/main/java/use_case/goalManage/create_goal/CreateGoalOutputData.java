package use_case.goalManage.create_goal;

/**
 * Output data for goal creation, containing success/status information.
 */
public class CreateGoalOutputData {
    private final String goalName;
    private final String statusMessage;

    public CreateGoalOutputData(String goalName, String statusMessage) {
        this.goalName = goalName;
        this.statusMessage = statusMessage;
    }

    // Getters
    public String getGoalName() { return goalName; }
    public String getStatusMessage() { return statusMessage; }
}