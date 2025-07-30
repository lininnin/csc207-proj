package interface_adapter.order_events;

import use_case.order_events.OrderEventsInputBoundary;
import use_case.order_events.OrderEventsInputData;

public class OrderEventsController {
    private final OrderEventsInputBoundary interactor;

    public OrderEventsController(OrderEventsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String orderBy, boolean reverse, boolean todayOnly) {
        OrderEventsInputData inputData = new OrderEventsInputData(
                orderBy, reverse, todayOnly
        );
        interactor.execute(inputData);
    }
}