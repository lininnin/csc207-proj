package use_case.Angela.task.overdue;

import java.time.LocalDate;
import java.util.List;

/**
 * Output data for the overdue tasks use case.
 */
public class OverdueTasksOutputData {
    private final List<OverdueTaskData> overdueTasks;
    private final int totalOverdueTasks;
    
    /**
     * Creates output data for overdue tasks.
     * 
     * @param overdueTasks List of overdue task data
     */
    public OverdueTasksOutputData(List<OverdueTaskData> overdueTasks) {
        this.overdueTasks = overdueTasks;
        this.totalOverdueTasks = overdueTasks.size();
    }
    
    /**
     * Gets the list of overdue tasks.
     * 
     * @return The overdue tasks
     */
    public List<OverdueTaskData> getOverdueTasks() {
        return overdueTasks;
    }
    
    /**
     * Gets the total number of overdue tasks.
     * 
     * @return The total count
     */
    public int getTotalOverdueTasks() {
        return totalOverdueTasks;
    }
    
    /**
     * Data for a single overdue task.
     */
    public static class OverdueTaskData {
        private final String taskId;
        private final String taskName;
        private final String categoryName;
        private final LocalDate dueDate;
        private final int daysOverdue;
        
        public OverdueTaskData(String taskId, String taskName, String categoryName, 
                              LocalDate dueDate, int daysOverdue) {
            this.taskId = taskId;
            this.taskName = taskName;
            this.categoryName = categoryName;
            this.dueDate = dueDate;
            this.daysOverdue = daysOverdue;
        }
        
        public String getTaskId() {
            return taskId;
        }
        
        public String getTaskName() {
            return taskName;
        }
        
        public String getCategoryName() {
            return categoryName;
        }
        
        public LocalDate getDueDate() {
            return dueDate;
        }
        
        public int getDaysOverdue() {
            return daysOverdue;
        }
    }
}