package entityTest.alex.daily_event_log;

import entity.Alex.DailyEventLog.DailyEventLog;
import entity.Alex.Event.Event;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DailyEventLogTest {

    private DailyEventLog eventLog;
    private Event mockEvent;

    @BeforeEach
    public void setUp() {
        eventLog = new DailyEventLog(LocalDate.of(2025, 8, 8));

        Info info = new Info.Builder("Test Event")
                .description("Team sync")
                .category("Work")
                .build();

        // 使用简单构造的 BeginAndDueDates 对象（你可替换为真实类）
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.of(2025, 8, 8), LocalDate.of(2025, 8, 10));

        mockEvent = new Event.Builder(info)
                .beginAndDueDates(dates)
                .oneTime(true)
                .build();
    }

    @Test
    public void testAddEntry() {
        eventLog.addEntry(mockEvent);
        List<Event> events = eventLog.getActualEvents();

        assertEquals(1, events.size());
        assertEquals(mockEvent.getInfo().getId(), events.get(0).getInfo().getId());
    }

    @Test
    public void testAddNullEntry() {
        eventLog.addEntry(null);
        assertTrue(eventLog.getActualEvents().isEmpty());
    }

    @Test
    public void testAddDuplicateEntry() {
        eventLog.addEntry(mockEvent);
        eventLog.addEntry(mockEvent);
        assertEquals(1, eventLog.getActualEvents().size());
    }

    @Test
    public void testRemoveEntryById() {
        eventLog.addEntry(mockEvent);
        eventLog.removeEntry(mockEvent.getInfo().getId());
        assertTrue(eventLog.getActualEvents().isEmpty());
    }

    @Test
    public void testRemoveEntryWithInvalidId() {
        eventLog.addEntry(mockEvent);
        eventLog.removeEntry("non-existent-id");
        assertEquals(1, eventLog.getActualEvents().size());
    }

    @Test
    public void testGetDate() {
        assertEquals(LocalDate.of(2025, 8, 8), eventLog.getDate());
    }

    @Test
    public void testGetIdIsNotNull() {
        assertNotNull(eventLog.getId());
    }

    @Test
    public void testGetActualEventsReturnsCopy() {
        eventLog.addEntry(mockEvent);
        List<Event> copy = eventLog.getActualEvents();
        copy.clear();
        assertEquals(1, eventLog.getActualEvents().size());
    }
}
