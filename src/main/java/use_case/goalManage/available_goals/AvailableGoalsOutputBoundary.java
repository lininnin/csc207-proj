package use_case.goalManage.available_goals;

/**
 * Output boundary interface for the Available Goals use case.
 * Defines how the results of available goals operations should be presented.
 */
public interface AvailableGoalsOutputBoundary {
    /**
     * Presents the available goals to the output.
     * @param outputData Contains the list of available goals
     */
    void presentAvailableGoals(AvailableGoalsOutputData outputData);
}
