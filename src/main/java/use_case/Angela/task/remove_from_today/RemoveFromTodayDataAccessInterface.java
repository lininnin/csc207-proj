package use_case.Angela.task.remove_from_today;

import entity.Angela.Task.Task;

/**
 * Data access interface for removing tasks from today's list.
 * This interface defines the methods needed by the RemoveFromToday use case.
 */
public interface RemoveFromTodayDataAccessInterface {
    /**
     * Gets a task from today's list by its ID.
     *
     * @param taskId The ID of the task
     * @return The task if found, null otherwise
     */
    Task getTodayTaskById(String taskId);

    /**
     * Removes a task from today's list only (soft delete).
     * The task remains in the Available Tasks list.
     *
     * @param taskId The ID of the task to remove from today
     * @return true if the removal was successful, false otherwise
     */
    boolean removeFromTodaysList(String taskId);
}