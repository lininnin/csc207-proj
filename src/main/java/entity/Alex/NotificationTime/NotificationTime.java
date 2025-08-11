package entity.Alex.NotificationTime;

import java.time.LocalTime;

/**
 * Represents a set of daily notification reminder times.
 * By default, the reminders are set at 08:00, 12:00, and 20:00.
 */
public class NotificationTime implements NotificationTimeInterf {

    /** Default hour for reminder 1 (08:00). */
    private static final int DEFAULT_REMINDER1_HOUR = 8;

    /** Default hour for reminder 2 (12:00). */
    private static final int DEFAULT_REMINDER2_HOUR = 12;

    /** Default hour for reminder 3 (20:00). */
    private static final int DEFAULT_REMINDER3_HOUR = 20;

    /** First reminder time (morning). */
    private LocalTime reminder1;

    /** Second reminder time (noon). */
    private LocalTime reminder2;

    /** Third reminder time (evening). */
    private LocalTime reminder3;

    /**
     * Constructs a NotificationTime object with default reminder times.
     * - reminder1: 08:00
     * - reminder2: 12:00
     * - reminder3: 20:00
     */
    public NotificationTime() {
        this.reminder1 = LocalTime.of(DEFAULT_REMINDER1_HOUR, 0);
        this.reminder2 = LocalTime.of(DEFAULT_REMINDER2_HOUR, 0);
        this.reminder3 = LocalTime.of(DEFAULT_REMINDER3_HOUR, 0);
    }

    // ------------------ Getters ------------------

    /**
     * Returns the time of the first reminder.
     *
     * @return Time of first reminder (e.g., 08:00)
     */
    public LocalTime getReminder1() {
        return reminder1;
    }

    /**
     * Returns the time of the second reminder.
     *
     * @return Time of second reminder (e.g., 12:00)
     */
    public LocalTime getReminder2() {
        return reminder2;
    }

    /**
     * Returns the time of the third reminder.
     *
     * @return Time of third reminder (e.g., 20:00)
     */
    public LocalTime getReminder3() {
        return reminder3;
    }

    // ------------------ Setters ------------------

    /**
     * Sets the time of the first reminder.
     *
     * @param reminder1Param the new time for reminder1
     * @throws IllegalArgumentException if {@code reminder1} is null
     */
    public void setReminder1(final LocalTime reminder1Param) {
        if (reminder1Param == null) {
            throw new IllegalArgumentException("Reminder1 time cannot be null.");
        }
        this.reminder1 = reminder1Param;
    }

    /**
     * Sets the time of the second reminder.
     *
     * @param reminder2Param the new time for reminder2
     * @throws IllegalArgumentException if {@code reminder2} is null
     */
    public void setReminder2(final LocalTime reminder2Param) {
        if (reminder2Param == null) {
            throw new IllegalArgumentException("Reminder2 time cannot be null.");
        }
        this.reminder2 = reminder2Param;
    }

    /**
     * Sets the time of the third reminder.
     *
     * @param reminder3Param the new time for reminder3
     * @throws IllegalArgumentException if {@code reminder3} is null
     */
    public void setReminder3(final LocalTime reminder3Param) {
        if (reminder3Param == null) {
            throw new IllegalArgumentException("Reminder3 time cannot be null.");
        }
        this.reminder3 = reminder3Param;
    }
}


