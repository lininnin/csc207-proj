package use_case.orderTask;

import entity.Task;
import java.util.List;

public class OrderTasksOutputData {
    private final List<Task> orderedTasks;
    private final String sortCriteria;

    public OrderTasksOutputData(List<Task> orderedTasks, String sortCriteria) {
        this.orderedTasks = orderedTasks;
        this.sortCriteria = sortCriteria;
    }

    public List<Task> getOrderedTasks() {
        return orderedTasks;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }
}