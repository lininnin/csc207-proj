package use_case.alex.notification_related;

public interface NotificationOutputBoundary {

    /**
     * Prepares the view for a successful reminder trigger.
     *
     * @param outputData the output data containing reminder message
     */
    void prepareReminderView(NotificationOutputData outputData);
}

