package use_case.alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog;

/**
 * Output data for the DeleteWellnessLog use case.
 * Carries the ID of the deleted wellness log and a success flag.
 */
public class DeleteWellnessLogOutputData {

    private final String deletedLogId;
    private final boolean success;

    public DeleteWellnessLogOutputData(String deletedLogId, boolean success) {
        this.deletedLogId = deletedLogId;
        this.success = success;
    }

    public String getDeletedLogId() {
        return deletedLogId;
    }

    public boolean isSuccess() {
        return success;
    }
}

