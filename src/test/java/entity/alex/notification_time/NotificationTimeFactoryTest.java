package entity.alex.notification_time;

import entity.Alex.NotificationTime.NotificationTimeFactory;
import entity.Alex.NotificationTime.NotificationTimeFactoryInterf;
import entity.Alex.NotificationTime.NotificationTimeInterf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationTimeFactoryTest {

    private NotificationTimeFactoryInterf factory;

    @BeforeEach
    void setUp() {
        factory = new NotificationTimeFactory();
    }

    @Test
    void testCreateDefaultNotificationTime() {
        NotificationTimeInterf time = factory.createDefault();

        assertEquals(LocalTime.of(8, 0), time.getReminder1());
        assertEquals(LocalTime.of(12, 0), time.getReminder2());
        assertEquals(LocalTime.of(20, 0), time.getReminder3());
    }

    @Test
    void testCreateCustomNotificationTime() {
        LocalTime t1 = LocalTime.of(9, 30);
        LocalTime t2 = LocalTime.of(14, 0);
        LocalTime t3 = LocalTime.of(22, 15);

        NotificationTimeInterf time = factory.create(t1, t2, t3);

        assertEquals(t1, time.getReminder1());
        assertEquals(t2, time.getReminder2());
        assertEquals(t3, time.getReminder3());
    }

    @Test
    void testModifyReminderTimesAfterCreation() {
        NotificationTimeInterf time = factory.createDefault();

        LocalTime new1 = LocalTime.of(6, 45);
        LocalTime new2 = LocalTime.of(13, 30);
        LocalTime new3 = LocalTime.of(19, 0);

        time.setReminder1(new1);
        time.setReminder2(new2);
        time.setReminder3(new3);

        assertEquals(new1, time.getReminder1());
        assertEquals(new2, time.getReminder2());
        assertEquals(new3, time.getReminder3());
    }
}
