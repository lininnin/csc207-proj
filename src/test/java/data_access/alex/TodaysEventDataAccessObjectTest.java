package data_access.alex;

import entity.alex.DailyEventLog.DailyEventLogFactoryInterf;
import entity.alex.DailyEventLog.DailyEventLogInterf;
import entity.alex.Event.EventInterf;
import entity.info.Info;
import entity.info.InfoInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TodaysEventDataAccessObjectTest {

    private DailyEventLogInterf mockLog;
    private TodaysEventDataAccessObject dao;

    private EventInterf mockEvent;
    private InfoInterf mockInfo;

    @BeforeEach
    public void setup() {
        mockLog = mock(DailyEventLogInterf.class);
        DailyEventLogFactoryInterf mockFactory = mock(DailyEventLogFactoryInterf.class);
        when(mockFactory.create(any(LocalDate.class))).thenReturn(mockLog);

        dao = new TodaysEventDataAccessObject(mockFactory);

        mockEvent = mock(EventInterf.class);
        mockInfo = mock(InfoInterf.class);
        when(mockEvent.getInfo()).thenReturn(mockInfo);
    }

    @Test
    public void testSaveAddsEntry() {
        dao.save(mockEvent);
        verify(mockLog).addEntry(mockEvent);
    }

    @Test
    public void testSaveThrowsIfNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.save(null));
    }

    @Test
    public void testRemoveSuccessful() {
        when(mockInfo.getId()).thenReturn("id123");
        List<EventInterf> events = new ArrayList<>();
        events.add(mockEvent);

        when(mockLog.getActualEvents()).thenReturn(events).thenReturn(new ArrayList<>());
        boolean result = dao.remove(mockEvent);

        assertTrue(result);
        verify(mockLog).removeEntry("id123");
    }

    @Test
    public void testRemoveFailsIfNullInfo() {
        when(mockEvent.getInfo()).thenReturn(null);
        assertFalse(dao.remove(mockEvent));
    }

    @Test
    public void testGetTodaysEventsDelegatesToLog() {
        List<EventInterf> events = List.of(mockEvent);
        when(mockLog.getActualEvents()).thenReturn(events);

        List<EventInterf> result = dao.getTodaysEvents();
        assertEquals(events, result);
    }

    @Test
    public void testGetEventsByCategoryMatch() {
        when(mockInfo.getCategory()).thenReturn("work");
        when(mockLog.getActualEvents()).thenReturn(List.of(mockEvent));

        List<EventInterf> result = dao.getEventsByCategory("work");
        assertEquals(1, result.size());
    }

    @Test
    public void testGetEventsByCategoryNoMatch() {
        when(mockInfo.getCategory()).thenReturn("other");
        when(mockLog.getActualEvents()).thenReturn(List.of(mockEvent));

        List<EventInterf> result = dao.getEventsByCategory("work");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetEventsByNameMatch() {
        when(mockInfo.getName()).thenReturn("Meeting");
        when(mockLog.getActualEvents()).thenReturn(List.of(mockEvent));

        List<EventInterf> result = dao.getEventsByName("Meeting");
        assertEquals(1, result.size());
    }

    @Test
    public void testGetEventsByNameNoMatch() {
        when(mockInfo.getName()).thenReturn("Workout");
        when(mockLog.getActualEvents()).thenReturn(List.of(mockEvent));

        List<EventInterf> result = dao.getEventsByName("Reading");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetEventCount() {
        when(mockLog.getActualEvents()).thenReturn(List.of(mockEvent));
        assertEquals(1, dao.getEventCount());
    }

    @Test
    public void testContainsReturnsTrue() {
        when(mockLog.getActualEvents()).thenReturn(List.of(mockEvent));
        assertTrue(dao.contains(mockEvent));
    }

    @Test
    public void testClearAllRemovesAll() {
        InfoInterf info1 = mock(InfoInterf.class);
        when(info1.getId()).thenReturn("a");

        EventInterf e1 = mock(EventInterf.class);
        when(e1.getInfo()).thenReturn(info1);

        when(mockLog.getActualEvents()).thenReturn(List.of(e1));
        dao.clearAll();
        verify(mockLog).removeEntry("a");
    }

    @Test
    public void testGetEventByIdFound() {
        when(mockInfo.getId()).thenReturn("abc");
        when(mockLog.getActualEvents()).thenReturn(List.of(mockEvent));

        assertEquals(mockEvent, dao.getEventById("abc"));
    }

    @Test
    public void testGetEventByIdNotFound() {
        when(mockInfo.getId()).thenReturn("def");
        when(mockLog.getActualEvents()).thenReturn(List.of(mockEvent));

        assertNull(dao.getEventById("xyz"));
    }

    @Test
    public void testUpdateSuccessful() {
        when(mockInfo.getId()).thenReturn("match");
        EventInterf updated = mock(EventInterf.class);
        InfoInterf updatedInfo = mock(InfoInterf.class);
        when(updated.getInfo()).thenReturn(updatedInfo);
        when(updatedInfo.getId()).thenReturn("match");

        when(mockLog.getActualEvents()).thenReturn(List.of(mockEvent));
        when(mockEvent.getInfo()).thenReturn(mockInfo);
        when(mockInfo.getId()).thenReturn("match");

        assertTrue(dao.update(updated));
        verify(mockLog).removeEntry("match");
        verify(mockLog).addEntry(updated);
    }

    @Test
    public void testUpdateFailsIfNotExists() {
        EventInterf updated = mock(EventInterf.class);
        InfoInterf updatedInfo = mock(InfoInterf.class);
        when(updated.getInfo()).thenReturn(updatedInfo);
        when(updatedInfo.getId()).thenReturn("not-found");

        when(mockLog.getActualEvents()).thenReturn(List.of(mockEvent));
        when(mockInfo.getId()).thenReturn("something-else");

        assertFalse(dao.update(updated));
    }

    @Test
    public void testExistsByIdFound() {
        when(mockInfo.getId()).thenReturn("exist");
        when(mockLog.getActualEvents()).thenReturn(List.of(mockEvent));

        assertTrue(dao.existsById("exist"));
    }

    @Test
    public void testExistsByIdNotFound() {
        when(mockInfo.getId()).thenReturn("wrong");
        when(mockLog.getActualEvents()).thenReturn(List.of(mockEvent));

        assertFalse(dao.existsById("missing"));
    }

    @Test
    public void testGetDailyEventLogReturnsInstance() {
        assertEquals(mockLog, dao.getDailyEventLog());
    }

    @Test
    public void testFindAvailableEventsByCategoryReturnsEmpty() {
        assertTrue(dao.findAvailableEventsByCategory("any").isEmpty());
    }

    @Test
    public void testFindTodaysEventsByCategoryCastsToInfo() {
        Info info = mock(Info.class);
        when(info.getCategory()).thenReturn("c1");
        when(info.getId()).thenReturn("id1");

        EventInterf e = mock(EventInterf.class);
        when(e.getInfo()).thenReturn(info);

        when(mockLog.getActualEvents()).thenReturn(List.of(e));
        List<Info> result = dao.findTodaysEventsByCategory("c1");

        assertEquals(1, result.size());
        assertEquals(info, result.get(0));
    }

    @Test
    public void testClearAvailableEventCategoryReturnsFalse() {
        assertFalse(dao.clearAvailableEventCategory("irrelevant"));
    }

    @Test
    public void testClearTodaysEventCategorySuccess() {
        InfoInterf info = mock(InfoInterf.class);
        when(info.getId()).thenReturn("idX");

        EventInterf e = mock(EventInterf.class);
        when(e.getInfo()).thenReturn(info);

        when(mockLog.getActualEvents()).thenReturn(List.of(e));

        boolean result = dao.clearTodaysEventCategory("idX");

        assertTrue(result);
        verify(info).setCategory(null);
    }

    @Test
    public void testClearTodaysEventCategoryFail() {
        InfoInterf info = mock(InfoInterf.class);
        when(info.getId()).thenReturn("notMatch");

        EventInterf e = mock(EventInterf.class);
        when(e.getInfo()).thenReturn(info);

        when(mockLog.getActualEvents()).thenReturn(List.of(e));
        assertFalse(dao.clearTodaysEventCategory("missing"));
    }
}
