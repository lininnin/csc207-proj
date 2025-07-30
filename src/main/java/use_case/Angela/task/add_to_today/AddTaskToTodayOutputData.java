package use_case.Angela.task.add_to_today;

import entity.Angela.Task.Task;

/**
 * Output data for adding a task to today.
 */
public class AddTaskToTodayOutputData {
    private final Task task;
    private final String taskName;

    public AddTaskToTodayOutputData(Task task, String taskName) {
        this.task = task;
        this.taskName = taskName;
    }

    public Task getTask() {
        return task;
    }

    public String getTaskName() {
        return taskName;
    }
}