package interface_adapter.Angela.task.overdue;

import use_case.Angela.task.overdue.OverdueTasksOutputData.OverdueTaskData;
import java.util.ArrayList;
import java.util.List;

/**
 * State for overdue tasks view model.
 */
public class OverdueTasksState {
    private List<OverdueTaskData> overdueTasks = new ArrayList<>();
    private int totalOverdueTasks = 0;
    private String error;
    
    public List<OverdueTaskData> getOverdueTasks() {
        return overdueTasks;
    }
    
    public void setOverdueTasks(List<OverdueTaskData> overdueTasks) {
        this.overdueTasks = overdueTasks;
    }
    
    public int getTotalOverdueTasks() {
        return totalOverdueTasks;
    }
    
    public void setTotalOverdueTasks(int totalOverdueTasks) {
        this.totalOverdueTasks = totalOverdueTasks;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
}