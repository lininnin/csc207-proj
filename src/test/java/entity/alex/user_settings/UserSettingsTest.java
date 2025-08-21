package entity.alex.user_settings;

import entity.alex.NotificationTime.NotificationTime;
import entity.alex.NotificationTime.NotificationTimeInterf;
import entity.alex.UserSettings.UserSettings;
import entity.alex.UserSettings.UserSettingsInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserSettingsTest {

    private NotificationTimeInterf notificationTime;

    @BeforeEach
    void setUp() {
        notificationTime = new NotificationTime();
    }

    @Test
    void testConstructorInitializesCorrectly() {
        UserSettingsInterf settings = new UserSettings("user123", notificationTime);

        assertEquals("user123", settings.getUserId());
        assertEquals(notificationTime, settings.getNotificationTime());
        assertEquals("Toronto", settings.getTimezone()); // 默认值
    }

    @Test
    void testSetTimezone() {
        UserSettings settings = new UserSettings("user456", notificationTime);
        settings.setTimezone("Vancouver");
        assertEquals("Vancouver", settings.getTimezone());
    }

    @Test
    void testConstructorWithNullUserIdThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new UserSettings(null, notificationTime));
    }

    @Test
    void testConstructorWithEmptyUserIdThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new UserSettings("   ", notificationTime));
    }

    @Test
    void testConstructorWithNullNotificationTimeThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new UserSettings("user789", null));
    }
}
