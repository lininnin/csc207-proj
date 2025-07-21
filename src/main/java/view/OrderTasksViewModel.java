package view;

import entity.Task;
import java.util.List;

/**
 * ViewModel for ordered tasks.
 */
public class OrderTasksViewModel {
    private List<Task> orderedTasks;
    private String message;

    public List<Task> getOrderedTasks() {
        return orderedTasks;
    }

    public void setOrderedTasks(List<Task> orderedTasks) {
        this.orderedTasks = orderedTasks;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
