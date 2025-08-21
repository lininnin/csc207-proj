package data_access.strategy;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskInterf;
import use_case.Angela.task.edit_today.EditTodayTaskDataAccessInterface;
import java.time.LocalDate;
import java.util.Map;

/**
 * Strategy for handling today's task editing operations.
 * Implements Single Responsibility Principle by focusing only on editing today's tasks.
 */
public class EditTodayTaskStrategy implements EditTodayTaskDataAccessInterface {
    
    private final Map<String, Task> todaysTasks;
    
    public EditTodayTaskStrategy(Map<String, Task> todaysTasks) {
        this.todaysTasks = todaysTasks;
    }
    
    @Override
    public TaskInterf getTodayTaskById(String taskId) {
        return todaysTasks.get(taskId);
    }
    
    @Override
    public boolean updateTodayTaskPriorityAndDueDate(String taskId, Task.Priority priority, LocalDate dueDate) {
        Task task = todaysTasks.get(taskId);
        if (task == null) {
            return false;
        }
        
        // Update priority
        task.setPriority(priority);
        
        // Update due date
        if (dueDate != null) {
            task.getDates().setDueDate(dueDate);
        }
        
        return true;
    }
    
    @Override
    public boolean isValidDueDate(LocalDate dueDate) {
        // Due date should not be in the past
        return dueDate == null || !dueDate.isBefore(LocalDate.now());
    }
}