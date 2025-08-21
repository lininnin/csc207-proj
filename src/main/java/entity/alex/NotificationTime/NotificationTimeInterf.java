package entity.alex.NotificationTime;

import java.time.LocalTime;

/**
 * Interface for NotificationTime entity.
 * Defines operations for getting and setting daily notification times.
 */
public interface NotificationTimeInterf {

    // ------------------ Getters ------------------

    /**
     * @return LocalTime of the first reminder
     */
    LocalTime getReminder1();

    /**
     * @return LocalTime of the second reminder
     */
    LocalTime getReminder2();

    /**
     * @return LocalTime of the third reminder
     */
    LocalTime getReminder3();

    // ------------------ Setters ------------------

    /**
     * Sets the time of the first reminder.
     *
     * @param reminder1 LocalTime for reminder1
     */
    void setReminder1(LocalTime reminder1);

    /**
     * Sets the time of the second reminder.
     *
     * @param reminder2 LocalTime for reminder2
     */
    void setReminder2(LocalTime reminder2);

    /**
     * Sets the time of the third reminder.
     *
     * @param reminder3 LocalTime for reminder3
     */
    void setReminder3(LocalTime reminder3);
}

