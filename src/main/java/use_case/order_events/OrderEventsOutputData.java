package use_case.order_events;

import java.util.List;

import entity.alex.Event.Event;

/**
 * A data structure for the output data of the order events use case.
 * It encapsulates the list of events that have been ordered and the criteria used for ordering.
 */
public class OrderEventsOutputData {
    private final List<Event> orderedEvents;
    private final String orderCriteria;

    /**
     * Constructs an {@code OrderEventsOutputData} object.
     *
     * @param orderedEvents The list of events after they have been sorted.
     * @param orderCriteria The criteria used to sort the events.
     */
    public OrderEventsOutputData(List<Event> orderedEvents, String orderCriteria) {
        this.orderedEvents = orderedEvents;
        this.orderCriteria = orderCriteria;
    }

    /**
     * Gets the list of events that have been ordered.
     *
     * @return The ordered list of {@code Event} objects.
     */
    public List<Event> getOrderedEvents() {
        return orderedEvents;
    }

    /**
     * Gets the criteria that was used for ordering the events.
     *
     * @return The order criteria as a string.
     */
    public String getOrderCriteria() {
        return orderCriteria;
    }
}
