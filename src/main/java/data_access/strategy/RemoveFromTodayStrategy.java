package data_access.strategy;

import entity.Angela.Task.Task;
import use_case.Angela.task.remove_from_today.RemoveFromTodayDataAccessInterface;
import java.util.Map;

/**
 * Strategy for handling remove from today operations.
 * Implements Single Responsibility Principle by focusing only on removing tasks from today.
 */
public class RemoveFromTodayStrategy implements RemoveFromTodayDataAccessInterface {
    
    private final Map<String, Task> todaysTasks;
    
    public RemoveFromTodayStrategy(Map<String, Task> todaysTasks) {
        this.todaysTasks = todaysTasks;
    }
    
    @Override
    public entity.Angela.Task.TaskInterf getTodayTaskById(String taskId) {
        return todaysTasks.get(taskId);
    }
    
    @Override
    public boolean removeFromTodaysList(String taskId) {
        return todaysTasks.remove(taskId) != null;
    }
}