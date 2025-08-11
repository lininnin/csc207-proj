package interface_adapter.alex.Notification_related;

public class NotificationState {

    private boolean shouldShowReminder = false;
    private String reminderMessage = null;

    // ---------------- Getters ----------------

    public boolean shouldShowReminder() {
        return shouldShowReminder;
    }

    public String getReminderMessage() {
        return reminderMessage;
    }

    // ---------------- Setters ----------------

    public void setShouldShowReminder(boolean shouldShowReminder) {
        this.shouldShowReminder = shouldShowReminder;
    }

    public void setReminderMessage(String reminderMessage) {
        this.reminderMessage = reminderMessage;
    }
}

