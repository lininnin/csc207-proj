package use_case.goalManage.order_goal;

/**
 * A data structure for the input data of the order goals use case.
 * It encapsulates the criteria by which the goals should be ordered.
 */
public class OrderGoalsInputData {
    private final String orderBy;
    private final boolean reverse;

    /**
     * Constructs an {@code OrderGoalsInputData} object.
     *
     * @param orderBy The name of the field to order by (e.g., "name", "deadline", "period").
     * @param reverse A boolean indicating whether the order should be reversed.
     */
    public OrderGoalsInputData(String orderBy, boolean reverse) {
        this.orderBy = orderBy;
        this.reverse = reverse;
    }

    /**
     * Gets the field name to order the goals by.
     *
     * @return The field name.
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Checks if the order should be reversed.
     *
     * @return {@code true} if the goals should be sorted in reverse order, {@code false} otherwise.
     */
    public boolean isReverse() {
        return reverse;
    }
}
