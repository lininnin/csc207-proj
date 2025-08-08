package entityTest.alex.event;

import entity.Alex.Event.Event;
import entity.Alex.Event.EventFactory;
import entity.Alex.Event.EventFactoryInterf;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EventFactoryTest {

    @Test
    public void testCreateEventSuccessfully() {
        Info info = new Info.Builder("Project Presentation")
                .description("Final group presentation")
                .category("Academic")
                .build();

        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.of(2025, 8, 10), LocalDate.of(2025, 8, 11));

        EventFactoryInterf factory = new EventFactory();
        Event event = factory.createEvent(info, dates);

        assertNotNull(event);
        assertEquals(info.getId(), event.getInfo().getId());
        assertEquals(dates.getBeginDate(), event.getBeginAndDueDates().getBeginDate());
    }

    @Test
    public void testCreateEventWithNullInfoThrowsException() {
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 2));
        EventFactory factory = new EventFactory();

        assertThrows(IllegalArgumentException.class, () -> factory.createEvent(null, dates));
    }

    @Test
    public void testCreateEventWithNullDatesThrowsException() {
        Info info = new Info.Builder("Task A").category("Work").build();
        EventFactory factory = new EventFactory();

        assertThrows(IllegalArgumentException.class, () -> factory.createEvent(info, null));
    }
}

