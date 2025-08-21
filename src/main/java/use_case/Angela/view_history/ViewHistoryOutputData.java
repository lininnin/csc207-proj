package use_case.Angela.view_history;

import entity.Angela.Task.Task;
import entity.info.Info;
import entity.Angela.TodaySoFarSnapshot;
import entity.alex.WellnessLogEntry.WellnessLogEntry;

import java.time.LocalDate;
import java.util.List;

/**
 * Output data for the view history use case.
 * Contains all the data needed to display a historical day's information.
 */
public class ViewHistoryOutputData {
    private final LocalDate date;
    private final boolean dataExists;
    private final String errorMessage;
    
    // Task data
    private final List<Task> todaysTasks;
    private final List<Task> completedTasks;
    private final int taskCompletionRate;
    private final List<Task> overdueTasks;
    
    // Event data  
    private final List<Info> todaysEvents;
    
    // Goal data
    private final List<TodaySoFarSnapshot.GoalProgress> goalProgress;
    
    // Wellness data
    private final List<WellnessLogEntry> wellnessEntries;
    
    // Success constructor with data
    public ViewHistoryOutputData(TodaySoFarSnapshot snapshot) {
        this.date = snapshot.getDate();
        this.dataExists = true;
        this.errorMessage = null;
        this.todaysTasks = snapshot.getTodaysTasks();
        this.completedTasks = snapshot.getCompletedTasks();
        this.taskCompletionRate = snapshot.getTaskCompletionRate();
        this.overdueTasks = snapshot.getOverdueTasks();
        this.todaysEvents = snapshot.getTodaysEvents();
        this.goalProgress = snapshot.getGoalProgress();
        this.wellnessEntries = snapshot.getWellnessEntries();
    }
    
    // Failure constructor
    public ViewHistoryOutputData(LocalDate date, String errorMessage) {
        this.date = date;
        this.dataExists = false;
        this.errorMessage = errorMessage;
        this.todaysTasks = List.of();
        this.completedTasks = List.of();
        this.taskCompletionRate = 0;
        this.overdueTasks = List.of();
        this.todaysEvents = List.of();
        this.goalProgress = List.of();
        this.wellnessEntries = List.of();
    }
    
    // Getters
    public LocalDate getDate() { return date; }
    public boolean isDataExists() { return dataExists; }
    public String getErrorMessage() { return errorMessage; }
    public List<Task> getTodaysTasks() { return todaysTasks; }
    public List<Task> getCompletedTasks() { return completedTasks; }
    public int getTaskCompletionRate() { return taskCompletionRate; }
    public List<Task> getOverdueTasks() { return overdueTasks; }
    public List<Info> getTodaysEvents() { return todaysEvents; }
    public List<TodaySoFarSnapshot.GoalProgress> getGoalProgress() { return goalProgress; }
    public List<WellnessLogEntry> getWellnessEntries() { return wellnessEntries; }
}