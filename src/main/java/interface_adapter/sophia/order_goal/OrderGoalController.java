package interface_adapter.sophia.order_goal;

import use_case.goalManage.order_goal.OrderGoalsInputBoundary;
import use_case.goalManage.order_goal.OrderGoalsInputData;

/**
 * Controller for handling goal ordering operations.
 */
public class OrderGoalController {
    private final OrderGoalsInputBoundary interactor;
    private String lastOrderBy;
    private boolean lastReverse;

    /**
     * Constructs an OrderGoalController with the specified interactor.
     * Initializes with default ordering by name in ascending order.
     * @param interactor The use case interactor for ordering goals
     */
    public OrderGoalController(OrderGoalsInputBoundary interactor) {
        this.interactor = interactor;
        this.lastOrderBy = "name";
        this.lastReverse = false;
    }

    /**
     * Executes goal ordering with specified criteria.
     * @param orderBy The field to order by (e.g., "name", "date")
     * @param reverse Whether to reverse the order
     */
    public void execute(String orderBy, boolean reverse) {
        // Save the user's new ordering preference
        this.lastOrderBy = orderBy;
        this.lastReverse = reverse;

        interactor.execute(new OrderGoalsInputData(orderBy, reverse));
    }

    /**
     * Reapplies the last used ordering criteria.
     */
    public void restoreLastOrder() {
        interactor.execute(new OrderGoalsInputData(this.lastOrderBy, this.lastReverse));
    }
}
