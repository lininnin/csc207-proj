package use_case.Angela.task;

import entity.Angela.Task.Task;
import entity.info.Info;
import java.time.LocalDate;
import java.util.List;

/**
 * Gateway interface for task data access operations.
 * Manages both Available Tasks and Today's Tasks.
 * 
 * @deprecated This interface violates ISP. Use specific DataAccessInterface for each use case instead:
 * - CreateTaskDataAccessInterface for creating tasks
 * - EditAvailableTaskDataAccessInterface for editing available tasks  
 * - AddToTodayDataAccessInterface for adding tasks to today
 * - EditTodayTaskDataAccessInterface for editing today's tasks
 * - MarkTaskCompleteDataAccessInterface for marking tasks complete
 * - RemoveFromTodayDataAccessInterface for removing from today
 * - DeleteTaskDataAccessInterface for deleting tasks
 * - OverdueTasksDataAccessInterface for overdue tasks
 * - TodaySoFarDataAccessInterface for today's summary
 * - ViewHistoryDataAccessInterface for history
 */
@Deprecated
public interface TaskGateway {

    // Available Tasks operations

    /**
     * Saves a new task to Available Tasks.
     *
     * @param info The task info to save
     * @return The created task ID
     */
    String saveAvailableTask(Info info);

    /**
     * Gets all available tasks.
     *
     * @return List of available task infos
     */
    List<Info> getAllAvailableTasks();

    /**
     * Checks if a task name already exists in Available Tasks.
     *
     * @param name The task name to check
     * @return true if the name exists
     */
    boolean availableTaskNameExists(String name);

    /**
     * Checks if a task with the given name and category already exists.
     *
     * @param name The task name to check
     * @param category The category name (can be empty string for no category)
     * @return true if a task with this name and category combination exists
     */
    boolean taskExistsWithNameAndCategory(String name, String category);

    /**
     * Updates an available task.
     *
     * @param info The updated task info
     * @return true if updated successfully
     */
    boolean updateAvailableTask(Info info);

    /**
     * Deletes a task from Available Tasks.
     *
     * @param taskId The task ID
     * @return true if deleted successfully
     */
    boolean deleteFromAvailable(String taskId);

    // Today's Tasks operations

    /**
     * Adds a task to Today's Tasks with priority and optional due date.
     *
     * @param taskId The ID of the task from Available Tasks
     * @param priority The task priority
     * @param dueDate Optional due date
     * @return The created Today's Task
     */
    Task addToToday(String taskId, Task.Priority priority, LocalDate dueDate);

    /**
     * Gets all of today's tasks.
     *
     * @return List of today's tasks
     */
    List<Task> getTodaysTasks();

    /**
     * Updates a task in Today's Tasks.
     *
     * @param task The updated task
     * @return true if updated successfully
     */
    boolean updateTodaysTask(Task task);

    /**
     * Removes a task from Today's Tasks only.
     *
     * @param taskId The task ID
     * @return true if removed successfully
     */
    boolean removeFromToday(String taskId);

    /**
     * Marks a task as complete.
     *
     * @param taskId The task ID
     * @return true if marked successfully
     */
    boolean markTaskComplete(String taskId);

    /**
     * Unmarks a completed task.
     *
     * @param taskId The task ID
     * @return true if unmarked successfully
     */
    boolean unmarkTaskComplete(String taskId);

    // Combined operations

    /**
     * Checks if a task exists in Available Tasks.
     *
     * @param taskId The task ID
     * @return true if exists
     */
    boolean existsInAvailable(String taskId);

    /**
     * Checks if a task exists in Today's Tasks.
     *
     * @param taskId The task ID
     * @return true if exists
     */
    boolean existsInToday(String taskId);

    /**
     * Gets a task name by ID.
     *
     * @param taskId The task ID
     * @return The task name or null if not found
     */
    String getTaskName(String taskId);

    /**
     * Deletes a task completely from both Available and Today's lists.
     *
     * @param taskId The task ID
     * @return true if deleted successfully
     */
    boolean deleteTaskCompletely(String taskId);

    /**
     * Gets tasks with due dates for daily processing.
     *
     * @return List of tasks with due dates
     */
    List<Task> getTasksWithDueDates();

    /**
     * Gets overdue tasks within the last 7 days.
     *
     * @return List of overdue tasks
     */
    List<Task> getOverdueTasks();

    /**
     * Gets completed tasks for today.
     *
     * @return List of completed tasks
     */
    List<Task> getCompletedTasksToday();

    /**
     * Gets today's task completion rate.
     *
     * @return Completion rate between 0.0 and 100.0
     */
    double getTodaysCompletionRate();
}