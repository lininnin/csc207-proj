package entity.Angela.Task;

import entity.info.Info;

/**
 * Factory class for creating TaskAvailable instances.
 * Implements the Factory pattern following Clean Architecture principles.
 */
public class TaskAvailableFactory {

    /**
     * Creates a new available task template.
     *
     * @param info Task information
     * @return A new TaskAvailable instance
     * @throws IllegalArgumentException if info is null
     */
    public TaskAvailable create(Info info) {
        return new TaskAvailable(info);
    }

    /**
     * Creates an available task with one-time flag.
     *
     * @param info Task information
     * @param isOneTime Whether this is a one-time task
     * @return A new TaskAvailable instance
     * @throws IllegalArgumentException if info is null
     */
    public TaskAvailable create(Info info, boolean isOneTime) {
        TaskAvailable task = new TaskAvailable(info);
        task.setOneTime(isOneTime);
        return task;
    }

    /**
     * Creates an available task with all fields (for loading from storage).
     *
     * @param id The task ID
     * @param info Task information
     * @param plannedDueDate Optional planned due date in ISO format
     * @param isOneTime Whether this is a one-time task
     * @return A new TaskAvailable instance
     * @throws IllegalArgumentException if required parameters are invalid
     */
    public TaskAvailable create(String id, Info info, String plannedDueDate, boolean isOneTime) {
        return new TaskAvailable(id, info, plannedDueDate, isOneTime);
    }
}