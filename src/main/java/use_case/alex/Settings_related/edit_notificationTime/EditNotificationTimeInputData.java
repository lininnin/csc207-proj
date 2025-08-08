package use_case.alex.Settings_related.edit_notificationTime;

/**
 * Input data for the EditNotificationTime use case.
 * Carries the new values of the three daily reminder times (as strings).
 */
public class EditNotificationTimeInputData {

    private final String reminder1;
    private final String reminder2;
    private final String reminder3;

    /**
     * Constructs input data with the given reminder times.
     *
     * @param reminder1 The first reminder time (e.g., "08:00")
     * @param reminder2 The second reminder time (e.g., "12:00")
     * @param reminder3 The third reminder time (e.g., "20:00")
     */
    public EditNotificationTimeInputData(String reminder1, String reminder2, String reminder3) {
        this.reminder1 = reminder1;
        this.reminder2 = reminder2;
        this.reminder3 = reminder3;
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
}
