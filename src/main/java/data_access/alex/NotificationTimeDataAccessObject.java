package data_access.alex;

import entity.alex.NotificationTime.NotificationTimeFactoryInterf;
import entity.alex.NotificationTime.NotificationTimeInterf;
import use_case.alex.notification_related.NotificationDataAccessObjectInterf;
import use_case.alex.Settings_related.edit_notificationTime.EditNotificationTimeDataAccessInterf;

import java.time.LocalTime;

/**
 * In-memory implementation of EditNotificationTimeDataAccessInterf and
 * NotificationDataAccessObjectInterf.
 * <p>
 * This class manages a NotificationTime entity created via a factory.
 */
public class NotificationTimeDataAccessObject implements
        EditNotificationTimeDataAccessInterf,
        NotificationDataAccessObjectInterf {

    /**
     * The NotificationTime entity managed by this DAO.
     */
    private final NotificationTimeInterf notificationTime;

    /**
     * Constructs the DAO using a factory to create NotificationTimeInterf.
     *
     * @param factory the factory used to create a default NotificationTime entity
     */
    public NotificationTimeDataAccessObject(final NotificationTimeFactoryInterf factory) {
        this.notificationTime = factory.createDefault(); // 08:00, 12:00, 20:00
    }

    /**
     * Updates all three reminder times in the NotificationTime entity.
     *
     * @param reminder1 the new time for reminder 1
     * @param reminder2 the new time for reminder 2
     * @param reminder3 the new time for reminder 3
     */
    @Override
    public void updateNotificationTimes(final LocalTime reminder1,
                                        final LocalTime reminder2,
                                        final LocalTime reminder3) {
        notificationTime.setReminder1(reminder1);
        notificationTime.setReminder2(reminder2);
        notificationTime.setReminder3(reminder3);
    }

    /**
     * Returns the current NotificationTime entity (interface form).
     *
     * @return the NotificationTime entity
     */
    public NotificationTimeInterf getNotificationTime() {
        return notificationTime;
    }

    /**
     * Returns the first reminder time.
     *
     * @return reminder1 time as LocalTime
     */
    @Override
    public LocalTime getReminder1() {
        return notificationTime.getReminder1();
    }

    /**
     * Returns the second reminder time.
     *
     * @return reminder2 time as LocalTime
     */
    @Override
    public LocalTime getReminder2() {
        return notificationTime.getReminder2();
    }

    /**
     * Returns the third reminder time.
     *
     * @return reminder3 time as LocalTime
     */
    @Override
    public LocalTime getReminder3() {
        return notificationTime.getReminder3();
    }
}
