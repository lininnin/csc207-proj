package use_case.order_events;

/**
 * A data structure for the input data of the order events use case.
 * It encapsulates the criteria by which the events should be ordered.
 */
public class OrderEventsInputData {
    private final String orderBy;
    private final boolean reverse;
    private final boolean todayOnly;

    /**
     * Constructs an {@code OrderEventsInputData} object.
     *
     * @param orderBy The name of the field to order by (e.g., "name", "category", "due").
     * @param reverse A boolean indicating whether the order should be reversed.
     * @param todayOnly A boolean indicating whether to order only today's events or all events.
     */
    public OrderEventsInputData(String orderBy, boolean reverse, boolean todayOnly) {
        this.orderBy = orderBy;
        this.reverse = reverse;
        this.todayOnly = todayOnly;
    }

    /**
     * Gets the field name to order the events by.
     *
     * @return The field name.
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Checks if the order should be reversed.
     *
     * @return {@code true} if the events should be sorted in reverse order, {@code false} otherwise.
     */
    public boolean isReverse() {
        return reverse;
    }

    /**
     * Checks if the sorting should be limited to only today's events.
     *
     * @return {@code true} if only today's events should be considered, {@code false} for all events.
     */
    public boolean isTodayOnly() {
        return todayOnly;
    }
}
