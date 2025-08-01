package entity.Alex.NotificationTime;

import java.time.LocalTime;

/**
 * Concrete factory for creating NotificationTime instances.
 */
public class NotificationTimeFactory implements NotificationTimeFactoryInterf {

    /**
     * Creates a NotificationTime with default times: 08:00, 12:00, 20:00.
     *
     * @return a new NotificationTime instance
     */
    @Override
    public NotificationTimeInterf createDefault() {
        return new NotificationTime();
    }

    /**
     * Creates a NotificationTime with custom reminder times.
     *
     * @param reminder1 LocalTime for the first reminder
     * @param reminder2 LocalTime for the second reminder
     * @param reminder3 LocalTime for the third reminder
     * @return a new NotificationTime instance with specified times
     */
    @Override
    public NotificationTimeInterf create(LocalTime reminder1, LocalTime reminder2, LocalTime reminder3) {
        NotificationTime notificationTime = new NotificationTime();
        notificationTime.setReminder1(reminder1);
        notificationTime.setReminder2(reminder2);
        notificationTime.setReminder3(reminder3);
        return notificationTime;
    }
}

