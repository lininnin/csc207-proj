package use_case.task;

import entity.AvailableTask;
import entity.TodaysTask;
import java.util.List;

/**
 * Repository interface for Task persistence operations.
 * Defines the contract for data access without depending on implementation details.
 * Part of the use case layer - depends only on entities.
 */
public interface TaskRepository {

    // Available Task operations

    /**
     * Saves a new available task template.
     *
     * @param task The available task to save
     * @throws IllegalArgumentException if task with same ID already exists
     */
    void saveAvailableTask(AvailableTask task);

    /**
     * Updates an existing available task.
     *
     * @param task The available task to update
     * @throws IllegalArgumentException if task doesn't exist
     */
    void updateAvailableTask(AvailableTask task);

    /**
     * Deletes an available task by ID.
     *
     * @param taskId The ID of the task to delete
     * @return true if deleted, false if not found
     */
    boolean deleteAvailableTask(String taskId);

    /**
     * Finds an available task by ID.
     *
     * @param taskId The task ID
     * @return The available task or null if not found
     */
    AvailableTask findAvailableTaskById(String taskId);

    /**
     * Gets all available tasks.
     *
     * @return List of all available tasks
     */
    List<AvailableTask> findAllAvailableTasks();

    /**
     * Checks if an available task name already exists.
     *
     * @param name The task name to check
     * @return true if name exists, false otherwise
     */
    boolean availableTaskNameExists(String name);

    // Today's Task operations

    /**
     * Adds a task to today's active tasks.
     *
     * @param task The today's task to add
     */
    void addTodaysTask(TodaysTask task);

    /**
     * Updates a today's task (priority, completion, etc).
     *
     * @param task The today's task to update
     * @throws IllegalArgumentException if task doesn't exist in today's list
     */
    void updateTodaysTask(TodaysTask task);

    /**
     * Removes a task from today's list.
     *
     * @param taskId The ID of the task to remove
     * @return true if removed, false if not found
     */
    boolean removeTodaysTask(String taskId);

    /**
     * Gets all today's tasks.
     *
     * @return List of all today's tasks
     */
    List<TodaysTask> findAllTodaysTasks();

    /**
     * Gets today's tasks filtered by completion status.
     *
     * @param completed true for completed tasks, false for incomplete
     * @return Filtered list of today's tasks
     */
    List<TodaysTask> findTodaysTasksByStatus(boolean completed);

    /**
     * Gets overdue tasks from today's list.
     *
     * @return List of overdue tasks
     */
    List<TodaysTask> findOverdueTasks();

    /**
     * Checks if a task is in today's list.
     *
     * @param taskId The task ID to check
     * @return true if in today's list, false otherwise
     */
    boolean isInTodaysList(String taskId);

    // Daily reset operations

    /**
     * Clears all today's tasks.
     * Used during daily reset at midnight.
     */
    void clearTodaysTasks();

    /**
     * Removes one-time tasks from available list.
     * Called after daily reset for tasks marked as one-time.
     *
     * @param taskIds List of task IDs to remove
     */
    void removeOneTimeTasks(List<String> taskIds);
}