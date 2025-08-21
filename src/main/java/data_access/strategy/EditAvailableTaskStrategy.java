package data_access.strategy;

import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.Task;
import entity.info.Info;
import entity.info.InfoInterf;
import use_case.Angela.task.edit_available.EditAvailableTaskDataAccessInterface;
import java.util.List;
import java.util.Map;

/**
 * Strategy for handling available task editing operations.
 * Implements Single Responsibility Principle by focusing only on editing available tasks.
 */
public class EditAvailableTaskStrategy implements EditAvailableTaskDataAccessInterface {
    
    private final Map<String, TaskAvailable> availableTaskTemplates;
    private final Map<String, Task> todaysTasks;
    private final Map<String, Info> availableTasks; // Legacy storage for backward compatibility
    
    public EditAvailableTaskStrategy(Map<String, TaskAvailable> availableTaskTemplates,
                                   Map<String, Task> todaysTasks,
                                   Map<String, Info> availableTasks) {
        this.availableTaskTemplates = availableTaskTemplates;
        this.todaysTasks = todaysTasks;
        this.availableTasks = availableTasks;
    }
    
    @Override
    public boolean updateAvailableTask(String taskId, String newName, String newDescription, 
                                       String newCategoryId, boolean isOneTime) {
        TaskAvailable existingTask = availableTaskTemplates.get(taskId);
        if (existingTask == null) {
            return false;
        }
        
        // Update the Info object
        InfoInterf info = existingTask.getInfo();
        info.setName(newName);
        info.setDescription(newDescription != null ? newDescription : "");
        info.setCategory(newCategoryId != null ? newCategoryId : "");
        
        // Update the one-time flag
        existingTask.setOneTime(isOneTime);
        
        // Update legacy storage for backward compatibility
        availableTasks.put(taskId, (Info) info);
        
        // Propagate ALL changes to Today's tasks
        for (Task todayTask : todaysTasks.values()) {
            if (taskId.equals(todayTask.getTemplateTaskId())) {
                // Update ALL properties in today's task
                InfoInterf todayInfo = todayTask.getInfo();
                todayInfo.setName(newName);
                todayInfo.setDescription(newDescription != null ? newDescription : "");
                todayInfo.setCategory(newCategoryId != null ? newCategoryId : "");
                
                // Also update the one-time flag if it changed
                todayTask.setOneTime(isOneTime);
            }
        }
        
        return true;
    }
    
    @Override
    public boolean taskExistsWithNameAndCategoryExcluding(String name, String categoryId, String excludeTaskId) {
        if (name == null) {
            return false;
        }
        
        String normalizedCategory = (categoryId == null || categoryId.trim().isEmpty()) ? "" : categoryId.trim();
        
        return availableTaskTemplates.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(excludeTaskId))
                .map(Map.Entry::getValue)
                .filter(task -> task != null && task.getInfo() != null)
                .anyMatch(task -> {
                    InfoInterf info = task.getInfo();
                    String taskName = info.getName();
                    String taskCategory = info.getCategory();
                    String normalizedTaskCategory = (taskCategory == null || taskCategory.trim().isEmpty()) 
                        ? "" : taskCategory.trim();
                    
                    return taskName != null && taskName.equalsIgnoreCase(name) &&
                           normalizedTaskCategory.equalsIgnoreCase(normalizedCategory);
                });
    }
    
    @Override
    public TaskAvailableInterf getTaskAvailableById(String taskId) {
        return availableTaskTemplates.get(taskId);
    }
    
    @Override
    public List<TaskAvailableInterf> getAllAvailableTasksWithDetails() {
        return availableTaskTemplates.values().stream()
                .map(task -> (TaskAvailableInterf) task)
                .collect(java.util.stream.Collectors.toList());
    }
}