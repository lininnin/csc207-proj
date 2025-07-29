package entityTest;

import entity.Alex.DailyEventLog.DailyEventLog;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Alex.Event.Event;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DailyEventLogTest {

    private DailyEventLog dailyEventLog;
    private Event event1;
    private Event event2;

    @BeforeEach
    public void setUp() {
        dailyEventLog = new DailyEventLog(LocalDate.of(2025, 7, 22));

        Info info1 = new Info.Builder("Meeting")
                .description("Client discussion")
                .category("Work")
                .build();

        Info info2 = new Info.Builder("Yoga")
                .description("Morning session")
                .category("Health")
                .build();

        BeginAndDueDates dateRange = new BeginAndDueDates(
                LocalDate.of(2025, 7, 22),
                LocalDate.of(2025, 7, 22)
        );

        event1 = new Event.Builder(info1)
                .beginAndDueDates(dateRange)
                .build();

        event2 = new Event.Builder(info2)
                .beginAndDueDates(dateRange)
                .build();
    }

    @Test
    public void testConstructorInitializesFields() {
        assertEquals(LocalDate.of(2025, 7, 22), dailyEventLog.getDate());
        assertNotNull(dailyEventLog.getId());
        assertTrue(dailyEventLog.getActualEvents().isEmpty());
    }

    @Test
    public void testAddEventSuccessfully() {
        dailyEventLog.addEntry(event1);
        List<Event> events = dailyEventLog.getActualEvents();
        assertEquals(1, events.size());
        assertTrue(events.contains(event1));
    }

    @Test
    public void testAddDuplicateEventDoesNotDuplicate() {
        dailyEventLog.addEntry(event1);
        dailyEventLog.addEntry(event1);
        assertEquals(1, dailyEventLog.getActualEvents().size());
    }

    @Test
    public void testAddNullEventIgnored() {
        dailyEventLog.addEntry(null);
        assertTrue(dailyEventLog.getActualEvents().isEmpty());
    }

    @Test
    public void testRemoveEventByIdSuccessfully() {
        dailyEventLog.addEntry(event1);
        dailyEventLog.addEntry(event2);

        dailyEventLog.removeEntry(event1.getInfo().getId());

        List<Event> remaining = dailyEventLog.getActualEvents();
        assertEquals(1, remaining.size());
        assertTrue(remaining.contains(event2));
        assertFalse(remaining.contains(event1));
    }

    @Test
    public void testRemoveWithInvalidIdDoesNothing() {
        dailyEventLog.addEntry(event1);
        dailyEventLog.removeEntry("non-existent-id");
        assertEquals(1, dailyEventLog.getActualEvents().size());
    }

    @Test
    public void testRemoveWithNullIdDoesNothing() {
        dailyEventLog.addEntry(event1);
        dailyEventLog.removeEntry(null);
        assertEquals(1, dailyEventLog.getActualEvents().size());
    }

    @Test
    public void testGetActualEventsReturnsDefensiveCopy() {
        dailyEventLog.addEntry(event1);
        List<Event> copy = dailyEventLog.getActualEvents();
        copy.clear();  // modifying the copy
        assertEquals(1, dailyEventLog.getActualEvents().size()); // original should remain intact
    }
}

