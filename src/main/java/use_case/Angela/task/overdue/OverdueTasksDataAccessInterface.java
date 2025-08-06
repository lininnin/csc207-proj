package use_case.Angela.task.overdue;

import entity.Angela.Task.Task;
import java.time.LocalDate;
import java.util.List;

/**
 * Data access interface for retrieving overdue tasks.
 */
public interface OverdueTasksDataAccessInterface {
    /**
     * Gets all overdue tasks from Today's list within the specified days back.
     * A task is overdue if it has a due date before today and is not completed.
     * 
     * @param daysBack Number of days to look back for overdue tasks (e.g., 7 for last week)
     * @return List of overdue tasks sorted by most overdue first
     */
    List<Task> getOverdueTasks(int daysBack);
    
    /**
     * Gets all overdue tasks from Today's list regardless of time range.
     * 
     * @return List of all overdue tasks sorted by most overdue first
     */
    List<Task> getAllOverdueTasks();
}