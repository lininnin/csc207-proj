package data_access;

import entity.Angela.Task.Task;
import entity.Alex.Event.Event;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Sophia.Goal;
import use_case.Angela.today_so_far.TodaySoFarDataAccessInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In-memory implementation that aggregates data from existing data access objects.
 */
public class InMemoryTodaySoFarDataAccess implements TodaySoFarDataAccessInterface {
    
    private final InMemoryTaskGateway taskGateway;
    private final TodaysEventDataAccessObject eventDataAccess;
    private final WellnessLogDataAccessObject wellnessDataAccess;
    private final GoalDataAccessObject goalDataAccess;
    
    public InMemoryTodaySoFarDataAccess(InMemoryTaskGateway taskGateway,
                                        TodaysEventDataAccessObject eventDataAccess,
                                        WellnessLogDataAccessObject wellnessDataAccess,
                                        GoalDataAccessObject goalDataAccess) {
        this.taskGateway = taskGateway;
        this.eventDataAccess = eventDataAccess;
        this.wellnessDataAccess = wellnessDataAccess;
        this.goalDataAccess = goalDataAccess;
    }
    
    // Constructor with optional parameters for gradual integration
    public InMemoryTodaySoFarDataAccess(InMemoryTaskGateway taskGateway) {
        this.taskGateway = taskGateway;
        this.eventDataAccess = null;
        this.wellnessDataAccess = null;
        this.goalDataAccess = null;
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
                // Get today's events and filter for completed ones
                return eventDataAccess.getTodaysEvents().stream()
                        .filter(event -> isEventCompleted(event))
                        .collect(Collectors.toList());
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
                LocalDate today = LocalDate.now();
                return wellnessDataAccess.getWellnessEntriesForDate(today);
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }
    
    @Override
    public List<Goal> getActiveGoals() {
        if (goalDataAccess != null) {
            try {
                LocalDate today = LocalDate.now();
                return goalDataAccess.getActiveGoals().stream()
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
            return taskGateway.getTodaysTasks().size();
        }
        return 0;
    }
    
    @Override
    public int getCompletedTasksCountForToday() {
        if (taskGateway != null) {
            return (int) taskGateway.getTodaysTasks().stream()
                    .filter(Task::isCompleted)
                    .count();
        }
        return 0;
    }
    
    private boolean isEventCompleted(Event event) {
        // Check if event's time has passed or if it has a completed flag
        // This is a placeholder - actual implementation depends on Event entity
        LocalDate today = LocalDate.now();
        if (event.getBeginAndDueDates() != null && 
            event.getBeginAndDueDates().getDueDate() != null) {
            return event.getBeginAndDueDates().getDueDate().isBefore(today) ||
                   event.getBeginAndDueDates().getDueDate().isEqual(today);
        }
        return false;
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
    
    // Stub classes for when actual DAOs are not available
    private static class WellnessLogDataAccessObject {
        public List<WellnessLogEntry> getWellnessEntriesForDate(LocalDate date) {
            return new ArrayList<>();
        }
    }
    
    private static class GoalDataAccessObject {
        public List<Goal> getActiveGoals() {
            return new ArrayList<>();
        }
    }
}