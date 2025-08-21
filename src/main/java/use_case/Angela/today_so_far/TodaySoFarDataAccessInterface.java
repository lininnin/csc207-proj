package use_case.Angela.today_so_far;

import entity.Angela.Task.TaskInterf;
import entity.alex.Event.EventInterf;
import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.Sophia.GoalInterface;

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
    List<TaskInterf> getCompletedTasksForToday();
    
    /**
     * Gets all completed events for today.
     * @return List of completed events for today
     */
    List<EventInterf> getCompletedEventsForToday();
    
    /**
     * Gets wellness log entries for today.
     * @return List of wellness entries for today
     */
    List<WellnessLogEntryInterf> getWellnessEntriesForToday();
    
    /**
     * Gets all active goals with their progress.
     * @return List of active goals
     */
    List<GoalInterface> getActiveGoals();
    
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