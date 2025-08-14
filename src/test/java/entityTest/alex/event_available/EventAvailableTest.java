package entityTest.alex.event_available;

import entity.Alex.EventAvailable.EventAvailableInterf;
import entity.Alex.EventAvailable.EventAvailable;
import entity.info.Info;
import entity.info.InfoInterf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventAvailableTest {

    private EventAvailableInterf eventAvailable;
    private InfoInterf mathExam;
    private InfoInterf csProject;
    private InfoInterf dinner;

    @BeforeEach
    void setUp() {
        eventAvailable = new EventAvailable();

        mathExam = new Info.Builder("Math Exam")
                .description("Final math exam")
                .category("School")
                .build();

        csProject = new Info.Builder("CS Project")
                .description("CSC207 project")
                .category("School")
                .build();

        dinner = new Info.Builder("Dinner")
                .description("Dinner with friends")
                .category("Personal")
                .build();
    }

    @Test
    void testAddAndGetEvent() {
        eventAvailable.addEvent(mathExam);
        eventAvailable.addEvent(csProject);
        List<InfoInterf> all = eventAvailable.getEventAvailable();
        assertEquals(2, all.size());
        assertTrue(all.contains(mathExam));
        assertTrue(all.contains(csProject));
    }

    @Test
    void testAddNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> eventAvailable.addEvent(null));
    }

    @Test
    void testRemoveEventSuccess() {
        eventAvailable.addEvent(csProject);
        assertTrue(eventAvailable.removeEvent(csProject));
        assertFalse(eventAvailable.contains(csProject));
    }

    @Test
    void testRemoveEventFail() {
        assertFalse(eventAvailable.removeEvent(dinner));
    }

    @Test
    void testGetEventsByCategory() {
        eventAvailable.addEvent(csProject);
        eventAvailable.addEvent(mathExam);
        eventAvailable.addEvent(dinner);
        List<InfoInterf> schoolEvents = eventAvailable.getEventsByCategory("School");
        assertEquals(2, schoolEvents.size());
        assertTrue(schoolEvents.contains(mathExam));
        assertTrue(schoolEvents.contains(csProject));
    }

    @Test
    void testGetEventsByName() {
        eventAvailable.addEvent(csProject);
        eventAvailable.addEvent(dinner);
        List<InfoInterf> dinnerEvents = eventAvailable.getEventsByName("Dinner");
        assertEquals(1, dinnerEvents.size());
        assertEquals("Dinner", dinnerEvents.get(0).getName());
    }

    @Test
    void testGetEventCount() {
        assertEquals(0, eventAvailable.getEventCount());
        eventAvailable.addEvent(mathExam);
        assertEquals(1, eventAvailable.getEventCount());
    }

    @Test
    void testContains() {
        eventAvailable.addEvent(dinner);
        assertTrue(eventAvailable.contains(dinner));
        assertFalse(eventAvailable.contains(csProject));
    }

    @Test
    void testClearAll() {
        eventAvailable.addEvent(mathExam);
        eventAvailable.addEvent(csProject);
        eventAvailable.clearAll();
        assertEquals(0, eventAvailable.getEventCount());
        assertFalse(eventAvailable.contains(mathExam));
    }

    @Test
    void testDeepCopyFromGetEventAvailable() {
        eventAvailable.addEvent(mathExam);
        List<InfoInterf> listCopy = eventAvailable.getEventAvailable();
        listCopy.clear();  // 应不影响原始数据
        assertEquals(1, eventAvailable.getEventCount());
    }
}
