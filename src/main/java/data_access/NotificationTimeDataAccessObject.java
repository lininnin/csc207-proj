package data_access;

import entity.Alex.NotificationTime.NotificationTimeFactoryInterf;
import entity.Alex.NotificationTime.NotificationTimeInterf;
import use_case.alex.Notification_related.NotificationDataAccessObjectInterf;
import use_case.alex.Settings_related.edit_notificationTime.EditNotificationTimeDataAccessInterf;

import java.time.LocalTime;

/**
 * In-memory implementation of EditNotificationTimeDataAccessInterf.
 * Now decoupled from NotificationTime concrete class via DIP.
 */
public class NotificationTimeDataAccessObject implements
        EditNotificationTimeDataAccessInterf,
        NotificationDataAccessObjectInterf {

    private final NotificationTimeInterf notificationTime;

    /**
     * Constructs the DAO using a factory to create NotificationTimeInterf.
     *
     * @param factory The factory used to create a NotificationTime entity
     */
    public NotificationTimeDataAccessObject(NotificationTimeFactoryInterf factory) {
        this.notificationTime = factory.createDefault(); // 08:00, 12:00, 20:00
    }

    /**
     * Updates all three reminder times in the NotificationTime entity.
     */
    @Override
    public void updateNotificationTimes(LocalTime reminder1, LocalTime reminder2, LocalTime reminder3) {
        notificationTime.setReminder1(reminder1);
        notificationTime.setReminder2(reminder2);
        notificationTime.setReminder3(reminder3);
    }

    /**
     * Returns the current NotificationTime entity (interface form).
     */
    public NotificationTimeInterf getNotificationTime() {
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
