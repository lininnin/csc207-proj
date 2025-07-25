package use_case.orderGoal;

import entity.Sophia.Goal;
import java.util.List;

public class OrderGoalsOutputData {
    private final List<Goal> orderedGoals;

    public OrderGoalsOutputData(List<Goal> orderedGoals) {
        this.orderedGoals = orderedGoals;
    }

    public List<Goal> getOrderedGoals() {
        return orderedGoals;
    }
}
