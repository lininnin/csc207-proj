package use_case.goalManage.today_goal;

/**
 * Defines the input boundary for the today's goals use case.
 * This interface specifies the actions that can be performed related to a user's goals for the current day.
 */
public interface TodayGoalInputBoundary {
    /**
     * Executes the use case to retrieve and display today's goals.
     */
    void execute();

    /**
     * Adds a goal to the list of today's goals.
     *
     * @param inputData The data required to identify the goal to add.
     */
    void addToToday(TodayGoalInputData inputData);

    /**
     * Removes a goal from the list of today's goals.
     *
     * @param inputData The data required to identify the goal to remove.
     */
    void removeFromToday(TodayGoalInputData inputData);

    /**
     * Updates the progress of a specific goal.
     *
     * @param inputData The data required to identify the goal and its new progress amount.
     */
    void updateProgress(TodayGoalInputData inputData);
}
