package entityTest;

import entity.EventAvailable;
import entity.Info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventAvailableTest {

    private EventAvailable eventAvailable;
    private Info event1;
    private Info event2;
    private Info event3;

    @BeforeEach
    public void setUp() {
        eventAvailable = new EventAvailable();

        event1 = new Info.Builder("Meeting")
                .description("Discuss budget")
                .category("Work")
                .build();

        event2 = new Info.Builder("Yoga Class")
                .description("Evening session")
                .category("Wellness")
                .build();

        event3 = new Info.Builder("Team Lunch")
                .description("With new interns")
                .category("Work")
                .build();
    }

    @Test
    public void testAddEventSuccessfully() {
        eventAvailable.addEvent(event1);
        assertEquals(1, eventAvailable.getEventCount());
        assertTrue(eventAvailable.contains(event1));
    }

    @Test
    public void testAddNullEventThrows() {
        assertThrows(IllegalArgumentException.class, () -> eventAvailable.addEvent(null));
    }

    @Test
    public void testRemoveEventSuccessfully() {
        eventAvailable.addEvent(event1);
        assertTrue(eventAvailable.removeEvent(event1));
        assertFalse(eventAvailable.contains(event1));
    }

    @Test
    public void testRemoveNonexistentEventReturnsFalse() {
        assertFalse(eventAvailable.removeEvent(event2));
    }

    @Test
    public void testGetEventsByCategory() {
        eventAvailable.addEvent(event1);
        eventAvailable.addEvent(event2);
        eventAvailable.addEvent(event3);

        List<Info> workEvents = eventAvailable.getEventsByCategory("Work");
        assertEquals(2, workEvents.size());
        assertTrue(workEvents.contains(event1));
        assertTrue(workEvents.contains(event3));
    }

    @Test
    public void testGetEventsByName() {
        eventAvailable.addEvent(event1);
        eventAvailable.addEvent(event2);
        eventAvailable.addEvent(event3);

        List<Info> named = eventAvailable.getEventsByName("Yoga Class");
        assertEquals(1, named.size());
        assertEquals("Yoga Class", named.get(0).getName());
    }

    @Test
    public void testGetEventAvailableReturnsCopy() {
        eventAvailable.addEvent(event1);
        List<Info> copy = eventAvailable.getEventAvailable();

        assertEquals(1, copy.size());
        copy.clear();
        assertEquals(1, eventAvailable.getEventCount()); // ensure original list is unmodified
    }

    @Test
    public void testClearAll() {
        eventAvailable.addEvent(event1);
        eventAvailable.addEvent(event2);
        eventAvailable.clearAll();
        assertEquals(0, eventAvailable.getEventCount());
    }

    @Test
    public void testContainsReturnsTrueIfPresent() {
        eventAvailable.addEvent(event2);
        assertTrue(eventAvailable.contains(event2));
    }

    @Test
    public void testContainsReturnsFalseIfAbsent() {
        assertFalse(eventAvailable.contains(event3));
    }
}

