package interface_adapter.Sophia.order_goal;

import java.util.List;

/**
 * Represents the state of the ordered goals view
 */
public class OrderedGoalState {
    private List<String> orderedGoalNames;

    public OrderedGoalState() {
    }

    public List<String> getOrderedGoalNames() {
        return orderedGoalNames;
    }

    public void setOrderedGoalNames(List<String> orderedGoalNames) {
        this.orderedGoalNames = orderedGoalNames;
    }
}