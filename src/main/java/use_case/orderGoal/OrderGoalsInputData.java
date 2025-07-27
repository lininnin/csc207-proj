package use_case.orderGoal;

public class OrderGoalsInputData {
    private final String orderBy;
    private final boolean reverse;

    public OrderGoalsInputData(String orderBy, boolean reverse) {
        this.orderBy = orderBy;
        this.reverse = reverse;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public boolean isReverse() {
        return reverse;
    }
}
