package use_case.Angela.task.mark_complete;

import entity.Angela.Task.Task;

/**
 * Data access interface for marking tasks as complete or incomplete.
 * This interface defines the methods needed by the MarkTaskComplete use case.
 */
public interface MarkTaskCompleteDataAccessInterface {
    /**
     * Gets a task from today's list by its ID.
     *
     * @param taskId The ID of the task
     * @return The task if found, null otherwise
     */
    Task getTodayTaskById(String taskId);

    /**
     * Updates the completion status of a task in today's list.
     *
     * @param taskId The ID of the task to update
     * @param isCompleted The new completion status
     * @return true if the update was successful, false otherwise
     */
    boolean updateTaskCompletionStatus(String taskId, boolean isCompleted);

}