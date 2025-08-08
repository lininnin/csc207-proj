package use_case.alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog;

/**
 * Output data for the EditWellnessLog use case.
 * Represents the result after successfully editing a wellness log.
 */
public class EditWellnessLogOutputData {

    private final String logId;
    private final boolean successMessageVisible;

    /**
     * Constructor.
     *
     * @param logId                   The ID of the log that was edited.
     * @param successMessageVisible  Whether a success message should be shown.
     */
    public EditWellnessLogOutputData(String logId, boolean successMessageVisible) {
        this.logId = logId;
        this.successMessageVisible = successMessageVisible;
    }

    public String getLogId() {
        return logId;
    }

    public boolean isSuccessMessageVisible() {
        return successMessageVisible;
    }
}

