package interface_adapter.Sophia.available_goals;

import entity.Sophia.Goal;
import java.util.List;

public class AvailableGoalsState {
    private List<Goal> availableGoals;

    public AvailableGoalsState() {}

    public List<Goal> getAvailableGoals() {
        return availableGoals;
    }

    public void setAvailableGoals(List<Goal> availableGoals) {
        this.availableGoals = availableGoals;
    }
}