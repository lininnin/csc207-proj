package use_case.Angela.today_so_far;

import entity.Angela.Task.Task;
import entity.Alex.Event.Event;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Sophia.Goal;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Access Interface for Today So Far panel.
 * Aggregates data from Tasks, Events, Wellness, and Goals modules.
 */
public interface TodaySoFarDataAccessInterface {
    
    /**
     * Gets all completed tasks for today.
     * @return List of completed tasks for today
     */
    List<Task> getCompletedTasksForToday();
    
    /**
     * Gets all completed events for today.
     * @return List of completed events for today
     */
    List<Event> getCompletedEventsForToday();
    
    /**
     * Gets wellness log entries for today.
     * @return List of wellness entries for today
     */
    List<WellnessLogEntry> getWellnessEntriesForToday();
    
    /**
     * Gets all active goals with their progress.
     * @return List of active goals
     */
    List<Goal> getActiveGoals();
    
    /**
     * Gets the task completion rate for today.
     * @return Completion rate as a percentage (0-100)
     */
    int getTodayTaskCompletionRate();
    
    /**
     * Gets the total number of tasks for today.
     * @return Total task count
     */
    int getTotalTasksForToday();
    
    /**
     * Gets the total number of completed tasks for today.
     * @return Completed task count
     */
    int getCompletedTasksCountForToday();
}