package interface_adapter.orderGoals;

import use_case.orderGoal.OrderGoalsInputBoundary;
import use_case.orderGoal.OrderGoalsInputData;

public class OrderGoalsController {
    private final OrderGoalsInputBoundary interactor;

    public OrderGoalsController(OrderGoalsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void orderGoalsBy(String orderBy, boolean reverse) {
        interactor.execute(new OrderGoalsInputData(orderBy, reverse));
    }
}
