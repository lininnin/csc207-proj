package use_case.Angela.task.create;

import entity.Angela.Task.TaskAvailable;
import entity.info.Info;

import java.util.List;

/**
 * Interface for the Create Task data access object.
 * Defines methods to persist and query task templates (TaskAvailable) for the CreateTask use case.
 * 
 * Note: This interface is for creating TaskAvailable (templates in Available list).
 * Creating Task instances (Today's tasks) is handled by AddTaskToToday use case.
 */
public interface CreateTaskDataAccessInterface {

    /**
     * Saves the given TaskAvailable to the data store.
     *
     * @param taskAvailable The TaskAvailable template to save
     * @return The ID of the saved task
     */
    String saveTaskAvailable(TaskAvailable taskAvailable);

    /**
     * Checks if a task template with the given name already exists in the same category (case-insensitive).
     * This implements the duplicate checking rule: case-insensitive name + same category.
     *
     * @param name The task name to check
     * @param categoryId The category ID to check (null or empty for no category)
     * @return true if a task with this name and category combination exists, false otherwise
     */
    boolean taskExistsWithNameAndCategory(String name, String categoryId);

    /**
     * @return List of all available task templates
     */
    List<TaskAvailable> getAllAvailableTaskTemplates();

    /**
     * Retrieves a task template by its ID.
     *
     * @param taskId The ID of the task template to find
     * @return The TaskAvailable if found, null otherwise
     */
    TaskAvailable getTaskAvailableById(String taskId);

    /**
     * @return Total count of available task templates
     */
    int getAvailableTaskCount();

    /**
     * Checks whether the given TaskAvailable exists in the data store.
     *
     * @param taskAvailable The TaskAvailable to check
     * @return true if present, false otherwise
     */
    boolean exists(TaskAvailable taskAvailable);
}