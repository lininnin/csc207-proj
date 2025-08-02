package use_case.Alex.Notification_related;

import java.time.LocalTime;

/**
 * DAO interface for retrieving current notification reminder times.
 * Used by the reminder triggering use case.
 */
public interface NotificationDataAccessObjectInterf {

    /**
     * @return Time for the first daily reminder (e.g., 08:00)
     */
    LocalTime getReminder1();

    /**
     * @return Time for the second daily reminder (e.g., 12:00)
     */
    LocalTime getReminder2();

    /**
     * @return Time for the third daily reminder (e.g., 20:00)
     */
    LocalTime getReminder3();
}
