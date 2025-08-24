package use_case.Angela.today_so_far;

import entity.Angela.Task.TaskInterf;
import entity.alex.Event.EventInterf;
import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.Sophia.GoalInterface;
import entity.Category;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for the Today So Far use case.
 * Aggregates data from multiple modules and prepares it for display.
 */
public class TodaySoFarInteractor implements TodaySoFarInputBoundary {
    
    private final TodaySoFarDataAccessInterface dataAccess;
    private final TodaySoFarOutputBoundary outputBoundary;
    private final CategoryReadDataAccessInterface categoryReadDataAccess;
    
    public TodaySoFarInteractor(TodaySoFarDataAccessInterface dataAccess,
                                TodaySoFarOutputBoundary outputBoundary,
                                CategoryReadDataAccessInterface categoryReadDataAccess) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
        this.categoryReadDataAccess = categoryReadDataAccess;
    }
    
    @Override
    public void refreshTodaySoFar() {
        try {
            // Collect Goals data
            List<TodaySoFarOutputData.GoalProgress> goalProgressList = new ArrayList<>();
            List<GoalInterface> activeGoals = dataAccess.getActiveGoals();
            if (activeGoals != null) {
                for (GoalInterface goal : activeGoals) {
                    // Cast to concrete type for method access - this is cross-module compatibility issue
                    GoalInterface concreteGoal = goal;
                    String name = concreteGoal.getGoalInfo().getInfo().getName();
                    String period = formatPeriod(concreteGoal);
                    String progress = formatProgress(concreteGoal);
                    goalProgressList.add(new TodaySoFarOutputData.GoalProgress(name, period, progress));
                }
            }
            
            // Collect Completed Tasks and Events
            List<TodaySoFarOutputData.CompletedItem> completedItems = new ArrayList<>();
            
            // Add completed tasks
            List<TaskInterf> completedTasks = dataAccess.getCompletedTasksForToday();
            if (completedTasks != null) {
                for (TaskInterf task : completedTasks) {
                    String name = task.getInfo().getName();
                    String categoryName = getCategoryName(task.getInfo().getCategory());
                    completedItems.add(new TodaySoFarOutputData.CompletedItem("Task", name, categoryName));
                }
            }
            
            // Add completed events
            List<EventInterf> completedEvents = dataAccess.getCompletedEventsForToday();
            if (completedEvents != null) {
                for (EventInterf event : completedEvents) {
                    String name = event.getInfo().getName();
                    String categoryName = getCategoryName(event.getInfo().getCategory());
                    completedItems.add(new TodaySoFarOutputData.CompletedItem("Event", name, categoryName));
                }
            }
            
            // Calculate completion rate
            int completionRate = dataAccess.getTodayTaskCompletionRate();
            
            // Collect Wellness entries
            List<TodaySoFarOutputData.WellnessEntry> wellnessEntries = new ArrayList<>();
            List<WellnessLogEntryInterf> todaysWellness = dataAccess.getWellnessEntriesForToday();
            if (todaysWellness != null) {
                for (WellnessLogEntryInterf entry : todaysWellness) {
                    String mood = entry.getMoodLabel() != null ? entry.getMoodLabel().toString() : "Unknown";
                    int stress = entry.getStressLevel() != null ? entry.getStressLevel().getValue() : 0;
                    int energy = entry.getEnergyLevel() != null ? entry.getEnergyLevel().getValue() : 0;
                    int fatigue = entry.getFatigueLevel() != null ? entry.getFatigueLevel().getValue() : 0;
                    LocalTime time = entry.getTime() != null ? entry.getTime().toLocalTime() : LocalTime.now();
                    wellnessEntries.add(new TodaySoFarOutputData.WellnessEntry(
                        mood, stress, energy, fatigue, time
                    ));
                }
            }
            
            // Create and present output data
            TodaySoFarOutputData outputData = new TodaySoFarOutputData(
                goalProgressList,
                completedItems,
                completionRate,
                wellnessEntries
            );
            
            outputBoundary.presentTodaySoFar(outputData);
            
        } catch (Exception e) {
            e.printStackTrace();
            outputBoundary.presentError("Failed to load Today So Far data: " + e.getMessage());
        }
    }
    
    private String getCategoryName(String categoryId) {
        if (categoryId == null || categoryId.isEmpty()) {
            return "-";
        }
        Category category = categoryReadDataAccess.getCategoryById(categoryId);
        return (category != null) ? category.getName() : "-";
    }
    
    private String formatPeriod(GoalInterface goal) {
        // Format the goal period based on the goal's time period type
        if (goal.getTimePeriod() != null) {
            switch (goal.getTimePeriod()) {
                case WEEK:
                    return "Weekly";
                case MONTH:
                    return "Monthly";
                default:
                    if (goal.getBeginAndDueDates() != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
                        String start = goal.getBeginAndDueDates().getBeginDate().format(formatter);
                        String end = goal.getBeginAndDueDates().getDueDate().format(formatter);
                        return start + " - " + end;
                    }
            }
        }
        return "Ongoing";
    }
    
    private String formatProgress(GoalInterface goal) {
        // Format goal progress (e.g., "2/3 completed")
        int current = goal.getCurrentProgress();
        int required = goal.getFrequency();
        return current + "/" + required;
    }
}