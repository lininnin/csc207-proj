package use_case.alex.Settings_related.edit_notificationTime;

import java.time.LocalTime;

/**
 * Data access interface for editing notification times.
 * Defines methods to persist updated reminder times.
 */
public interface EditNotificationTimeDataAccessInterf {

    /**
     * Updates all three reminder times in the data store.
     *
     * @param reminder1 LocalTime for the first reminder
     * @param reminder2 LocalTime for the second reminder
     * @param reminder3 LocalTime for the third reminder
     */
    void updateNotificationTimes(LocalTime reminder1, LocalTime reminder2, LocalTime reminder3);
}
