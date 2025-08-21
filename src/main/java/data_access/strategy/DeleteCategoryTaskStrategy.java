package data_access.strategy;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import use_case.Angela.category.delete.DeleteCategoryTaskDataAccessInterface;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Strategy for handling category deletion operations on tasks.
 * Implements Single Responsibility Principle by focusing only on task operations during category deletion.
 */
public class DeleteCategoryTaskStrategy implements DeleteCategoryTaskDataAccessInterface {
    
    private final Map<String, TaskAvailable> availableTaskTemplates;
    private final Map<String, Task> todaysTasks;
    
    public DeleteCategoryTaskStrategy(Map<String, TaskAvailable> availableTaskTemplates,
                                     Map<String, Task> todaysTasks) {
        this.availableTaskTemplates = availableTaskTemplates;
        this.todaysTasks = todaysTasks;
    }
    
    @Override
    public List<TaskAvailable> findAvailableTasksByCategory(String categoryId) {
        return availableTaskTemplates.values().stream()
                .filter(task -> task != null && task.getInfo() != null)
                .filter(task -> {
                    String taskCategory = task.getInfo().getCategory();
                    if (categoryId == null || categoryId.trim().isEmpty()) {
                        return taskCategory == null || taskCategory.trim().isEmpty();
                    }
                    return categoryId.equals(taskCategory);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Task> findTodaysTasksByCategory(String categoryId) {
        return todaysTasks.values().stream()
                .filter(task -> task != null && task.getInfo() != null)
                .filter(task -> {
                    String taskCategory = task.getInfo().getCategory();
                    if (categoryId == null || categoryId.trim().isEmpty()) {
                        return taskCategory == null || taskCategory.trim().isEmpty();
                    }
                    return categoryId.equals(taskCategory);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean updateAvailableTaskCategory(String taskId, String newCategoryId) {
        TaskAvailable task = availableTaskTemplates.get(taskId);
        if (task == null || task.getInfo() == null) {
            return false;
        }
        
        task.getInfo().setCategory(newCategoryId != null ? newCategoryId : "");
        return true;
    }
    
    @Override
    public boolean updateTodaysTaskCategory(String taskId, String newCategoryId) {
        Task task = todaysTasks.get(taskId);
        if (task == null || task.getInfo() == null) {
            return false;
        }
        
        task.getInfo().setCategory(newCategoryId != null ? newCategoryId : "");
        return true;
    }
    
    @Override
    public List<TaskAvailable> findAvailableTasksWithEmptyCategory() {
        return findAvailableTasksByCategory("");
    }
    
    @Override
    public List<Task> findTodaysTasksWithEmptyCategory() {
        return findTodaysTasksByCategory("");
    }
}