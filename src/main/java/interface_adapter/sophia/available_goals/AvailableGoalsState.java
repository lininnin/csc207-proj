package interface_adapter.sophia.available_goals;

import java.util.List;

import entity.Sophia.Goal;

/**
 * Represents the state of available goals in the application.
 * Maintains a list of goals that are currently available for selection or management.
 */
public class AvailableGoalsState {
    private List<Goal> availableGoals;

    /**
     * Constructs a new AvailableGoalsState with no initial goals.
     */
    public AvailableGoalsState() {

    }

    /**
     * Gets the current list of available goals.
     * @return List of Goal objects representing available goals
     */
    public List<Goal> getAvailableGoals() {
        return availableGoals;
    }

    /**
     * Sets the list of available goals.
     * @param availableGoals List of Goal objects to set as available
     */
    public void setAvailableGoals(List<Goal> availableGoals) {
        this.availableGoals = availableGoals;
    }
}
