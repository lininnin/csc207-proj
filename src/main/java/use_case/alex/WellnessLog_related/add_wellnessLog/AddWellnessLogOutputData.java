package use_case.alex.WellnessLog_related.add_wellnessLog;

import entity.Alex.WellnessLogEntry.WellnessLogEntry;

/**
 * Output data for AddWellnessLog use case.
 * It carries the saved wellness log entry back to the presenter.
 */
public class AddWellnessLogOutputData {

    private final WellnessLogEntry savedEntry;
    private final boolean success;
    private final String message;

    public AddWellnessLogOutputData(WellnessLogEntry savedEntry, boolean success, String message) {
        this.savedEntry = savedEntry;
        this.success = success;
        this.message = message;
    }

    public WellnessLogEntry getSavedEntry() {
        return savedEntry;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}

