package use_case.Alex.Settings_related.edit_notificationTime;

/**
 * Output data for the EditNotificationTime use case.
 * Contains the updated reminder times and a flag indicating failure.
 */
public class EditNotificationTimeOutputData {

    private final String reminder1;
    private final String reminder2;
    private final String reminder3;
    private final boolean useCaseFailed;

    /**
     * Constructs output data with updated reminder times.
     *
     * @param reminder1     Updated first reminder time (e.g., "08:00")
     * @param reminder2     Updated second reminder time (e.g., "12:00")
     * @param reminder3     Updated third reminder time (e.g., "20:00")
     * @param useCaseFailed Whether the use case failed
     */
    public EditNotificationTimeOutputData(String reminder1, String reminder2, String reminder3, boolean useCaseFailed) {
        this.reminder1 = reminder1;
        this.reminder2 = reminder2;
        this.reminder3 = reminder3;
        this.useCaseFailed = useCaseFailed;
    }

    public String getReminder1() {
        return reminder1;
    }

    public String getReminder2() {
        return reminder2;
    }

    public String getReminder3() {
        return reminder3;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}

