package interface_adapter.Angela.task.overdue;

import use_case.Angela.task.overdue.OverdueTasksInputBoundary;
import use_case.Angela.task.overdue.OverdueTasksInputData;

/**
 * Controller for the overdue tasks feature.
 */
public class OverdueTasksController {
    private final OverdueTasksInputBoundary interactor;
    
    public OverdueTasksController(OverdueTasksInputBoundary interactor) {
        this.interactor = interactor;
    }
    
    /**
     * Executes the overdue tasks use case.
     * 
     * @param daysBack Number of days to look back for overdue tasks
     */
    public void execute(int daysBack) {
        OverdueTasksInputData inputData = new OverdueTasksInputData(daysBack);
        interactor.execute(inputData);
    }
}