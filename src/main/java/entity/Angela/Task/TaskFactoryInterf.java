package entity.Angela.Task;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;

/**
 * Factory interface for creating Task objects.
 * Provides multiple creation methods for different scenarios.
 */
public interface TaskFactoryInterf {
    /**
     * Creates a task without priority (for Available tasks).
     *
     * @param info Task information
     * @param beginAndDueDates Date range (can have nulls initially)
     * @return A new Task instance
     */
    Task createTask(Info info, BeginAndDueDates beginAndDueDates);

    /**
     * Creates a task with priority (for Today's tasks).
     *
     * @param info Task information
     * @param beginAndDueDates Date range with actual dates
     * @param priority Task priority level
     * @return A new Task instance with priority set
     */
    Task createTask(Info info, BeginAndDueDates beginAndDueDates, Task.Priority priority);

    /**
     * Creates a one-time task.
     *
     * @param info Task information
     * @param beginAndDueDates Date range
     * @param oneTime Whether this is a one-time task
     * @return A new Task instance
     */
    Task createTask(Info info, BeginAndDueDates beginAndDueDates, boolean oneTime);
}