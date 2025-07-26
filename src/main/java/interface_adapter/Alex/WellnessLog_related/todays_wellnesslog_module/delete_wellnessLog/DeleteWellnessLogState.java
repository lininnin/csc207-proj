package interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog;

/**
 * State class for deleting a wellness log entry.
 * Currently acts as a placeholder; can be extended to include error messages, deleted entry ID, etc.
 */
public class DeleteWellnessLogState {

    private String deletedLogId = "";
    private String deleteError = "";

    public String getDeletedLogId() {
        return deletedLogId;
    }

    public void setDeletedLogId(String deletedLogId) {
        this.deletedLogId = deletedLogId;
    }

    public String getDeleteError() {
        return deleteError;
    }

    public void setDeleteError(String deleteError) {
        this.deleteError = deleteError;
    }
}

