package use_case.alex.notification_related;

import java.time.LocalTime;

/**
 * Input data for the reminder triggering use case.
 * Can include current system time or other context if needed.
 */
public class NotificationInputData {

    private final LocalTime currentTime;

    public NotificationInputData(LocalTime currentTime) {
        this.currentTime = currentTime;
    }

    public LocalTime getCurrentTime() {
        return currentTime;
    }
}

