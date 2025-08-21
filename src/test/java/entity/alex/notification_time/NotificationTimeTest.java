package entity.alex.notification_time;

import entity.Alex.NotificationTime.NotificationTime;
import entity.Alex.NotificationTime.NotificationTimeInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationTimeTest {

    private NotificationTimeInterf notificationTime;

    @BeforeEach
    void setUp() {
        notificationTime = new NotificationTime();
    }

    @Test
    void testDefaultConstructorValues() {
        assertEquals(LocalTime.of(8, 0), notificationTime.getReminder1());
        assertEquals(LocalTime.of(12, 0), notificationTime.getReminder2());
        assertEquals(LocalTime.of(20, 0), notificationTime.getReminder3());
    }

    @Test
    void testSetReminder1Successfully() {
        LocalTime newTime = LocalTime.of(7, 15);
        notificationTime.setReminder1(newTime);
        assertEquals(newTime, notificationTime.getReminder1());
    }

    @Test
    void testSetReminder2Successfully() {
        LocalTime newTime = LocalTime.of(13, 45);
        notificationTime.setReminder2(newTime);
        assertEquals(newTime, notificationTime.getReminder2());
    }

    @Test
    void testSetReminder3Successfully() {
        LocalTime newTime = LocalTime.of(22, 0);
        notificationTime.setReminder3(newTime);
        assertEquals(newTime, notificationTime.getReminder3());
    }

    @Test
    void testSetReminder1WithNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> notificationTime.setReminder1(null));
    }

    @Test
    void testSetReminder2WithNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> notificationTime.setReminder2(null));
    }

    @Test
    void testSetReminder3WithNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> notificationTime.setReminder3(null));
    }
}
