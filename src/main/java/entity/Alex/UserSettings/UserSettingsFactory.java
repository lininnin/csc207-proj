package entity.Alex.UserSettings;

import entity.Alex.NotificationTime.NotificationTimeInterf;

/**
 * Default factory for creating UserSettings objects.
 */
public class UserSettingsFactory implements UserSettingsFactoryInterf {

    @Override
    public UserSettingsInterf create(String userId, NotificationTimeInterf notificationTime) {
        return new UserSettings(userId, notificationTime);
    }
}

