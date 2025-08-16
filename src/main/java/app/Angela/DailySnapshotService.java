package app.Angela;

import entity.Angela.TodaySoFarSnapshot;
import entity.Angela.Task.Task;
import entity.info.Info;
import entity.Sophia.Goal;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import use_case.Angela.view_history.ViewHistoryDataAccessInterface;
import app.SharedDataAccess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service that automatically creates and saves daily snapshots of "Today So Far" data.
 * Runs at the end of each day to preserve the historical record.
 */
public class DailySnapshotService {
    private final ViewHistoryDataAccessInterface historyDataAccess;
    private final SharedDataAccess sharedDataAccess;
    private Timer timer;
    private static DailySnapshotService instance;
    
    private DailySnapshotService(ViewHistoryDataAccessInterface historyDataAccess,
                                 SharedDataAccess sharedDataAccess) {
        this.historyDataAccess = historyDataAccess;
        this.sharedDataAccess = sharedDataAccess;
    }
    
    /**
     * Gets or creates the singleton instance of the snapshot service.
     */
    public static DailySnapshotService getInstance() {
        if (instance == null) {
            instance = new DailySnapshotService(
                HistoryPageBuilder.getHistoryDataAccess(),
                SharedDataAccess.getInstance()
            );
        }
        return instance;
    }
    
    /**
     * Starts the automatic snapshot service.
     * Schedules a task to run at 11:59 PM each day.
     */
    public void startAutomaticSnapshots() {
        if (timer != null) {
            timer.cancel();
        }
        
        timer = new Timer(true); // daemon thread
        
        // Calculate time until 11:59 PM
        LocalTime targetTime = LocalTime.of(23, 59, 0);
        LocalTime now = LocalTime.now();
        long initialDelay;
        
        if (now.isBefore(targetTime)) {
            initialDelay = TimeUnit.SECONDS.toMillis(
                targetTime.toSecondOfDay() - now.toSecondOfDay());
        } else {
            // Schedule for tomorrow
            initialDelay = TimeUnit.SECONDS.toMillis(
                86400 - now.toSecondOfDay() + targetTime.toSecondOfDay());
        }
        
        // Schedule the task to run daily
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                createAndSaveSnapshot();
            }
        }, initialDelay, TimeUnit.DAYS.toMillis(1));
    }
    
    /**
     * Stops the automatic snapshot service.
     */
    public void stopAutomaticSnapshots() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    
    /**
     * Creates and saves a snapshot of the current day's data.
     * Can be called manually or automatically.
     */
    public void createAndSaveSnapshot() {
        LocalDate today = LocalDate.now();
        
        // Don't overwrite existing snapshot for today
        if (historyDataAccess.hasSnapshot(today)) {
            System.out.println("Snapshot already exists for " + today);
            return;
        }
        
        // Gather today's tasks
        List<Task> todaysTasks = sharedDataAccess.getTaskGateway().getTodaysTasks();
        List<Task> completedTasks = todaysTasks.stream()
            .filter(Task::isCompleted)
            .collect(Collectors.toList());
        
        // Calculate completion rate
        int completionRate = 0;
        if (!todaysTasks.isEmpty()) {
            completionRate = (completedTasks.size() * 100) / todaysTasks.size();
        }
        
        // Get overdue tasks
        List<Task> overdueTasks = todaysTasks.stream()
            .filter(task -> task.getDates() != null && 
                           task.getDates().getDueDate() != null &&
                           task.getDates().getDueDate().isBefore(today) &&
                           !task.isCompleted())
            .collect(Collectors.toList());
        
        // Get today's events
        List<Info> todaysEvents = new ArrayList<>();
        // TODO: Convert EventInterf to Info or change snapshot to use EventInterf
        
        // Get current goals with progress
        List<TodaySoFarSnapshot.GoalProgress> goalProgress = new ArrayList<>();
        List<Goal> currentGoals = sharedDataAccess.getGoalRepository().getCurrentGoals();
        
        for (Goal goal : currentGoals) {
            // Calculate progress based on completed tasks that match the target task
            int current = goal.getCurrentProgress();
            String targetTaskId = goal.getGoalInfo().getTargetTaskInfo().getId();
            
            String period = goal.getTimePeriod() != null ? goal.getTimePeriod().toString() : "Unknown";
            int target = goal.getFrequency();
            
            TodaySoFarSnapshot.GoalProgress progress = new TodaySoFarSnapshot.GoalProgress(
                goal.getGoalInfo().getInfo().getId(),
                goal.getGoalInfo().getInfo().getName(),
                period,
                current,
                target
            );
            goalProgress.add(progress);
        }
        
        // Get wellness entries for today
        List<WellnessLogEntry> wellnessEntries = new ArrayList<>();
        // TODO: Convert WellnessLogEntryInterf to WellnessLogEntry
        
        // Create and save the snapshot
        TodaySoFarSnapshot snapshot = new TodaySoFarSnapshot(
            today,
            todaysTasks,
            completedTasks,
            completionRate,
            overdueTasks,
            todaysEvents,
            goalProgress,
            wellnessEntries
        );
        
        boolean saved = historyDataAccess.saveSnapshot(snapshot);
        if (saved) {
            System.out.println("Successfully saved snapshot for " + today);
            
            // Clean up old snapshots (keep last 30 days)
            int deleted = historyDataAccess.cleanupOldSnapshots(30);
            if (deleted > 0) {
                System.out.println("Cleaned up " + deleted + " old snapshots");
            }
        } else {
            System.err.println("Failed to save snapshot for " + today);
        }
    }
    
    /**
     * Manually triggers a snapshot for the current day.
     * Useful for testing or manual saves.
     */
    public void saveCurrentDaySnapshot() {
        createAndSaveSnapshot();
    }
}