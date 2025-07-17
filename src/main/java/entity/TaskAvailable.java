package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the collection of tasks available to be added to a specific day.
 * This serves as a pool of tasks that users can choose from when planning their day.
 */
public class TaskAvailable {
    private final List<Task> taskAvailable;

    /**
     * Constructs a new TaskAvailable with an empty list of tasks.
     */
    public TaskAvailable() {
        this.taskAvailable = new ArrayList<>();
    }

    /**
     * Adds a task to the available tasks pool.
     *
     * @param task The task to add
     * @throws IllegalArgumentException if task is null
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        taskAvailable.add(task);
    }

    /**
     * Removes a task from the available tasks pool.
     *
     * @param task The task to remove
     * @return true if the task was removed, false if it wasn't in the list
     */
    public boolean removeTask(Task task) {
        return taskAvailable.remove(task);
    }

    /**
     * Returns all available tasks.
     *
     * @return A copy of the list of available tasks
     */
    public List<Task> getTaskAvailable() {
        return new ArrayList<>(taskAvailable);
    }

    /**
     * Returns tasks filtered by category.
     *
     * @param category The category to filter by
     * @return List of tasks in the specified category
     */
    public List<Task> getTasksByCategory(String category) {
        List<Task> filtered = new ArrayList<>();
        for (Task task : taskAvailable) {
            if (category.equals(task.getInfo().getCategory())) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    /**
     * Returns tasks filtered by priority.
     *
     * @param priority The priority level to filter by
     * @return List of tasks with the specified priority
     */
    public List<Task> getTasksByPriority(Task.Priority priority) {
        List<Task> filtered = new ArrayList<>();
        for (Task task : taskAvailable) {
            if (task.getTaskPriority() == priority) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    /**
     * Returns incomplete tasks only.
     *
     * @return List of tasks that are not completed
     */
    public List<Task> getIncompleteTasks() {
        List<Task> incomplete = new ArrayList<>();
        for (Task task : taskAvailable) {
            if (!task.isComplete()) {
                incomplete.add(task);
            }
        }
        return incomplete;
    }

    /**
     * Returns the number of available tasks.
     *
     * @return The size of the task list
     */
    public int getTaskCount() {
        return taskAvailable.size();
    }

    /**
     * Checks if a specific task is in the available list.
     *
     * @param task The task to check
     * @return true if the task is available, false otherwise
     */
    public boolean contains(Task task) {
        return taskAvailable.contains(task);
    }

    /**
     * Clears all tasks from the available list.
     */
    public void clearAll() {
        taskAvailable.clear();
    }
}