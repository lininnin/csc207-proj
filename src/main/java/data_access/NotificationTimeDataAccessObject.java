package data_access;

import entity.Alex.NotificationTime.NotificationTime;
import use_case.Alex.Notification_related.NotificationDataAccessObjectInterf;
import use_case.Alex.Settings_related.edit_notificationTime.EditNotificationTimeDataAccessInterf;

import java.time.LocalTime;

/**
 * In-memory implementation of EditNotificationTimeDataAccessInterf.
 * Stores and updates a single NotificationTime instance.
 */
public class NotificationTimeDataAccessObject implements EditNotificationTimeDataAccessInterf,
        NotificationDataAccessObjectInterf {

    private final NotificationTime notificationTime;

    /**
     * Constructs the DAO with default notification times.
     */
    public NotificationTimeDataAccessObject() {
        this.notificationTime = new NotificationTime();  // 08:00, 12:00, 20:00 by default
    }

    /**
     * Updates all three reminder times in the NotificationTime entity.
     *
     * @param reminder1 LocalTime for the first reminder
     * @param reminder2 LocalTime for the second reminder
     * @param reminder3 LocalTime for the third reminder
     */
    @Override
    public void updateNotificationTimes(LocalTime reminder1, LocalTime reminder2, LocalTime reminder3) {
        notificationTime.setReminder1(reminder1);
        notificationTime.setReminder2(reminder2);
        notificationTime.setReminder3(reminder3);
    }

    /**
     * Returns the current NotificationTime object.
     * Useful for reading the current values into the UI or other use cases.
     *
     * @return NotificationTime
     */
    public NotificationTime getNotificationTime() {
        return notificationTime;
    }

    @Override
    public LocalTime getReminder1() {
        return notificationTime.getReminder1();
    }

    @Override
    public LocalTime getReminder2() {
        return notificationTime.getReminder2();
    }

    @Override
    public LocalTime getReminder3() {
        return notificationTime.getReminder3();
    }
}

