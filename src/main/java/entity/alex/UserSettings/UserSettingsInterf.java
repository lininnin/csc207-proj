package entity.alex.UserSettings;

import entity.alex.NotificationTime.NotificationTimeInterf;

/**
 * Interface for accessing user-specific settings
 * such as notification times and timezone.
 */
public interface UserSettingsInterf {

    /**
     * @return the unique user ID
     */
    String getUserId();

    /**
     * @return the user's configured notification time settings
     */
    NotificationTimeInterf getNotificationTime();

    /**
     * @return the user's timezone (e.g., "Toronto")
     */
    String getTimezone();
}

