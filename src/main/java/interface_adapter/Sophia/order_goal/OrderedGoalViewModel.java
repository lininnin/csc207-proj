package interface_adapter.Sophia.order_goal;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * ViewModel for the ordered goals view
 */
public class OrderedGoalViewModel extends ViewModel<OrderedGoalState> {
    public static final String TITLE_LABEL = "Ordered Goals";
    public static final String ORDERED_GOALS_PROPERTY = "orderedGoals";

    public OrderedGoalViewModel() {
        super("ordered goals");
        setState(new OrderedGoalState());
    }

    public void setOrderedGoals(List<String> orderedGoals) {
        OrderedGoalState currentState = getState();
        currentState.setOrderedGoalNames(orderedGoals);
        setState(currentState);
        firePropertyChanged(ORDERED_GOALS_PROPERTY);
    }

    public List<String> getOrderedGoals() {
        return getState().getOrderedGoalNames();
    }
}