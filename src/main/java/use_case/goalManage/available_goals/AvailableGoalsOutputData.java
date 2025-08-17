package use_case.goalManage.available_goals;

import java.util.List;

import entity.Sophia.Goal;

/**
 * Output data structure for the Available Goals use case.
 * Contains the results of available goals operations.
 */
public class AvailableGoalsOutputData {
    private final List<Goal> availableGoals;

    /**
     * Constructs an AvailableGoalsOutputData with the specified goals.
     * @param availableGoals The list of available goals
     */
    public AvailableGoalsOutputData(List<Goal> availableGoals) {
        this.availableGoals = availableGoals;
    }

    /**
     * Gets the list of available goals.
     * @return The list of available goals
     */
    public List<Goal> getAvailableGoals() {
        return availableGoals;
    }
}
