package use_case.orderTask;

public class OrderTasksInputData {
    private final boolean ascending;
    private final String sortBy;

    public OrderTasksInputData(boolean ascending, String sortBy) {
        this.ascending = ascending;
        this.sortBy = sortBy;
    }

    public boolean isAscending() {
        return ascending;
    }

    public String getSortBy() {
        return sortBy;
    }
}