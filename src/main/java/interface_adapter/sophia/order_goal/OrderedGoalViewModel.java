package interface_adapter.sophia.order_goal;

import java.util.List;

import interface_adapter.ViewModel;

/**
 * ViewModel for managing the ordered goals view state and properties.
 */
public class OrderedGoalViewModel extends ViewModel<OrderedGoalState> {
    public static final String TITLE_LABEL = "Ordered Goals";
    public static final String ORDERED_GOALS_PROPERTY = "orderedGoals";

    /**
     * Constructs an OrderedGoalViewModel with default state.
     */
    public OrderedGoalViewModel() {
        super("ordered goals");
        setState(new OrderedGoalState());
    }

    /**
     * Sets the ordered goals list and notifies property change listeners.
     * @param orderedGoals The ordered list of goal names
     */
    public void setOrderedGoals(List<String> orderedGoals) {
        final OrderedGoalState currentState = getState();
        currentState.setOrderedGoalNames(orderedGoals);
        setState(currentState);
        firePropertyChanged(ORDERED_GOALS_PROPERTY);
    }

    /**
     * Gets the current ordered goals list.
     * @return List of ordered goal names
     */
    public List<String> getOrderedGoals() {
        return getState().getOrderedGoalNames();
    }
}
