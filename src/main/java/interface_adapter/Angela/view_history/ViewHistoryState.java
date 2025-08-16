package interface_adapter.Angela.view_history;

import entity.Angela.Task.Task;
import entity.info.Info;
import entity.Angela.TodaySoFarSnapshot;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * State class for the view history view model.
 * Immutable state object that holds all the data for displaying history.
 */
public class ViewHistoryState {
    private final LocalDate selectedDate;
    private final List<LocalDate> availableDates;
    private final boolean hasData;
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
    
    // Export status
    private final String exportMessage;
    
    // Default constructor
    public ViewHistoryState() {
        this.selectedDate = null;
        this.availableDates = new ArrayList<>();
        this.hasData = false;
        this.errorMessage = null;
        this.todaysTasks = new ArrayList<>();
        this.completedTasks = new ArrayList<>();
        this.taskCompletionRate = 0;
        this.overdueTasks = new ArrayList<>();
        this.todaysEvents = new ArrayList<>();
        this.goalProgress = new ArrayList<>();
        this.wellnessEntries = new ArrayList<>();
        this.exportMessage = null;
    }
    
    // Copy constructor with modifications
    private ViewHistoryState(Builder builder) {
        this.selectedDate = builder.selectedDate;
        this.availableDates = new ArrayList<>(builder.availableDates);
        this.hasData = builder.hasData;
        this.errorMessage = builder.errorMessage;
        this.todaysTasks = new ArrayList<>(builder.todaysTasks);
        this.completedTasks = new ArrayList<>(builder.completedTasks);
        this.taskCompletionRate = builder.taskCompletionRate;
        this.overdueTasks = new ArrayList<>(builder.overdueTasks);
        this.todaysEvents = new ArrayList<>(builder.todaysEvents);
        this.goalProgress = new ArrayList<>(builder.goalProgress);
        this.wellnessEntries = new ArrayList<>(builder.wellnessEntries);
        this.exportMessage = builder.exportMessage;
    }
    
    // Builder class for creating new states
    public static class Builder {
        private LocalDate selectedDate;
        private List<LocalDate> availableDates = new ArrayList<>();
        private boolean hasData;
        private String errorMessage;
        private List<Task> todaysTasks = new ArrayList<>();
        private List<Task> completedTasks = new ArrayList<>();
        private int taskCompletionRate;
        private List<Task> overdueTasks = new ArrayList<>();
        private List<Info> todaysEvents = new ArrayList<>();
        private List<TodaySoFarSnapshot.GoalProgress> goalProgress = new ArrayList<>();
        private List<WellnessLogEntry> wellnessEntries = new ArrayList<>();
        private String exportMessage;
        
        public Builder(ViewHistoryState copyFrom) {
            this.selectedDate = copyFrom.selectedDate;
            this.availableDates = new ArrayList<>(copyFrom.availableDates);
            this.hasData = copyFrom.hasData;
            this.errorMessage = copyFrom.errorMessage;
            this.todaysTasks = new ArrayList<>(copyFrom.todaysTasks);
            this.completedTasks = new ArrayList<>(copyFrom.completedTasks);
            this.taskCompletionRate = copyFrom.taskCompletionRate;
            this.overdueTasks = new ArrayList<>(copyFrom.overdueTasks);
            this.todaysEvents = new ArrayList<>(copyFrom.todaysEvents);
            this.goalProgress = new ArrayList<>(copyFrom.goalProgress);
            this.wellnessEntries = new ArrayList<>(copyFrom.wellnessEntries);
            this.exportMessage = copyFrom.exportMessage;
        }
        
        public Builder() {}
        
        public Builder selectedDate(LocalDate date) { this.selectedDate = date; return this; }
        public Builder availableDates(List<LocalDate> dates) { this.availableDates = dates; return this; }
        public Builder hasData(boolean hasData) { this.hasData = hasData; return this; }
        public Builder errorMessage(String error) { this.errorMessage = error; return this; }
        public Builder todaysTasks(List<Task> tasks) { this.todaysTasks = tasks; return this; }
        public Builder completedTasks(List<Task> tasks) { this.completedTasks = tasks; return this; }
        public Builder taskCompletionRate(int rate) { this.taskCompletionRate = rate; return this; }
        public Builder overdueTasks(List<Task> tasks) { this.overdueTasks = tasks; return this; }
        public Builder todaysEvents(List<Info> events) { this.todaysEvents = events; return this; }
        public Builder goalProgress(List<TodaySoFarSnapshot.GoalProgress> progress) { this.goalProgress = progress; return this; }
        public Builder wellnessEntries(List<WellnessLogEntry> entries) { this.wellnessEntries = entries; return this; }
        public Builder exportMessage(String message) { this.exportMessage = message; return this; }
        
        public ViewHistoryState build() {
            return new ViewHistoryState(this);
        }
    }
    
    // Getters
    public LocalDate getSelectedDate() { return selectedDate; }
    public List<LocalDate> getAvailableDates() { return new ArrayList<>(availableDates); }
    public boolean hasData() { return hasData; }
    public String getErrorMessage() { return errorMessage; }
    public List<Task> getTodaysTasks() { return new ArrayList<>(todaysTasks); }
    public List<Task> getCompletedTasks() { return new ArrayList<>(completedTasks); }
    public int getTaskCompletionRate() { return taskCompletionRate; }
    public List<Task> getOverdueTasks() { return new ArrayList<>(overdueTasks); }
    public List<Info> getTodaysEvents() { return new ArrayList<>(todaysEvents); }
    public List<TodaySoFarSnapshot.GoalProgress> getGoalProgress() { return new ArrayList<>(goalProgress); }
    public List<WellnessLogEntry> getWellnessEntries() { return new ArrayList<>(wellnessEntries); }
    public String getExportMessage() { return exportMessage; }
}