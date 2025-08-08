package entity.Alex.NotificationTime;

import java.time.LocalTime;

/**
 * Factory interface for creating NotificationTime entities.
 */
public interface NotificationTimeFactoryInterf {

    /**
     * Creates a NotificationTime object with default reminder times.
     * - 08:00, 12:00, 20:00
     *
     * @return a new NotificationTime instance with default values
     */
    NotificationTimeInterf createDefault();

    /**
     * Creates a NotificationTime object with custom reminder times.
     *
     * @param reminder1 LocalTime for the first reminder
     * @param reminder2 LocalTime for the second reminder
     * @param reminder3 LocalTime for the third reminder
     * @return a new NotificationTime instance with specified times
     */
    NotificationTimeInterf create(LocalTime reminder1,
                                  LocalTime reminder2,
                                  LocalTime reminder3);
}

