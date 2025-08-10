package data_access;

import entity.Angela.Task.Task;
import entity.Alex.Event.Event;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Sophia.Goal;
import use_case.Angela.today_so_far.TodaySoFarDataAccessInterface;
import interface_adapter.GoalRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In-memory implementation that aggregates data from existing data access objects.
 */
public class InMemoryTodaySoFarDataAccess implements TodaySoFarDataAccessInterface {
    
    private final InMemoryTaskGateway taskGateway;
    private final TodaysEventDataAccessObject eventDataAccess;
    private final TodaysWellnessLogDataAccessObject wellnessDataAccess;
    private final GoalRepository goalRepository;
    
    public InMemoryTodaySoFarDataAccess(InMemoryTaskGateway taskGateway,
                                        TodaysEventDataAccessObject eventDataAccess,
                                        TodaysWellnessLogDataAccessObject wellnessDataAccess,
                                        GoalRepository goalRepository) {
        this.taskGateway = taskGateway;
        this.eventDataAccess = eventDataAccess;
        this.wellnessDataAccess = wellnessDataAccess;
        this.goalRepository = goalRepository;
    }
    
    // Constructor with optional parameters for gradual integration
    public InMemoryTodaySoFarDataAccess(InMemoryTaskGateway taskGateway) {
        this.taskGateway = taskGateway;
        this.eventDataAccess = null;
        this.wellnessDataAccess = null;
        this.goalRepository = null;
    }
    
    @Override
    public List<Task> getCompletedTasksForToday() {
        if (taskGateway != null) {
            return taskGateway.getTodaysTasks().stream()
                    .filter(Task::isCompleted)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    
    @Override
    public List<Event> getCompletedEventsForToday() {
        if (eventDataAccess != null) {
            try {
                // Get today's events - consider all events as "completed" for display purposes
                // since they're logged events for today
                return eventDataAccess.getTodaysEvents();
            } catch (Exception e) {
                // Handle if the method doesn't exist or throws exception
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }
    
    @Override
    public List<WellnessLogEntry> getWellnessEntriesForToday() {
        if (wellnessDataAccess != null) {
            try {
                // Get today's wellness log entries
                return wellnessDataAccess.getTodaysWellnessLogEntries();
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }
    
    @Override
    public List<Goal> getActiveGoals() {
        if (goalRepository != null) {
            try {
                LocalDate today = LocalDate.now();
                // Get current goals (which are the active ones)
                return goalRepository.getCurrentGoals().stream()
                        .filter(goal -> isGoalActive(goal, today))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }
    
    @Override
    public int getTodayTaskCompletionRate() {
        int total = getTotalTasksForToday();
        if (total == 0) return 0;
        
        int completed = getCompletedTasksCountForToday();
        return (int) Math.round((completed * 100.0) / total);
    }
    
    @Override
    public int getTotalTasksForToday() {
        if (taskGateway != null) {
            // Only count tasks that are actually for today (not overdue)
            // Overdue tasks should not be counted in today's total
            return (int) taskGateway.getTodaysTasks().stream()
                    .filter(task -> !task.isOverdue())
                    .count();
        }
        return 0;
    }
    
    @Override
    public int getCompletedTasksCountForToday() {
        if (taskGateway != null) {
            // Count completed tasks that are for today (not overdue)
            // Note: If a task was overdue but completed today, it still counts as today's completion
            return (int) taskGateway.getTodaysTasks().stream()
                    .filter(task -> task.isCompleted() && !task.isOverdue())
                    .count();
        }
        return 0;
    }
    
    private boolean isGoalActive(Goal goal, LocalDate date) {
        // Check if goal is active on the given date
        if (goal.getBeginAndDueDates() != null) {
            LocalDate beginDate = goal.getBeginAndDueDates().getBeginDate();
            LocalDate dueDate = goal.getBeginAndDueDates().getDueDate();
            
            return (beginDate == null || !date.isBefore(beginDate)) &&
                   (dueDate == null || !date.isAfter(dueDate));
        }
        return true; // If no dates, consider it active
    }
}