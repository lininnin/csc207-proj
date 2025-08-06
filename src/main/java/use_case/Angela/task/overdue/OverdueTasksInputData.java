package use_case.Angela.task.overdue;

/**
 * Input data for the overdue tasks use case.
 */
public class OverdueTasksInputData {
    private final int daysBack;
    
    /**
     * Creates input data for retrieving overdue tasks.
     * 
     * @param daysBack Number of days to look back for overdue tasks
     */
    public OverdueTasksInputData(int daysBack) {
        if (daysBack <= 0) {
            throw new IllegalArgumentException("Days back must be positive");
        }
        this.daysBack = daysBack;
    }
    
    /**
     * Gets the number of days to look back.
     * 
     * @return The days back value
     */
    public int getDaysBack() {
        return daysBack;
    }
}