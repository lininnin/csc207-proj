package entityTest.alex.notification_time;

import entity.Alex.NotificationTime.NotificationTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationTimeTest {

    private NotificationTime notificationTime;

    @BeforeEach
    public void setUp() {
        notificationTime = new NotificationTime();
    }

    @Test
    public void testDefaultReminderTimes() {
        assertEquals(LocalTime.of(8, 0), notificationTime.getReminder1());
        assertEquals(LocalTime.of(12, 0), notificationTime.getReminder2());
        assertEquals(LocalTime.of(20, 0), notificationTime.getReminder3());
    }

    @Test
    public void testSetReminder1Successfully() {
        LocalTime newTime = LocalTime.of(9, 15);
        notificationTime.setReminder1(newTime);
        assertEquals(newTime, notificationTime.getReminder1());
    }

    @Test
    public void testSetReminder2Successfully() {
        LocalTime newTime = LocalTime.of(14, 30);
        notificationTime.setReminder2(newTime);
        assertEquals(newTime, notificationTime.getReminder2());
    }

    @Test
    public void testSetReminder3Successfully() {
        LocalTime newTime = LocalTime.of(21, 45);
        notificationTime.setReminder3(newTime);
        assertEquals(newTime, notificationTime.getReminder3());
    }

    @Test
    public void testSetReminder1NullThrows() {
        assertThrows(IllegalArgumentException.class, () -> notificationTime.setReminder1(null));
    }

    @Test
    public void testSetReminder2NullThrows() {
        assertThrows(IllegalArgumentException.class, () -> notificationTime.setReminder2(null));
    }

    @Test
    public void testSetReminder3NullThrows() {
        assertThrows(IllegalArgumentException.class, () -> notificationTime.setReminder3(null));
    }
}

