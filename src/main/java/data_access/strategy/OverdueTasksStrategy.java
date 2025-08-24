package data_access.strategy;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskInterf;
import use_case.Angela.task.overdue.OverdueTasksDataAccessInterface;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Strategy for handling overdue task operations.
 * Implements Single Responsibility Principle by focusing only on overdue task queries.
 */
public class OverdueTasksStrategy implements OverdueTasksDataAccessInterface {
    
    private final Map<String, Task> todaysTasks;
    
    public OverdueTasksStrategy(Map<String, Task> todaysTasks) {
        this.todaysTasks = todaysTasks;
    }
    
    @Override
    public List<TaskInterf> getOverdueTasks(int daysBack) {
        LocalDate cutoffDate = LocalDate.now().minusDays(daysBack);
        
        return todaysTasks.values().stream()
                .filter(task -> task.getBeginAndDueDates().getDueDate() != null)
                .filter(task -> task.getBeginAndDueDates().getDueDate().isBefore(LocalDate.now()))
                .filter(task -> !task.getBeginAndDueDates().getDueDate().isBefore(cutoffDate))
                .filter(task -> !task.isCompleted()) // Exclude completed tasks
                .map(task -> (TaskInterf) task)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TaskInterf> getAllOverdueTasks() {
        return todaysTasks.values().stream()
                .filter(task -> task.getBeginAndDueDates().getDueDate() != null)
                .filter(task -> task.getBeginAndDueDates().getDueDate().isBefore(LocalDate.now()))
                .filter(task -> !task.isCompleted()) // Exclude completed tasks
                .map(task -> (TaskInterf) task)
                .collect(Collectors.toList());
    }
}