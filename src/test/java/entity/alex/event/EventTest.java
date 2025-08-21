package entity.alex.event;

import entity.alex.Event.Event;
import entity.alex.Event.EventInterf;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.BeginAndDueDates.BeginAndDueDatesInterf;
import entity.info.Info;
import entity.info.InfoInterf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    private InfoInterf initialInfo;
    private BeginAndDueDatesInterf initialDates;
    private LocalDate beginDate;
    private LocalDate dueDate;

    @BeforeEach
    void setUp() {
        beginDate = LocalDate.of(2025, 8, 15);
        dueDate = LocalDate.of(2025, 8, 20);
        initialInfo = new Info.Builder("Task")
                .description("Description")
                .category("Category")
                .build();
        initialDates = new BeginAndDueDates(beginDate, dueDate);
    }

    @Test
    void testBuilderCreatesValidEvent() {
        EventInterf event = new Event.Builder(initialInfo)
                .beginAndDueDates(initialDates)
                .oneTime(true)
                .build();

        assertNotNull(event);
        assertEquals(initialInfo, event.getInfo());
        assertEquals(initialDates, event.getBeginAndDueDates());
        assertTrue(((Event) event).isOneTime()); // cast to access concrete method
    }

    @Test
    void testEditInfoWorks() {
        EventInterf event = new Event.Builder(initialInfo)
                .beginAndDueDates(initialDates)
                .build();

        InfoInterf newInfo = new Info.Builder("NewName")
                .description("NewDesc")
                .category("NewCat")
                .build();
        event.editInfo(newInfo);

        assertEquals("NewName", event.getInfo().getName());
    }

    @Test
    void testEditInfoThrowsIfNull() {
        EventInterf event = new Event.Builder(initialInfo)
                .beginAndDueDates(initialDates)
                .build();

        assertThrows(IllegalArgumentException.class, () -> event.editInfo(null));
    }

    @Test
    void testEditDueDateSuccessfully() {
        EventInterf event = new Event.Builder(initialInfo)
                .beginAndDueDates(initialDates)
                .build();

        LocalDate newDue = dueDate.plusDays(2);
        event.editDueDate(newDue);

        assertEquals(newDue, event.getBeginAndDueDates().getDueDate());
    }

    @Test
    void testEditDueDateThrowsIfNull() {
        EventInterf event = new Event.Builder(initialInfo)
                .beginAndDueDates(initialDates)
                .build();

        assertThrows(IllegalArgumentException.class, () -> event.editDueDate(null));
    }

    @Test
    void testEditDueDateThrowsIfBeforeBeginDate() {
        EventInterf event = new Event.Builder(initialInfo)
                .beginAndDueDates(initialDates)
                .build();

        LocalDate earlier = beginDate.minusDays(1);
        assertThrows(IllegalArgumentException.class, () -> event.editDueDate(earlier));
    }

    @Test
    void testSetOneTimeUpdatesFlag() {
        Event event = new Event.Builder(initialInfo)
                .beginAndDueDates(initialDates)
                .build();

        assertFalse(event.isOneTime());
        event.setOneTime(true);
        assertTrue(event.isOneTime());
    }

    @Test
    void testBuilderThrowsIfInfoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Event.Builder(null));
    }

    @Test
    void testBuilderThrowsIfBeginAndDueDatesIsNull() {
        Event.Builder builder = new Event.Builder(initialInfo);
        assertThrows(IllegalArgumentException.class, () -> builder.beginAndDueDates(null));
    }

    @Test
    void testBuildThrowsIfBeginAndDueDatesNotSet() {
        Event.Builder builder = new Event.Builder(initialInfo);
        assertThrows(IllegalStateException.class, builder::build);
    }
}
