package entity.Angela;

import entity.Angela.Task.Task;
import entity.info.Info;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a historical snapshot of "Today So Far" data for a specific date.
 * This entity stores the complete state of a day including tasks, events, goals, and wellness data.
 */
public class TodaySoFarSnapshot {
    private final String id;
    private final LocalDate date;
    private final LocalDateTime createdAt;
    
    // Task data
    private final List<Task> todaysTasks;
    private final List<Task> completedTasks;
    private final int taskCompletionRate;
    private final List<Task> overdueTasks;
    
    // Event data
    private final List<Info> todaysEvents;
    
    // Goal data
    private final List<GoalProgress> goalProgress;
    
    // Wellness data
    private final List<WellnessLogEntry> wellnessEntries;
    
    /**
     * Creates a new snapshot of today's data.
     */
    public TodaySoFarSnapshot(LocalDate date,
                             List<Task> todaysTasks,
                             List<Task> completedTasks,
                             int taskCompletionRate,
                             List<Task> overdueTasks,
                             List<Info> todaysEvents,
                             List<GoalProgress> goalProgress,
                             List<WellnessLogEntry> wellnessEntries) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.createdAt = LocalDateTime.now();
        this.todaysTasks = new ArrayList<>(todaysTasks);
        this.completedTasks = new ArrayList<>(completedTasks);
        this.taskCompletionRate = taskCompletionRate;
        this.overdueTasks = new ArrayList<>(overdueTasks);
        this.todaysEvents = new ArrayList<>(todaysEvents);
        this.goalProgress = new ArrayList<>(goalProgress);
        this.wellnessEntries = new ArrayList<>(wellnessEntries);
    }
    
    /**
     * Represents a goal with its progress at this point in time.
     */
    public static class GoalProgress {
        private final String goalId;
        private final String goalName;
        private final String period;
        private final int current;
        private final int target;
        
        public GoalProgress(String goalId, String goalName, String period, int current, int target) {
            this.goalId = goalId;
            this.goalName = goalName;
            this.period = period;
            this.current = current;
            this.target = target;
        }
        
        public String getGoalId() { return goalId; }
        public String getGoalName() { return goalName; }
        public String getPeriod() { return period; }
        public int getCurrent() { return current; }
        public int getTarget() { return target; }
        public String getProgressString() { return current + "/" + target; }
    }
    
    // Getters
    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<Task> getTodaysTasks() { return new ArrayList<>(todaysTasks); }
    public List<Task> getCompletedTasks() { return new ArrayList<>(completedTasks); }
    public int getTaskCompletionRate() { return taskCompletionRate; }
    public List<Task> getOverdueTasks() { return new ArrayList<>(overdueTasks); }
    public List<Info> getTodaysEvents() { return new ArrayList<>(todaysEvents); }
    public List<GoalProgress> getGoalProgress() { return new ArrayList<>(goalProgress); }
    public List<WellnessLogEntry> getWellnessEntries() { return new ArrayList<>(wellnessEntries); }
}