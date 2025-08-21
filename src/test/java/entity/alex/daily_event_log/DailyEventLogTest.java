package entity.alex.daily_event_log;

import entity.alex.DailyEventLog.DailyEventLog;
import entity.alex.Event.EventInterf;
import entity.info.InfoInterf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DailyEventLog.
 */
public class DailyEventLogTest {

    private DailyEventLog log;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2025, 8, 14);
        log = new DailyEventLog(testDate);
    }

    @Test
    void testGetDateReturnsCorrectValue() {
        assertEquals(testDate, log.getDate());
    }

    @Test
    void testGetIdIsNotNullAndUnique() {
        String id1 = log.getId();
        String id2 = new DailyEventLog(LocalDate.now()).getId();
        assertNotNull(id1);
        assertNotEquals(id1, id2, "Each instance should have a unique ID.");
    }

    @Test
    void testAddEntrySuccessfullyAddsNewEvent() {
        EventInterf mockEvent = mock(EventInterf.class);
        log.addEntry(mockEvent);
        List<EventInterf> events = log.getActualEvents();
        assertEquals(1, events.size());
        assertTrue(events.contains(mockEvent));
    }

    @Test
    void testAddEntryDoesNotAddNull() {
        log.addEntry(null);
        assertTrue(log.getActualEvents().isEmpty());
    }

    @Test
    void testAddEntryDoesNotAddDuplicate() {
        EventInterf mockEvent = mock(EventInterf.class);
        log.addEntry(mockEvent);
        log.addEntry(mockEvent); // Duplicate
        assertEquals(1, log.getActualEvents().size());
    }

    @Test
    void testRemoveEntryByIdSuccess() {
        EventInterf mockEvent = mock(EventInterf.class);
        InfoInterf mockInfo = mock(InfoInterf.class);
        when(mockInfo.getId()).thenReturn("event123");
        when(mockEvent.getInfo()).thenReturn(mockInfo);

        log.addEntry(mockEvent);
        log.removeEntry("event123");

        assertTrue(log.getActualEvents().isEmpty());
    }

    @Test
    void testRemoveEntryByIdFailsIfIdNotMatch() {
        EventInterf mockEvent = mock(EventInterf.class);
        InfoInterf mockInfo = mock(InfoInterf.class);
        when(mockInfo.getId()).thenReturn("event123");
        when(mockEvent.getInfo()).thenReturn(mockInfo);

        log.addEntry(mockEvent);
        log.removeEntry("nonexistent-id");

        assertEquals(1, log.getActualEvents().size());
    }

    @Test
    void testRemoveEntryDoesNothingIfNullId() {
        EventInterf mockEvent = mock(EventInterf.class);
        InfoInterf mockInfo = mock(InfoInterf.class);
        when(mockInfo.getId()).thenReturn("event456");
        when(mockEvent.getInfo()).thenReturn(mockInfo);

        log.addEntry(mockEvent);
        log.removeEntry(null); // Should do nothing

        assertEquals(1, log.getActualEvents().size());
    }

    @Test
    void testGetActualEventsReturnsCopy() {
        EventInterf mockEvent = mock(EventInterf.class);
        log.addEntry(mockEvent);
        List<EventInterf> events = log.getActualEvents();
        events.clear(); // Modify copy

        assertEquals(1, log.getActualEvents().size(), "Original list should not be affected.");
    }
}
