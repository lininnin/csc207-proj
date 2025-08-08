package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog;

import use_case.alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog.DeleteWellnessLogInputBoundary;
import use_case.alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog.DeleteWellnessLogInputData;

/**
 * Controller for the DeleteWellnessLog use case.
 * Receives user input (e.g., log ID) and triggers the use case execution.
 */
public class DeleteWellnessLogController {

    private final DeleteWellnessLogInputBoundary interactor;

    public DeleteWellnessLogController(DeleteWellnessLogInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Initiates the delete process for the given log ID.
     *
     * @param logId the ID of the wellness log entry to be deleted
     */
    public void delete(String logId) {
        DeleteWellnessLogInputData inputData = new DeleteWellnessLogInputData(logId);
        interactor.execute(inputData);
    }
}

