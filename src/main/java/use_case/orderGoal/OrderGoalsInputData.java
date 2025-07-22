package use_case.orderGoal;

public class OrderGoalsInputData {
    private final String orderBy; // e.g., "priority", "deadline", "progress"

    public OrderGoalsInputData(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderBy() {
        return orderBy;
    }
}
