package data_access.strategy;

import entity.Angela.Task.Task;
import use_case.Angela.task.mark_complete.MarkTaskCompleteDataAccessInterface;
import java.util.Map;

/**
 * Strategy for handling task completion operations.
 * Implements Single Responsibility Principle by focusing only on marking tasks complete.
 */
public class MarkTaskCompleteStrategy implements MarkTaskCompleteDataAccessInterface {
    
    private final Map<String, Task> todaysTasks;
    
    public MarkTaskCompleteStrategy(Map<String, Task> todaysTasks) {
        this.todaysTasks = todaysTasks;
    }
    
    @Override
    public entity.Angela.Task.TaskInterf getTodayTaskById(String taskId) {
        return todaysTasks.get(taskId);
    }
    
    @Override
    public boolean updateTaskCompletionStatus(String taskId, boolean isCompleted) {
        Task task = todaysTasks.get(taskId);
        if (task == null) {
            return false;
        }
        
        if (isCompleted) {
            task.markComplete();
        } else {
            task.unmarkComplete();
        }
        return true;
    }
}