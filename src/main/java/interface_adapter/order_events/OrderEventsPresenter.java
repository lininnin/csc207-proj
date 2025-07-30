package interface_adapter.order_events;

import use_case.order_events.OrderEventsOutputBoundary;
import use_case.order_events.OrderEventsOutputData;

public class OrderEventsPresenter implements OrderEventsOutputBoundary {
    @Override
    public void present(OrderEventsOutputData outputData) {
        String message = String.format("Events ordered by %s (%s)",
                outputData.getOrderCriteria(),
                outputData.getOrderedEvents().size());

        System.out.println(message); // Replace with actual UI update
    }
}