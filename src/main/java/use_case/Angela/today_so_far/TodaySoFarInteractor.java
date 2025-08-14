package use_case.Angela.today_so_far;

import entity.Angela.Task.Task;
import entity.Alex.Event.EventInterf;
import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.Sophia.Goal;
import entity.Category;
import use_case.Angela.category.CategoryGateway;

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
    private final CategoryGateway categoryGateway;
    
    public TodaySoFarInteractor(TodaySoFarDataAccessInterface dataAccess,
                                TodaySoFarOutputBoundary outputBoundary,
                                CategoryGateway categoryGateway) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
        this.categoryGateway = categoryGateway;
    }
    
    @Override
    public void refreshTodaySoFar() {
        System.out.println("DEBUG: TodaySoFarInteractor.refreshTodaySoFar() called");
        try {
            // Collect Goals data
            List<TodaySoFarOutputData.GoalProgress> goalProgressList = new ArrayList<>();
            List<Goal> activeGoals = dataAccess.getActiveGoals();
            System.out.println("DEBUG: Active goals count: " + (activeGoals != null ? activeGoals.size() : 0));
            if (activeGoals != null) {
                for (Goal goal : activeGoals) {
                    String name = goal.getGoalInfo().getInfo().getName();
                    String period = formatPeriod(goal);
                    String progress = formatProgress(goal);
                    System.out.println("DEBUG: Adding goal to Today So Far - Name: " + name + 
                                      ", Period: " + period + ", Progress: " + progress);
                    goalProgressList.add(new TodaySoFarOutputData.GoalProgress(name, period, progress));
                }
            }
            
            // Collect Completed Tasks and Events
            List<TodaySoFarOutputData.CompletedItem> completedItems = new ArrayList<>();
            
            // Add completed tasks
            List<Task> completedTasks = dataAccess.getCompletedTasksForToday();
            System.out.println("DEBUG: Completed tasks count: " + (completedTasks != null ? completedTasks.size() : 0));
            if (completedTasks != null) {
                for (Task task : completedTasks) {
                    String name = task.getInfo().getName();
                    String categoryName = getCategoryName(task.getInfo().getCategory());
                    completedItems.add(new TodaySoFarOutputData.CompletedItem("Task", name, categoryName));
                }
            }
            
            // Add completed events
            List<EventInterf> completedEvents = dataAccess.getCompletedEventsForToday();
            System.out.println("DEBUG: Completed events count: " + (completedEvents != null ? completedEvents.size() : 0));
            if (completedEvents != null) {
                for (EventInterf event : completedEvents) {
                    String name = event.getInfo().getName();
                    String categoryName = getCategoryName(event.getInfo().getCategory());
                    completedItems.add(new TodaySoFarOutputData.CompletedItem("Event", name, categoryName));
                    System.out.println("DEBUG: Added event to completed items: " + name);
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
            
            System.out.println("DEBUG: Presenting output data to boundary");
            outputBoundary.presentTodaySoFar(outputData);
            
        } catch (Exception e) {
            System.out.println("DEBUG: Error in refreshTodaySoFar: " + e.getMessage());
            e.printStackTrace();
            outputBoundary.presentError("Failed to load Today So Far data: " + e.getMessage());
        }
    }
    
    private String getCategoryName(String categoryId) {
        if (categoryId == null || categoryId.isEmpty()) {
            return "-";
        }
        Category category = categoryGateway.getCategoryById(categoryId);
        return (category != null) ? category.getName() : "-";
    }
    
    private String formatPeriod(Goal goal) {
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
    
    private String formatProgress(Goal goal) {
        // Format goal progress (e.g., "2/3 completed")
        int current = goal.getCurrentProgress();
        int required = goal.getFrequency();
        return current + "/" + required;
    }
}