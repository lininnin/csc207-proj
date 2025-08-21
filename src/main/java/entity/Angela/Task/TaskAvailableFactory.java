package entity.Angela.Task;

import entity.info.Info;
import entity.info.InfoInterf;
import entity.info.ImmutableInfo;

/**
 * Factory class for creating TaskAvailable instances.
 * Implements the Factory pattern following Clean Architecture principles.
 * 
 * Provides methods to create both mutable and immutable TaskAvailable entities.
 * Uses interface types for dependency inversion compliance.
 */
public class TaskAvailableFactory {

    /**
     * Creates a new available task template.
     *
     * @param info Task information
     * @return A new TaskAvailable instance
     * @throws IllegalArgumentException if info is null
     */
    public TaskAvailableInterf create(InfoInterf info) {
        // Convert interface to concrete type for TaskAvailable constructor
        Info concreteInfo = (info instanceof Info) ? (Info) info : ((ImmutableInfo) info).toMutableInfo();
        return new TaskAvailable(concreteInfo);
    }

    /**
     * Creates an available task with one-time flag.
     *
     * @param info Task information
     * @param isOneTime Whether this is a one-time task
     * @return A new TaskAvailable instance
     * @throws IllegalArgumentException if info is null
     */
    public TaskAvailableInterf create(InfoInterf info, boolean isOneTime) {
        // Convert interface to concrete type for TaskAvailable constructor
        Info concreteInfo = (info instanceof Info) ? (Info) info : ((ImmutableInfo) info).toMutableInfo();
        TaskAvailable task = new TaskAvailable(concreteInfo);
        return task.withOneTimeFlag(isOneTime); // Use immutable update method
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
    public TaskAvailableInterf create(String id, InfoInterf info, String plannedDueDate, boolean isOneTime) {
        // Convert interface to concrete type for TaskAvailable constructor
        Info concreteInfo = (info instanceof Info) ? (Info) info : ((ImmutableInfo) info).toMutableInfo();
        return new TaskAvailable(id, concreteInfo, plannedDueDate, isOneTime);
    }
    
    // Factory methods for immutable entity creation
    
    /**
     * Creates a new available task template using immutable entities.
     *
     * @param info Immutable task information
     * @return A new TaskAvailable instance with immutable components
     * @throws IllegalArgumentException if info is null
     */
    public TaskAvailableInterf createImmutable(ImmutableInfo info) {
        if (info == null) {
            throw new IllegalArgumentException("Info cannot be null");
        }
        return new TaskAvailable(info.toMutableInfo());
    }
    
    /**
     * Creates an available task with one-time flag using immutable entities.
     *
     * @param info Immutable task information
     * @param isOneTime Whether this is a one-time task
     * @return A new TaskAvailable instance with immutable components
     * @throws IllegalArgumentException if info is null
     */
    public TaskAvailableInterf createImmutable(ImmutableInfo info, boolean isOneTime) {
        if (info == null) {
            throw new IllegalArgumentException("Info cannot be null");
        }
        TaskAvailable task = new TaskAvailable(info.toMutableInfo());
        return task.withOneTimeFlag(isOneTime); // Use immutable update method
    }
}