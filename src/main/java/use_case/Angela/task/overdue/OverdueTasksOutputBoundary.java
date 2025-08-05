package use_case.Angela.task.overdue;

/**
 * Output boundary for the overdue tasks use case.
 */
public interface OverdueTasksOutputBoundary {
    /**
     * Prepares the view with overdue tasks data.
     * 
     * @param outputData The output data containing overdue tasks
     */
    void presentOverdueTasks(OverdueTasksOutputData outputData);
    
    /**
     * Prepares the view for an error.
     * 
     * @param error The error message
     */
    void prepareFailView(String error);
}