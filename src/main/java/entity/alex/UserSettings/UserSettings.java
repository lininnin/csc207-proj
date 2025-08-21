package entity.alex.UserSettings;

import entity.alex.NotificationTime.NotificationTimeInterf;

/**
 * Represents the settings associated with a user,
 * including notification schedule and timezone.
 */
public class UserSettings implements UserSettingsInterf {

    /**
     * The unique identifier of the user.
     */
    private final String userId;

    /**
     * The notification time settings for the user.
     */
    private final NotificationTimeInterf notificationTime;

    /**
     * The timezone setting for the user (default: Toronto).
     */
    private String timezone;

    /**
     * Constructs a new UserSettings object.
     *
     * @param userIdParam the unique ID of the user; must not be null or empty
     * @param notificationTimeParam the user's notification time settings;
     *                              must not be null
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public UserSettings(
            final String userIdParam,
            final NotificationTimeInterf notificationTimeParam) {

        if (userIdParam == null || userIdParam.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (notificationTimeParam == null) {
            throw new IllegalArgumentException("NotificationTime cannot be null.");
        }

        this.userId = userIdParam;
        this.notificationTime = notificationTimeParam;
        this.timezone = "Toronto";  // default
    }

    /**
     * Returns the unique user ID.
     *
     * @return the user ID
     */
    @Override
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the user's notification time settings.
     *
     * @return the notification time settings
     */
    @Override
    public NotificationTimeInterf getNotificationTime() {
        return notificationTime;
    }

    /**
     * Returns the current timezone setting for the user.
     *
     * @return the user's timezone
     */
    @Override
    public String getTimezone() {
        return timezone;
    }

    /**
     * Sets the user's timezone.
     *
     * @param timezoneParam the timezone to set
     */
    public void setTimezone(final String timezoneParam) {
        this.timezone = timezoneParam;
    }
}
