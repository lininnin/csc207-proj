package use_case.Angela.task.overdue;

/**
 * Input boundary for the overdue tasks use case.
 */
public interface OverdueTasksInputBoundary {
    /**
     * Executes the overdue tasks use case.
     * 
     * @param inputData The input data containing parameters
     */
    void execute(OverdueTasksInputData inputData);
}