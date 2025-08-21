package entity.Angela.Task;

import entity.info.InfoInterf;

/**
 * Interface defining the contract for TaskAvailable entities.
 * Follows Clean Architecture principles by defining what a TaskAvailable must provide.
 * This interface enables dependency inversion, allowing use cases to depend on
 * abstractions rather than concrete implementations.
 */
public interface TaskAvailableInterf {
    /**
     * Gets the unique identifier for this task template.
     * @return The task ID
     */
    String getId();
    
    /**
     * Gets the task's basic information.
     * @return The InfoInterf object containing name, description, category
     */
    InfoInterf getInfo();
    
    /**
     * Checks if this is a one-time task.
     * @return true if one-time, false if regular
     */
    boolean isOneTime();
    
    /**
     * Gets the planned due date for this task.
     * @return The planned due date in ISO format, or null if not set
     */
    String getPlannedDueDate();
    
    /**
     * Sets whether this is a one-time task.
     * @param oneTime true for one-time, false for regular
     */
    void setOneTime(boolean oneTime);
    
    /**
     * Sets the planned due date for this task.
     * @param plannedDueDate The planned due date in ISO format, or null
     */
    void setPlannedDueDate(String plannedDueDate);
}