package entity.Angela.Task;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;

/**
 * Factory class for creating Task instances.
 * Implements the TaskFactoryInterf with various creation methods.
 */
public class TaskFactory implements TaskFactoryInterf {

    /**
     * Creates a basic task without priority (for Available tasks).
     *
     * @param info Task information
     * @param beginAndDueDates Date range (can have nulls initially)
     * @return A new Task instance
     * @throws IllegalArgumentException if required parameters are null
     */
    @Override
    public Task createTask(Info info, BeginAndDueDates beginAndDueDates) {
        if (info == null) {
            throw new IllegalArgumentException("Info cannot be null");
        }
        if (beginAndDueDates == null) {
            throw new IllegalArgumentException("BeginAndDueDates cannot be null");
        }

        return new Task(info, beginAndDueDates);
    }

    /**
     * Creates a task with priority (for Today's tasks).
     *
     * @param info Task information
     * @param beginAndDueDates Date range with actual dates
     * @param priority Task priority level
     * @return A new Task instance with priority set
     * @throws IllegalArgumentException if required parameters are null
     */
    @Override
    public Task createTask(Info info, BeginAndDueDates beginAndDueDates, Task.Priority priority) {
        Task task = createTask(info, beginAndDueDates);

        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null when creating a task for today");
        }

        task.setPriority(priority);
        return task;
    }

    /**
     * Creates a one-time task.
     *
     * @param info Task information
     * @param beginAndDueDates Date range
     * @param oneTime Whether this is a one-time task
     * @return A new Task instance with one-time flag set
     * @throws IllegalArgumentException if required parameters are null
     */
    @Override
    public Task createTask(Info info, BeginAndDueDates beginAndDueDates, boolean oneTime) {
        Task task = createTask(info, beginAndDueDates);
        task.setOneTime(oneTime);
        return task;
    }

    /**
     * Creates a task from an existing task (copy for today).
     * This is useful when adding an available task to today's tasks.
     *
     * @param existingTask The task to copy
     * @param priority The priority for today
     * @param beginDate The begin date (usually today)
     * @param dueDate The optional due date
     * @return A new Task instance configured for today
     */
    public Task createTaskForToday(Task existingTask, Task.Priority priority,
                                   java.time.LocalDate beginDate, java.time.LocalDate dueDate) {
        if (existingTask == null) {
            throw new IllegalArgumentException("Existing task cannot be null");
        }

        // Create new BeginAndDueDates with the specified dates
        BeginAndDueDates newDates = new BeginAndDueDates(beginDate, dueDate);

        // Create new task with same info but new dates
        Task todayTask = new Task(existingTask.getInfo(), newDates);

        // Set priority and preserve one-time flag
        todayTask.setPriority(priority);
        todayTask.setOneTime(existingTask.isOneTime());

        return todayTask;
    }
}