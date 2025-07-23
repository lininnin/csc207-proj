package entity.Alex.UserSettings;

import entity.Alex.NotificationTime.NotificationTime;

/**
 * Represents the settings associated with a user, including their notification schedule and timezone.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li>userId: String — Unique identifier for the user (non-null, non-empty)</li>
 *   <li>notificationTime: NotificationTime — User’s customizable notification schedule (non-null)</li>
 *   <li>timezone: String — User's timezone for accurate time block management (default: "Toronto")</li>
 * </ul>
 */
public class UserSettings {

    private final String userId;
    private final NotificationTime notificationTime;
    private String timezone;

    /**
     * Constructs a new {@code UserSettings} object with the given user ID and notification time.
     * The timezone is set to "Toronto" by default.
     *
     * @param userId the unique ID of the user; must not be null or empty
     * @param notificationTime the user's custom notification times; must not be null
     * @throws IllegalArgumentException if {@code userId} is null/empty or {@code notificationTime} is null
     */
    public UserSettings(String userId, NotificationTime notificationTime) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (notificationTime == null) {
            throw new IllegalArgumentException("NotificationTime cannot be null");
        }
        this.userId = userId;
        this.notificationTime = notificationTime;
        this.timezone = "Toronto";
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the user's notification time settings.
     *
     * @return the notification time settings
     */
    public NotificationTime getNotificationTime() {
        return notificationTime;
    }

    /**
     * Returns the user's timezone.
     *
     * @return the timezone
     */
    public String getTimezone() {
        return timezone;
    }
}

