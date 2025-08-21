package data_access.strategy;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskInterf;
import entity.info.Info;
import use_case.Angela.task.delete.DeleteTaskDataAccessInterface;
import data_access.GoalRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Strategy for handling task deletion operations.
 * Implements Single Responsibility Principle by focusing only on task deletion.
 */
public class DeleteTaskStrategy implements DeleteTaskDataAccessInterface {
    
    private final Map<String, TaskAvailable> availableTaskTemplates;
    private final Map<String, Task> todaysTasks;
    private final Map<String, Info> availableTasks; // Legacy storage
    private GoalRepository goalRepository; // Optional dependency
    
    public DeleteTaskStrategy(Map<String, TaskAvailable> availableTaskTemplates,
                             Map<String, Task> todaysTasks,
                             Map<String, Info> availableTasks) {
        this.availableTaskTemplates = availableTaskTemplates;
        this.todaysTasks = todaysTasks;
        this.availableTasks = availableTasks;
    }
    
    public void setGoalRepository(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }
    
    @Override
    public TaskAvailableInterf getTaskAvailableById(String taskId) {
        return availableTaskTemplates.get(taskId);
    }
    
    @Override
    public List<TaskAvailableInterf> getAllAvailableTaskTemplates() {
        return availableTaskTemplates.values().stream()
                .map(task -> (TaskAvailableInterf) task)
                .collect(Collectors.toList());
    }
    
    @Override
    public TaskInterf getTodaysTaskById(String taskId) {
        return todaysTasks.get(taskId);
    }
    
    @Override
    public boolean existsInAvailable(TaskAvailableInterf taskAvailable) {
        return availableTaskTemplates.containsKey(taskAvailable.getId());
    }
    
    @Override
    public boolean existsInToday(TaskInterf task) {
        return todaysTasks.containsKey(task.getId());
    }
    
    @Override
    public boolean templateExistsInToday(String templateTaskId) {
        return todaysTasks.values().stream()
                .anyMatch(task -> templateTaskId.equals(task.getTemplateTaskId()));
    }
    
    @Override
    public boolean deleteFromAvailable(TaskAvailableInterf taskAvailable) {
        String taskId = taskAvailable.getId();
        
        // Remove from both storages
        TaskAvailable removed = availableTaskTemplates.remove(taskId);
        availableTasks.remove(taskId);
        
        return removed != null;
    }
    
    @Override
    public boolean deleteAllTodaysTasksWithTemplate(String templateTaskId) {
        List<String> toRemove = todaysTasks.entrySet().stream()
                .filter(entry -> templateTaskId.equals(entry.getValue().getTemplateTaskId()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        toRemove.forEach(todaysTasks::remove);
        return !toRemove.isEmpty();
    }
    
    @Override
    public boolean deleteTaskCompletely(String templateTaskId) {
        // Check for goal dependencies first
        if (goalRepository != null) {
            List<String> dependentGoals = getGoalNamesTargetingTask(templateTaskId);
            if (!dependentGoals.isEmpty()) {
                return false; // Cannot delete if goals depend on this task
            }
        }
        
        // Remove from available
        TaskAvailable removed = availableTaskTemplates.remove(templateTaskId);
        availableTasks.remove(templateTaskId);
        
        // Remove all today instances
        deleteAllTodaysTasksWithTemplate(templateTaskId);
        
        return removed != null;
    }
    
    @Override
    public List<TaskInterf> getTodaysTasksByTemplate(String templateTaskId) {
        return todaysTasks.values().stream()
                .filter(task -> templateTaskId.equals(task.getTemplateTaskId()))
                .map(task -> (TaskInterf) task)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TaskInterf> getAllTodaysTasks() {
        return todaysTasks.values().stream()
                .map(task -> (TaskInterf) task)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getGoalNamesTargetingTask(String taskId) {
        if (goalRepository == null) {
            return List.of();
        }
        
        return goalRepository.getAllGoals().stream()
                .filter(goal -> goal.getTargetTaskInfo() != null && 
                               taskId.equals(goal.getTargetTaskInfo().getId()))
                .map(goal -> goal.getInfo().getName())
                .collect(Collectors.toList());
    }
}