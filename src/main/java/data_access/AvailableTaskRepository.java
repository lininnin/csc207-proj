package data_access;

import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskAvailable;
import entity.info.InfoInterf;
import entity.info.Info;
import use_case.Angela.task.create.CreateTaskDataAccessInterface;
import use_case.Angela.task.edit_available.EditAvailableTaskDataAccessInterface;
import use_case.Angela.task.delete.DeleteTaskDataAccessInterface;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for managing Available Task templates.
 * Follows SRP by focusing only on Available Task CRUD operations.
 * Implements multiple DataAccessInterfaces for different use cases.
 */
public class AvailableTaskRepository implements 
        CreateTaskDataAccessInterface,
        EditAvailableTaskDataAccessInterface {
        
    private final Map<String, TaskAvailable> availableTaskTemplates = Collections.synchronizedMap(new HashMap<>());

    /**
     * Saves a TaskAvailable template to the repository.
     *
     * @param taskAvailable The TaskAvailableInterf template to save
     * @return The ID of the saved task
     */
    @Override
    public String saveTaskAvailable(TaskAvailableInterf taskAvailable) {
        if (taskAvailable == null) {
            throw new IllegalArgumentException("TaskAvailable cannot be null");
        }
        
        // Convert interface to concrete type for storage
        TaskAvailable concreteTask = (TaskAvailable) taskAvailable;
        availableTaskTemplates.put(concreteTask.getId(), concreteTask);
        return concreteTask.getId();
    }

    /**
     * Checks if a task template with the given name already exists in the same category.
     *
     * @param name The task name to check
     * @param categoryId The category ID to check (null or empty for no category)
     * @return true if a task with this name and category combination exists
     */
    @Override
    public boolean taskExistsWithNameAndCategory(String name, String categoryId) {
        if (name == null) return false;
        
        String normalizedName = name.trim().toLowerCase();
        String normalizedCategory = (categoryId == null || categoryId.trim().isEmpty()) ? "" : categoryId.trim();
        
        return availableTaskTemplates.values().stream()
                .anyMatch(task -> {
                    String taskName = task.getInfo().getName().trim().toLowerCase();
                    String taskCategory = task.getInfo().getCategory();
                    String normalizedTaskCategory = (taskCategory == null || taskCategory.trim().isEmpty()) ? "" : taskCategory.trim();
                    
                    return taskName.equals(normalizedName) && normalizedTaskCategory.equals(normalizedCategory);
                });
    }

    /**
     * @return List of all available task templates
     */
    @Override
    public List<TaskAvailableInterf> getAllAvailableTaskTemplates() {
        return availableTaskTemplates.values().stream()
                .map(task -> (TaskAvailableInterf) task)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a task template by its ID.
     *
     * @param taskId The ID of the task template to find
     * @return The TaskAvailableInterf if found, null otherwise
     */
    @Override
    public TaskAvailableInterf getTaskAvailableById(String taskId) {
        return availableTaskTemplates.get(taskId);
    }

    /**
     * @return Total count of available task templates
     */
    @Override
    public int getAvailableTaskCount() {
        return availableTaskTemplates.size();
    }

    /**
     * Checks whether the given TaskAvailableInterf exists in the repository.
     *
     * @param taskAvailable The TaskAvailableInterf to check
     * @return true if present, false otherwise
     */
    @Override
    public boolean exists(TaskAvailableInterf taskAvailable) {
        if (taskAvailable == null) return false;
        return availableTaskTemplates.containsKey(taskAvailable.getId());
    }

    // EditAvailableTaskDataAccessInterface methods

    /**
     * Updates an existing available task with new information.
     *
     * @param taskId The ID of the task to update
     * @param newName The new name for the task
     * @param newDescription The new description for the task
     * @param newCategoryId The new category ID for the task (can be empty)
     * @param isOneTime Whether the task is a one-time task
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean updateAvailableTask(String taskId, String newName, String newDescription, 
                                       String newCategoryId, boolean isOneTime) {
        TaskAvailable task = availableTaskTemplates.get(taskId);
        if (task == null) {
            return false;
        }

        try {
            // Use existing mutable approach (cast to concrete class)
            Info info = (Info) task.getInfo();
            info.setName(newName);
            info.setDescription(newDescription != null ? newDescription : "");
            info.setCategory(newCategoryId != null ? newCategoryId : "");
            
            // Create new TaskAvailable with updated info and one-time flag
            TaskAvailable updatedTask = new TaskAvailable(task.getId(), 
                                                         info, 
                                                         task.getPlannedDueDate(), 
                                                         isOneTime);
            
            availableTaskTemplates.put(taskId, updatedTask);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating task: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a task with the given name exists in the same category.
     * Used for duplicate checking during edit.
     *
     * @param name The task name to check
     * @param categoryId The category ID to check within
     * @param excludeTaskId The task ID to exclude from the check (the task being edited)
     * @return true if a duplicate exists, false otherwise
     */
    @Override
    public boolean taskExistsWithNameAndCategoryExcluding(String name, String categoryId, String excludeTaskId) {
        if (name == null) return false;
        
        String normalizedName = name.trim().toLowerCase();
        String normalizedCategory = (categoryId == null || categoryId.trim().isEmpty()) ? "" : categoryId.trim();
        
        return availableTaskTemplates.values().stream()
                .filter(task -> !task.getId().equals(excludeTaskId)) // Exclude the task being edited
                .anyMatch(task -> {
                    String taskName = task.getInfo().getName().trim().toLowerCase();
                    String taskCategory = task.getInfo().getCategory();
                    String normalizedTaskCategory = (taskCategory == null || taskCategory.trim().isEmpty()) ? "" : taskCategory.trim();
                    
                    return taskName.equals(normalizedName) && normalizedTaskCategory.equals(normalizedCategory);
                });
    }

    /**
     * Gets all available tasks with their complete information including isOneTime flag.
     *
     * @return List of all TaskAvailableInterf objects
     */
    @Override
    public List<TaskAvailableInterf> getAllAvailableTasksWithDetails() {
        return getAllAvailableTaskTemplates();
    }

    // Additional methods for deletion and other operations can be added here
    
    /**
     * Removes a task template from the repository.
     *
     * @param taskId The ID of the task template to remove
     * @return true if removed successfully, false if not found
     */
    public boolean removeTaskTemplate(String taskId) {
        return availableTaskTemplates.remove(taskId) != null;
    }

    /**
     * Gets all task templates that match the given category.
     *
     * @param categoryId The category ID to filter by
     * @return List of matching task templates
     */
    public List<TaskAvailableInterf> getTaskTemplatesByCategory(String categoryId) {
        String normalizedCategory = (categoryId == null || categoryId.trim().isEmpty()) ? "" : categoryId.trim();
        
        return availableTaskTemplates.values().stream()
                .filter(task -> {
                    String taskCategory = task.getInfo().getCategory();
                    String normalizedTaskCategory = (taskCategory == null || taskCategory.trim().isEmpty()) ? "" : taskCategory.trim();
                    return normalizedTaskCategory.equals(normalizedCategory);
                })
                .map(task -> (TaskAvailableInterf) task)
                .collect(Collectors.toList());
    }
}