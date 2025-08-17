package use_case.Angela.today_so_far;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Output data for the Today So Far use case.
 */
public class TodaySoFarOutputData {
    
    // Goals section
    private final List<GoalProgress> goals;
    
    // Completed Tasks/Events section
    private final List<CompletedItem> completedItems;
    private final int completionRate;
    
    // Wellness section
    private final List<WellnessEntry> wellnessEntries;
    
    public TodaySoFarOutputData(List<GoalProgress> goals,
                                List<CompletedItem> completedItems,
                                int completionRate,
                                List<WellnessEntry> wellnessEntries) {
        this.goals = goals;
        this.completedItems = completedItems;
        this.completionRate = completionRate;
        this.wellnessEntries = wellnessEntries;
    }
    
    public List<GoalProgress> getGoals() {
        return goals;
    }
    
    public List<CompletedItem> getCompletedItems() {
        return completedItems;
    }
    
    public int getCompletionRate() {
        return completionRate;
    }
    
    public List<WellnessEntry> getWellnessEntries() {
        return wellnessEntries;
    }
    
    /**
     * Represents a goal with its progress.
     */
    public static class GoalProgress {
        private final String name;
        private final String period;
        private final String progress;
        
        public GoalProgress(String name, String period, String progress) {
            this.name = name;
            this.period = period;
            this.progress = progress;
        }
        
        public String getName() { return name; }
        public String getPeriod() { return period; }
        public String getProgress() { return progress; }
    }
    
    /**
     * Represents a completed task or event.
     */
    public static class CompletedItem {
        private final String type; // "Task" or "Event"
        private final String name;
        private final String category;
        
        public CompletedItem(String type, String name, String category) {
            this.type = type;
            this.name = name;
            this.category = category;
        }
        
        public String getType() { return type; }
        public String getName() { return name; }
        public String getCategory() { return category; }
    }
    
    /**
     * Represents a wellness log entry.
     */
    public static class WellnessEntry {
        private final String mood;
        private final int stress;
        private final int energy;
        private final int fatigue;
        private final LocalTime time;
        
        public WellnessEntry(String mood, int stress, int energy, int fatigue, LocalTime time) {
            this.mood = mood;
            this.stress = stress;
            this.energy = energy;
            this.fatigue = fatigue;
            this.time = time;
        }
        
        public String getMood() { return mood; }
        public int getStress() { return stress; }
        public int getEnergy() { return energy; }
        public int getFatigue() { return fatigue; }
        public LocalTime getTime() { return time; }
    }
}