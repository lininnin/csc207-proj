package interface_adapter.Sophia.order_goal;

import use_case.goalManage.order_goal.OrderGoalsInputBoundary;
import use_case.goalManage.order_goal.OrderGoalsInputData;

public class OrderGoalController {
    private final OrderGoalsInputBoundary interactor;
    private String lastOrderBy = "name"; // Default sort order
    private boolean lastReverse = false; // Default is not reverse

    public OrderGoalController(OrderGoalsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String orderBy, boolean reverse) {
        // Save the user's new ordering preference
        this.lastOrderBy = orderBy;
        this.lastReverse = reverse;

        // Execute the use case
        interactor.execute(new OrderGoalsInputData(orderBy, reverse));
    }

    public void restoreLastOrder() {
        // Re-execute the last known order
        interactor.execute(new OrderGoalsInputData(this.lastOrderBy, this.lastReverse));
    }
}