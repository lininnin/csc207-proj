package use_case.goalManage.available_goals;

import entity.Sophia.Goal;
import java.util.List;

public class AvailableGoalsOutputData {
    private final List<Goal> availableGoals;

    public AvailableGoalsOutputData(List<Goal> availableGoals) {
        this.availableGoals = availableGoals;
    }

    public List<Goal> getAvailableGoals() {
        return availableGoals;
    }
}