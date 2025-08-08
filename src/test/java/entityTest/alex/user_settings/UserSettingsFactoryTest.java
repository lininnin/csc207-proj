package entityTest.alex.user_settings;

import entity.Alex.NotificationTime.NotificationTime;
import entity.Alex.UserSettings.UserSettings;
import entity.Alex.UserSettings.UserSettingsFactory;
import entity.Alex.UserSettings.UserSettingsInterf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserSettingsFactoryTest {

    @Test
    public void testCreateUserSettingsSuccessfully() {
        String userId = "user-123";
        NotificationTime notificationTime = new NotificationTime();  // 默认时间

        UserSettingsFactory factory = new UserSettingsFactory();
        UserSettingsInterf settings = factory.create(userId, notificationTime);

        assertNotNull(settings);
        assertTrue(settings instanceof UserSettings);

        UserSettings casted = (UserSettings) settings;
        assertEquals("user-123", casted.getUserId());
        assertEquals(notificationTime, casted.getNotificationTime());
    }

    @Test
    public void testCreateWithNullUserId() {
        NotificationTime notificationTime = new NotificationTime();
        UserSettingsFactory factory = new UserSettingsFactory();

        assertThrows(IllegalArgumentException.class, () -> factory.create(null, notificationTime));
    }

    @Test
    public void testCreateWithNullNotificationTime() {
        UserSettingsFactory factory = new UserSettingsFactory();

        assertThrows(IllegalArgumentException.class, () -> factory.create("user-456", null));
    }
}

