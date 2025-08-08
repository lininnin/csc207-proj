package entityTest.alex.notification_time;

import entity.Alex.NotificationTime.NotificationTime;
import entity.Alex.NotificationTime.NotificationTimeFactory;
import entity.Alex.NotificationTime.NotificationTimeInterf;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationTimeFactoryTest {

    @Test
    public void testCreateDefaultNotificationTime() {
        NotificationTimeFactory factory = new NotificationTimeFactory();
        NotificationTimeInterf notification = factory.createDefault();

        assertNotNull(notification);
        assertTrue(notification instanceof NotificationTime);
        assertEquals(LocalTime.of(8, 0), notification.getReminder1());
        assertEquals(LocalTime.of(12, 0), notification.getReminder2());
        assertEquals(LocalTime.of(20, 0), notification.getReminder3());
    }

    @Test
    public void testCreateCustomNotificationTime() {
        NotificationTimeFactory factory = new NotificationTimeFactory();
        LocalTime r1 = LocalTime.of(9, 30);
        LocalTime r2 = LocalTime.of(14, 0);
        LocalTime r3 = LocalTime.of(21, 15);

        NotificationTimeInterf notification = factory.create(r1, r2, r3);

        assertNotNull(notification);
        assertEquals(r1, notification.getReminder1());
        assertEquals(r2, notification.getReminder2());
        assertEquals(r3, notification.getReminder3());
    }

    @Test
    public void testCreateWithNullTimes() {
        NotificationTimeFactory factory = new NotificationTimeFactory();

        NotificationTimeInterf notification = factory.create(null, null, null);

        assertNotNull(notification);
        assertNull(notification.getReminder1());
        assertNull(notification.getReminder2());
        assertNull(notification.getReminder3());
    }
}

