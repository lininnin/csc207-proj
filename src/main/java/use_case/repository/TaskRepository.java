package use_case.repository;

import entity.Task;
import java.util.List;

/**
 * Repository interface for Task persistence.
 * This interface belongs to the use case layer and defines the contract
 * for data access without depending on specific implementation details.
 */
public interface TaskRepository {
    /**
     * Saves a new task.
     *
     * @param task The task to save
     */
    void save(Task task);

    /**
     * Updates an existing task.
     *
     * @param task The task to update
     */
    void update(Task task);

    /**
     * Finds a task by its ID.
     *
     * @param taskId The ID of the task
     * @return The task if found, null otherwise
     */
    Task findById(String taskId);

    /**
     * Gets all tasks.
     *
     * @return List of all tasks
     */
    List<Task> findAll();

    /**
     * Gets today's scheduled tasks.
     *
     * @return List of tasks scheduled for today
     */
    List<Task> findTodaysTasks();

    /**
     * Gets overdue tasks.
     *
     * @return List of overdue tasks
     */
    List<Task> findOverdueTasks();

    /**
     * Adds a task to today's available tasks.
     *
     * @param task The task to add
     */
    void addToTodaysTasks(Task task);

    /**
     * Gets tasks by category.
     *
     * @param category The category to filter by
     * @return List of tasks in the category
     */
    List<Task> findByCategory(String category);

    /**
     * Gets tasks by priority.
     *
     * @param priority The priority to filter by
     * @return List of tasks with the priority
     */
    List<Task> findByPriority(Task.Priority priority);
}