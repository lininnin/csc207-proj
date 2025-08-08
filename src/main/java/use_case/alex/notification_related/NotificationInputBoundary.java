package use_case.alex.notification_related;

public interface NotificationInputBoundary {
    /**
     * Executes the reminder use case, using the current time
     * provided in the input data.
     *
     * @param inputData the input data containing current system time
     */
    void execute(NotificationInputData inputData);
}

