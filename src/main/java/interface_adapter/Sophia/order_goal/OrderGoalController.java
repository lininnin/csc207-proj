package interface_adapter.Sophia.order_goal;

import use_case.goalManage.order_goal.OrderGoalsInputBoundary;
import use_case.goalManage.order_goal.OrderGoalsInputData;

public class OrderGoalController {
    private final OrderGoalsInputBoundary interactor;

    public OrderGoalController(OrderGoalsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String orderBy, boolean reverse) {
        interactor.execute(new OrderGoalsInputData(orderBy, reverse));
    }
}
