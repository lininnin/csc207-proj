package data_access.alex;

import entity.alex.NotificationTime.NotificationTimeFactoryInterf;
import entity.alex.NotificationTime.NotificationTimeInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationTimeDataAccessObjectTest {

    private NotificationTimeInterf mockNotificationTime;
    private NotificationTimeDataAccessObject dao;

    @BeforeEach
    public void setup() {
        mockNotificationTime = mock(NotificationTimeInterf.class);
        NotificationTimeFactoryInterf factory = mock(NotificationTimeFactoryInterf.class);
        when(factory.createDefault()).thenReturn(mockNotificationTime);

        dao = new NotificationTimeDataAccessObject(factory);
    }

    @Test
    public void testUpdateNotificationTimes() {
        LocalTime t1 = LocalTime.of(9, 0);
        LocalTime t2 = LocalTime.of(13, 0);
        LocalTime t3 = LocalTime.of(21, 0);

        dao.updateNotificationTimes(t1, t2, t3);

        verify(mockNotificationTime).setReminder1(t1);
        verify(mockNotificationTime).setReminder2(t2);
        verify(mockNotificationTime).setReminder3(t3);
    }

    @Test
    public void testGetReminder1() {
        LocalTime expected = LocalTime.of(8, 0);
        when(mockNotificationTime.getReminder1()).thenReturn(expected);

        assertEquals(expected, dao.getReminder1());
    }

    @Test
    public void testGetReminder2() {
        LocalTime expected = LocalTime.of(12, 0);
        when(mockNotificationTime.getReminder2()).thenReturn(expected);

        assertEquals(expected, dao.getReminder2());
    }

    @Test
    public void testGetReminder3() {
        LocalTime expected = LocalTime.of(20, 0);
        when(mockNotificationTime.getReminder3()).thenReturn(expected);

        assertEquals(expected, dao.getReminder3());
    }

    @Test
    public void testGetNotificationTimeReturnsInternalInstance() {
        assertEquals(mockNotificationTime, dao.getNotificationTime());
    }
}

