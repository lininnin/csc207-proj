package data_access.strategy;

import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.TaskAvailableInterf;
import use_case.Angela.task.create.CreateTaskDataAccessInterface;
import java.util.List;
import java.util.Map;

/**
 * Strategy for handling task creation operations.
 * Implements Single Responsibility Principle by focusing only on task creation.
 */
public class CreateTaskStrategy implements CreateTaskDataAccessInterface {
    
    private final Map<String, TaskAvailable> availableTaskTemplates;
    
    public CreateTaskStrategy(Map<String, TaskAvailable> availableTaskTemplates) {
        this.availableTaskTemplates = availableTaskTemplates;
    }
    
    @Override
    public String saveTaskAvailable(TaskAvailableInterf taskAvailable) {
        TaskAvailable concrete = (TaskAvailable) taskAvailable;
        String taskId = concrete.getId();
        availableTaskTemplates.put(taskId, concrete);
        return taskId;
    }
    
    @Override
    public boolean taskExistsWithNameAndCategory(String name, String categoryId) {
        if (name == null) {
            return false;
        }
        
        String normalizedCategory = (categoryId == null || categoryId.trim().isEmpty()) ? "" : categoryId.trim();
        
        return availableTaskTemplates.values().stream()
                .filter(task -> task != null && task.getInfo() != null)
                .anyMatch(task -> {
                    String taskName = task.getInfo().getName();
                    String taskCategory = task.getInfo().getCategory();
                    String normalizedTaskCategory = (taskCategory == null || taskCategory.trim().isEmpty()) 
                        ? "" : taskCategory.trim();
                    
                    return taskName != null && taskName.equalsIgnoreCase(name) &&
                           normalizedTaskCategory.equalsIgnoreCase(normalizedCategory);
                });
    }
    
    @Override
    public List<TaskAvailableInterf> getAllAvailableTaskTemplates() {
        return availableTaskTemplates.values().stream()
                .map(task -> (TaskAvailableInterf) task)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public TaskAvailableInterf getTaskAvailableById(String taskId) {
        return availableTaskTemplates.get(taskId);
    }
    
    @Override
    public int getAvailableTaskCount() {
        return availableTaskTemplates.size();
    }
    
    @Override
    public boolean exists(TaskAvailableInterf taskAvailable) {
        return availableTaskTemplates.containsKey(taskAvailable.getId());
    }
}