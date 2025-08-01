package entity.Alex.NotificationTime;

import java.time.LocalTime;

/**
 * Represents a set of daily notification reminder times.
 * By default, the reminders are set at 08:00, 12:00, and 20:00.
 */
public class NotificationTime implements NotificationTimeInterf {
    private LocalTime reminder1;
    private LocalTime reminder2;
    private LocalTime reminder3;

    /**
     * Constructs a NotificationTime object with default reminder times.
     * - reminder1: 08:00
     * - reminder2: 12:00
     * - reminder3: 20:00
     */
    public NotificationTime() {
        this.reminder1 = LocalTime.of(8, 0);
        this.reminder2 = LocalTime.of(12, 0);
        this.reminder3 = LocalTime.of(20, 0);
    }

    // ------------------ Getters ------------------

    /**
     * @return Time of first reminder (e.g., 08:00)
     */
    public LocalTime getReminder1() {
        return reminder1;
    }

    /**
     * @return Time of second reminder (e.g., 12:00)
     */
    public LocalTime getReminder2() {
        return reminder2;
    }

    /**
     * @return Time of third reminder (e.g., 20:00)
     */
    public LocalTime getReminder3() {
        return reminder3;
    }

    // ------------------ Setters ------------------

    /**
     * Sets the time of the first reminder.
     *
     * @param reminder1 The LocalTime for reminder1
     * @throws IllegalArgumentException if reminder1 is null
     */
    public void setReminder1(LocalTime reminder1) {
        if (reminder1 == null) {
            throw new IllegalArgumentException("Reminder1 time cannot be null");
        }
        this.reminder1 = reminder1;
    }

    /**
     * Sets the time of the second reminder.
     *
     * @param reminder2 The LocalTime for reminder2
     * @throws IllegalArgumentException if reminder2 is null
     */
    public void setReminder2(LocalTime reminder2) {
        if (reminder2 == null) {
            throw new IllegalArgumentException("Reminder2 time cannot be null");
        }
        this.reminder2 = reminder2;
    }

    /**
     * Sets the time of the third reminder.
     *
     * @param reminder3 The LocalTime for reminder3
     * @throws IllegalArgumentException if reminder3 is null
     */
    public void setReminder3(LocalTime reminder3) {
        if (reminder3 == null) {
            throw new IllegalArgumentException("Reminder3 time cannot be null");
        }
        this.reminder3 = reminder3;
    }
}

