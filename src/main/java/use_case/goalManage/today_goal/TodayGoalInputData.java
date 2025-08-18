package use_case.goalManage.today_goal;

/**
 * A data structure for the input data of the today's goals use case.
 * It encapsulates the name of the goal and, optionally, a new progress amount.
 */
public class TodayGoalInputData {
    private final String goalName;
    private final double newAmount;

    /**
     * Constructs a {@code TodayGoalInputData} object.
     *
     * @param goalName  The name of the goal.
     * @param newAmount The new progress amount for the goal.
     */
    public TodayGoalInputData(String goalName, double newAmount) {
        this.goalName = goalName;
        this.newAmount = newAmount;
    }

    /**
     * Gets the name of the goal.
     *
     * @return The goal name.
     */
    public String getGoalName() {
        return goalName;
    }

    /**
     * Gets the new progress amount for the goal.
     *
     * @return The new progress amount.
     */
    public double getNewAmount() {
        return newAmount;
    }
}
