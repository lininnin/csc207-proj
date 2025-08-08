package entity.Alex.UserSettings;

import entity.Alex.NotificationTime.NotificationTimeInterf;

/**
 * Default factory for creating UserSettings objects.
 */
public class UserSettingsFactory implements UserSettingsFactoryInterf {

    /**
     * Creates a new {@link UserSettingsInterf} instance for the specified user.
     *
     * @param userId the unique identifier of the user;
     *               must not be null or empty
     * @param notificationTime the notification time settings for the user;
     *                         must not be null
     * @return a new {@link UserSettingsInterf} instance
     * initialized with the given userId and notificationTime
     * @throws IllegalArgumentException if {@code userId} is null or empty,
     * or if {@code notificationTime} is null
     */
    @Override
    public UserSettingsInterf create(final String userId,
                                     final NotificationTimeInterf notificationTime) {
        return new UserSettings(userId, notificationTime);
    }

}

