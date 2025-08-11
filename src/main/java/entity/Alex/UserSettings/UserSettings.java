package entity.Alex.UserSettings;

import entity.Alex.NotificationTime.NotificationTimeInterf;

/**
 * Represents the settings associated with a user, including notification schedule and timezone.
 */
public class UserSettings implements UserSettingsInterf {

    private final String userId;
    private final NotificationTimeInterf notificationTime;
    private String timezone;

    /**
     * Constructs a new UserSettings object.
     *
     * @param userId the unique ID of the user; must not be null or empty
     * @param notificationTime the user's notification time settings; must not be null
     */
    public UserSettings(String userId, NotificationTimeInterf notificationTime) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (notificationTime == null) {
            throw new IllegalArgumentException("NotificationTime cannot be null");
        }
        this.userId = userId;
        this.notificationTime = notificationTime;
        this.timezone = "Toronto";  // default
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public NotificationTimeInterf getNotificationTime() {
        return notificationTime;
    }

    @Override
    public String getTimezone() {
        return timezone;
    }

    /**
     * Sets the user's timezone.
     *
     * @param timezone The timezone to set
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
