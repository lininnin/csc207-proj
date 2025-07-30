package use_case.order_events;

public class OrderEventsInputData {
    private final String orderBy;
    private final boolean reverse;
    private final boolean todayOnly;

    public OrderEventsInputData(String orderBy, boolean reverse, boolean todayOnly) {
        this.orderBy = orderBy;
        this.reverse = reverse;
        this.todayOnly = todayOnly;
    }

    // Getters
    public String getOrderBy() { return orderBy; }
    public boolean isReverse() { return reverse; }
    public boolean isTodayOnly() { return todayOnly; }
}