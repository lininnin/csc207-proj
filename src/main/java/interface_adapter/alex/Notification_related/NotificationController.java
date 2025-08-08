package interface_adapter.alex.Notification_related;

import use_case.alex.notification_related.NotificationInputBoundary;
import use_case.alex.notification_related.NotificationInputData;

import java.time.LocalTime;

/**
 * Controller for the notification use case.
 * Converts the trigger event into use case input and delegates execution.
 */
public class NotificationController {

    private final NotificationInputBoundary interactor;

    public NotificationController(NotificationInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Triggers the reminder check using the current system time.
     */
    public void execute() {
        LocalTime now = LocalTime.now();
        NotificationInputData inputData = new NotificationInputData(now);
        interactor.execute(inputData);
    }
}

