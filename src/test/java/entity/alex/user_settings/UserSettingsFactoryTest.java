package entity.alex.user_settings;

import entity.Alex.NotificationTime.NotificationTime;
import entity.Alex.NotificationTime.NotificationTimeInterf;
import entity.Alex.UserSettings.UserSettingsFactory;
import entity.Alex.UserSettings.UserSettingsFactoryInterf;
import entity.Alex.UserSettings.UserSettingsInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserSettingsFactoryTest {

    private UserSettingsFactoryInterf factory;
    private NotificationTimeInterf notificationTime;

    @BeforeEach
    void setUp() {
        factory = new UserSettingsFactory();
        notificationTime = new NotificationTime();
        notificationTime.setReminder1(LocalTime.of(9, 0));
        notificationTime.setReminder2(LocalTime.of(13, 0));
        notificationTime.setReminder3(LocalTime.of(18, 0));
    }

    @Test
    void testCreateUserSettingsSuccessfully() {
        UserSettingsInterf settings = factory.create("user123", notificationTime);

        assertNotNull(settings);
        assertEquals("user123", settings.getUserId());
        assertEquals(notificationTime, settings.getNotificationTime());
    }

    @Test
    void testCreateWithNullUserIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, notificationTime));
    }

    @Test
    void testCreateWithEmptyUserIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> factory.create("   ", notificationTime));
    }

    @Test
    void testCreateWithNullNotificationTimeThrows() {
        assertThrows(IllegalArgumentException.class, () -> factory.create("user456", null));
    }
}
