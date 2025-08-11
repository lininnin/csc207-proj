package entity.Alex.UserSettings;

import entity.Alex.NotificationTime.NotificationTimeInterf;

/**
 * Factory interface for creating UserSettings instances.
 */
public interface UserSettingsFactoryInterf {

    /**
     * Creates a new UserSettings instance.
     *
     * @param userId The unique user ID
     * @param notificationTime The user's notification time settings
     * @return A new UserSettingsInterf instance
     */
    UserSettingsInterf create(String userId,
                              NotificationTimeInterf notificationTime);
}

