package use_case.alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog;

/**
 * Input data for the DeleteWellnessLog use case.
 * Carries the identifier of the wellness log entry to be deleted.
 */
public class DeleteWellnessLogInputData {

    private final String logId;

    public DeleteWellnessLogInputData(String logId) {
        this.logId = logId;
    }

    public String getLogId() {
        return logId;
    }
}

