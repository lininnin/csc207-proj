package use_case.add_task_to_today;

import java.time.LocalDate;

/**
 * Input data for adding a task to today.
 * Includes priority and dates that are set at this point.
 */
public class AddTaskToTodayInputData {
    private final String taskId;
    private final String priority;
    private final LocalDate beginDate;
    private final LocalDate dueDate;

    public AddTaskToTodayInputData(String taskId, String priority,
                                   LocalDate beginDate, LocalDate dueDate) {
        this.taskId = taskId;
        this.priority = priority;
        this.beginDate = beginDate;
        this.dueDate = dueDate;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getPriority() {
        return priority;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}