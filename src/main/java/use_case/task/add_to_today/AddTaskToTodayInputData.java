package use_case.task.add_to_today;

import entity.TodaysTask.Priority;
import java.time.LocalDate;

/**
 * Input data for adding an available task to today's list.
 */
public class AddTaskToTodayInputData {
    private final String taskId;
    private final Priority priority;
    private final LocalDate dueDate;

    public AddTaskToTodayInputData(String taskId, Priority priority, LocalDate dueDate) {
        this.taskId = taskId;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public String getTaskId() {
        return taskId;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}