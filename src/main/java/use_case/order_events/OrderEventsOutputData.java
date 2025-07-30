package use_case.order_events;

import entity.Alex.Event.Event;
import java.util.List;

public class OrderEventsOutputData {
    private final List<Event> orderedEvents;
    private final String orderCriteria;

    public OrderEventsOutputData(List<Event> orderedEvents, String orderCriteria) {
        this.orderedEvents = orderedEvents;
        this.orderCriteria = orderCriteria;
    }

    // Getters
    public List<Event> getOrderedEvents() { return orderedEvents; }
    public String getOrderCriteria() { return orderCriteria; }
}