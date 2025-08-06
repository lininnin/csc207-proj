package use_case.Angela.task.edit_today;

/**
 * Input boundary for the Edit Today Task use case.
 * Defines the interface for executing the use case.
 */
public interface EditTodayTaskInputBoundary {
    /**
     * Executes the edit today task use case.
     * 
     * @param inputData The input data containing task ID and new values
     */
    void execute(EditTodayTaskInputData inputData);
}