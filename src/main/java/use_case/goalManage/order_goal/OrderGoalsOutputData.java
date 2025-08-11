package use_case.goalManage.order_goal;

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
