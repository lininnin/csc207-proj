package entityTest;

import entity.Alex.UserSettings.UserSettings;
import entity.Alex.NotificationTime.NotificationTime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserSettingsTest {

    @Test
    public void testValidConstructor() {
        NotificationTime notificationTime = new NotificationTime();
        UserSettings settings = new UserSettings("user123", notificationTime);

        assertEquals("user123", settings.getUserId());
        assertEquals(notificationTime, settings.getNotificationTime());
        assertEquals("Toronto", settings.getTimezone());
    }

    @Test
    public void testConstructorWithNullUserId() {
        NotificationTime notificationTime = new NotificationTime();
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new UserSettings(null, notificationTime)
        );
        assertEquals("User ID cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testConstructorWithEmptyUserId() {
        NotificationTime notificationTime = new NotificationTime();
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new UserSettings("   ", notificationTime)
        );
        assertEquals("User ID cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testConstructorWithNullNotificationTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new UserSettings("user123", null)
        );
        assertEquals("NotificationTime cannot be null", exception.getMessage());
    }

    @Test
    public void testGetters() {
        NotificationTime notificationTime = new NotificationTime();
        UserSettings settings = new UserSettings("user456", notificationTime);

        assertAll(
                () -> assertEquals("user456", settings.getUserId()),
                () -> assertEquals(notificationTime, settings.getNotificationTime()),
                () -> assertEquals("Toronto", settings.getTimezone())
        );
    }
}
