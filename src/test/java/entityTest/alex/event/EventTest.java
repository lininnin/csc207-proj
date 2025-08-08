package entityTest.alex.event;

import entity.Alex.Event.Event;
import entity.info.Info;
import entity.BeginAndDueDates.BeginAndDueDates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    private Info sampleInfo;
    private BeginAndDueDates validDates;

    @BeforeEach
    public void setUp() {
        sampleInfo = new Info.Builder("Meeting with client")
                .description("Discuss requirements")
                .category("Work")
                .build();

        validDates = new BeginAndDueDates(
                LocalDate.of(2025, 7, 21),
                LocalDate.of(2025, 7, 22)
        );
    }

    @Test
    public void testBuildValidEvent() {
        Event event = new Event.Builder(sampleInfo)
                .beginAndDueDates(validDates)
                .build();

        assertEquals(sampleInfo, event.getInfo());
        assertEquals(validDates, event.getBeginAndDueDates());
    }

    @Test
    public void testBuildEventWithoutBeginAndDueDatesThrows() {
        Event.Builder builder = new Event.Builder(sampleInfo);
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    public void testBuilderWithNullInfoThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Event.Builder(null));
    }

    @Test
    public void testBeginAndDueDatesSetterRejectsNull() {
        Event.Builder builder = new Event.Builder(sampleInfo);
        assertThrows(IllegalArgumentException.class, () -> builder.beginAndDueDates(null));
    }

    @Test
    public void testEditInfoSuccessfully() {
        Event event = createDefaultEvent();
        Info newInfo = new Info.Builder("Updated Meeting")
                .description("Revised details")
                .category("Client")
                .build();

        event.editInfo(newInfo);
        assertEquals(newInfo, event.getInfo());
    }

    @Test
    public void testEditInfoWithNullThrows() {
        Event event = createDefaultEvent();
        assertThrows(IllegalArgumentException.class, () -> event.editInfo(null));
    }

    @Test
    public void testEditDueDateSuccessfully() {
        Event event = createDefaultEvent();
        LocalDate newDueDate = LocalDate.of(2025, 7, 23);
        event.editDueDate(newDueDate);
        assertEquals(newDueDate, event.getBeginAndDueDates().getDueDate());
    }

    @Test
    public void testEditDueDateBeforeBeginDateThrows() {
        Event event = createDefaultEvent();
        LocalDate invalidDue = LocalDate.of(2025, 7, 20); // earlier than begin
        assertThrows(IllegalArgumentException.class, () -> event.editDueDate(invalidDue));
    }

    @Test
    public void testEditDueDateNullThrows() {
        Event event = createDefaultEvent();
        assertThrows(IllegalArgumentException.class, () -> event.editDueDate(null));
    }

    // Helper
    private Event createDefaultEvent() {
        return new Event.Builder(sampleInfo)
                .beginAndDueDates(validDates)
                .build();
    }
}

