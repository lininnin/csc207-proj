package use_case.goalManage.today_goal;

/**
 * Defines the output boundary for the today's goals use case.
 * It specifies the methods for the presenter to prepare the view for success or failure.
 */
public interface TodayGoalOutputBoundary {
    /**
     * Prepares the success view with the data of today's goals.
     *
     * @param outputData The data containing the list of today's goals.
     */
    void prepareSuccessView(TodayGoalOutputData outputData);

    /**
     * Prepares the fail view with an error message.
     *
     * @param error The error message to be displayed.
     */
    void prepareFailView(String error);
}
