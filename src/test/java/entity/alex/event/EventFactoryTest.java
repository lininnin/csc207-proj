package entity.alex.event;

import entity.alex.Event.Event;
import entity.alex.Event.EventFactory;
import entity.alex.Event.EventFactoryInterf;
import entity.alex.Event.EventInterf;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.BeginAndDueDates.BeginAndDueDatesInterf;
import entity.info.InfoFactory;
import entity.info.InfoFactoryInterf;
import entity.info.InfoInterf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EventFactoryTest {

    private EventFactoryInterf factory;
    private InfoFactoryInterf infoFactory;

    @BeforeEach
    void setUp() {
        factory = new EventFactory();
        infoFactory = new InfoFactory(); // 使用你上传的 InfoFactory 实现类
    }

    @Test
    void testCreateEventReturnsNonNull() {
        InfoInterf info = infoFactory.create("Test Event", "Test Description", "Test Category");
        BeginAndDueDatesInterf dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(2));

        EventInterf event = factory.createEvent(info, dates);
        assertNotNull(event, "Factory should return non-null EventInterf.");
    }

    @Test
    void testCreateEventReturnsCorrectType() {
        InfoInterf info = infoFactory.create("TypeCheck", "desc", "cat");
        BeginAndDueDatesInterf dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(1));

        EventInterf event = factory.createEvent(info, dates);
        assertTrue(event instanceof Event, "Returned instance should be of type Event.");
    }

    @Test
    void testCreateEventThrowsIfInfoIsNull() {
        BeginAndDueDatesInterf dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(2));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.createEvent(null, dates);
        });
        assertEquals("Info is required", exception.getMessage());
    }

    @Test
    void testCreateEventThrowsIfDatesIsNull() {
        InfoInterf info = infoFactory.create("NoDate", "desc", "cat");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.createEvent(info, null);
        });
        assertEquals("BeginAndDueDates is required", exception.getMessage());
    }

    @Test
    void testEventFieldsMatchInput() {
        String name = "Deadline";
        String desc = "Submit report";
        String cat = "Work";

        InfoInterf info = infoFactory.create(name, desc, cat);
        LocalDate begin = LocalDate.of(2025, 8, 20);
        LocalDate due = LocalDate.of(2025, 8, 25);
        BeginAndDueDatesInterf dates = new BeginAndDueDates(begin, due);

        EventInterf event = factory.createEvent(info, dates);

        assertEquals(name, event.getInfo().getName());
        assertEquals(desc, event.getInfo().getDescription());
        assertEquals(cat, event.getInfo().getCategory());
        assertEquals(begin, event.getBeginAndDueDates().getBeginDate());
        assertEquals(due, event.getBeginAndDueDates().getDueDate());
    }
}
