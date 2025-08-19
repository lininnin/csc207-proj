package interface_adapter.sophia.order_goal;

import java.util.List;

/**
 * Represents the state of the ordered goals view.
 * Maintains the current ordered list of goal names.
 */
public class OrderedGoalState {
    private List<String> orderedGoalNames;

    /**
     * Constructs an empty OrderedGoalState.
     */
    public OrderedGoalState() {
    }

    /**
     * Gets the ordered list of goal names.
     * @return List of ordered goal names
     */
    public List<String> getOrderedGoalNames() {
        return orderedGoalNames;
    }

    /**
     * Sets the ordered list of goal names.
     * @param orderedGoalNames The list of goal names to set
     */
    public void setOrderedGoalNames(List<String> orderedGoalNames) {
        this.orderedGoalNames = orderedGoalNames;
    }
}
