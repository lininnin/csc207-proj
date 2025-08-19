package use_case.goalManage.order_goal;

import java.util.List;

import entity.Sophia.Goal;

/**
 * A data structure for the output data of the order goals use case.
 * It encapsulates the list of goals that have been ordered.
 */
public class OrderGoalsOutputData {
    private final List<Goal> orderedGoals;

    /**
     * Constructs an {@code OrderGoalsOutputData} object.
     *
     * @param orderedGoals The list of goals after they have been sorted.
     */
    public OrderGoalsOutputData(List<Goal> orderedGoals) {
        this.orderedGoals = orderedGoals;
    }

    /**
     * Gets the list of goals that have been ordered.
     *
     * @return The ordered list of {@code Goal} objects.
     */
    public List<Goal> getOrderedGoals() {
        return orderedGoals;
    }
}
