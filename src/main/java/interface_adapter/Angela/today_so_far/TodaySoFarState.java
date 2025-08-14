package interface_adapter.Angela.today_so_far;

import use_case.Angela.today_so_far.TodaySoFarOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * State for the Today So Far view model.
 */
public class TodaySoFarState {
    
    private List<TodaySoFarOutputData.GoalProgress> goals = new ArrayList<>();
    private List<TodaySoFarOutputData.CompletedItem> completedItems = new ArrayList<>();
    private int completionRate = 0;
    private List<TodaySoFarOutputData.WellnessEntry> wellnessEntries = new ArrayList<>();
    private String error = null;
    
    public List<TodaySoFarOutputData.GoalProgress> getGoals() {
        return goals;
    }
    
    public void setGoals(List<TodaySoFarOutputData.GoalProgress> goals) {
        this.goals = goals;
    }
    
    public List<TodaySoFarOutputData.CompletedItem> getCompletedItems() {
        return completedItems;
    }
    
    public void setCompletedItems(List<TodaySoFarOutputData.CompletedItem> completedItems) {
        this.completedItems = completedItems;
    }
    
    public int getCompletionRate() {
        return completionRate;
    }
    
    public void setCompletionRate(int completionRate) {
        this.completionRate = completionRate;
    }
    
    public List<TodaySoFarOutputData.WellnessEntry> getWellnessEntries() {
        return wellnessEntries;
    }
    
    public void setWellnessEntries(List<TodaySoFarOutputData.WellnessEntry> wellnessEntries) {
        this.wellnessEntries = wellnessEntries;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
}