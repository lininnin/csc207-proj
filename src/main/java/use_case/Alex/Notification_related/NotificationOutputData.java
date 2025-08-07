package use_case.Alex.Notification_related;

/**
 * Output data for the reminder triggering use case.
 * Carries the reminder message to be shown to the user.
 */
public class NotificationOutputData {

    private final String reminderMessage;

    public NotificationOutputData(String reminderMessage) {
        this.reminderMessage = reminderMessage;
    }

    public String getReminderMessage() {
        return reminderMessage;
    }
}