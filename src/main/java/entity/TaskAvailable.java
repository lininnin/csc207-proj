package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the collection of tasks available to be added to a specific day.
 * This serves as a pool of tasks that users can choose from when planning their day.
 *
 * TODO: Will be used in Sophia's story #2 for filtering and organizing tasks by category/priority
 */
public class TaskAvailable {
    private final List<Info> taskInfoAvailable;

    /**
     * Constructs a new TaskAvailable with an empty list of tasks.
     */
    public TaskAvailable() {
        this.taskInfoAvailable = new ArrayList<>();
    }

    /**
     * Adds a task to the available tasks pool.
     *
     * @param info The Info to add
     * @throws IllegalArgumentException if info is null
     */
    public void addTask(Info info) {
        if (info == null) {
            throw new IllegalArgumentException("Info cannot be null");
        }
        taskInfoAvailable.add(info);
    }

    /**
     * Removes a task from the available tasks pool.
     *
     * @param info The Info to remove
     * @return true if the Info was removed, false if it wasn't in the list
     */
    public boolean removeTask(Info info) {
        return taskInfoAvailable.remove(info);
    }

    /**
     * Returns all available tasks.
     *
     * @return A copy of the list of available Info
     */
    public List<Info> getTaskAvailable() {
        return new ArrayList<>(taskInfoAvailable);
    }

    /**
     * Returns tasks filtered by category.
     *
     * @param category The category to filter by
     * @return List of Info in the specified category
     */
    public List<Info> getTasksByCategory(String category) {
        List<Info> filtered = new ArrayList<>();
        for (Info info : taskInfoAvailable) {
            if (category.equals(info.getCategory())) {
                filtered.add(info);
            }
        }
        return filtered;
    }

    /**
     * Returns tasks filtered by name.
     *
     * @param name The name to filter by
     * @return List of Info with the specified name
     */
    public List<Info> getTasksByName(String name) {
        List<Info> filtered = new ArrayList<>();
        for (Info info : taskInfoAvailable) {
            if (name.equals(info.getName())) {
                filtered.add(info);
            }
        }
        return filtered;
    }

    /**
     * Returns the number of available tasks.
     *
     * @return The size of the info list
     */
    public int getTaskCount() {
        return taskInfoAvailable.size();
    }

    /**
     * Checks if a specific Info is in the available list.
     *
     * @param info The Info to check
     * @return true if the Info is available, false otherwise
     */
    public boolean contains(Info info) {
        return taskInfoAvailable.contains(info);
    }

    /**
     * Clears all Info from the available list.
     */
    public void clearAll() {
        taskInfoAvailable.clear();
    }
}
