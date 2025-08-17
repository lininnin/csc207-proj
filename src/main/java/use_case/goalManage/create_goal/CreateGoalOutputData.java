package use_case.goalManage.create_goal;

/**
 * Output data for goal creation, containing success/status information.
 * Used to communicate the results of goal creation operations.
 */
public class CreateGoalOutputData {
    private final String goalName;
    private final String statusMessage;

    /**
     * Constructs a new CreateGoalOutputData with results.
     * @param goalName Name of the created goal
     * @param statusMessage Status message about the operation
     */
    public CreateGoalOutputData(String goalName, String statusMessage) {
        this.goalName = goalName;
        this.statusMessage = statusMessage;
    }

    /**
     * Gets the name of the created goal.
     * @return The goal name
     */
    public String getGoalName() {
        return goalName;
    }

    /**
     * Gets the status message about the operation.
     * @return The status message
     */
    public String getStatusMessage() {
        return statusMessage;
    }
}
