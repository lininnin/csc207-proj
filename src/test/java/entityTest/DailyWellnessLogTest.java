package entityTest;

import entity.DailyWellnessLog;
import entity.WellnessLogEntry;
import entity.MoodLabel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DailyWellnessLogTest {

    private DailyWellnessLog log;
    private WellnessLogEntry entry1;
    private WellnessLogEntry entry2;

    @BeforeEach
    public void setUp() {
        log = new DailyWellnessLog(LocalDate.of(2025, 7, 21));

        entry1 = new WellnessLogEntry.Builder()
                .time(LocalDateTime.of(2025, 7, 21, 9, 0))
                .stressLevel(4)
                .energyLevel(6)
                .fatigueLevel(3)
                .moodLabel(new MoodLabel.Builder("Focused").type(MoodLabel.Type.Positive).build())
                .userNote("Morning work")
                .build();

        entry2 = new WellnessLogEntry.Builder()
                .time(LocalDateTime.of(2025, 7, 21, 18, 0))
                .stressLevel(7)
                .energyLevel(3)
                .fatigueLevel(6)
                .moodLabel(new MoodLabel.Builder("Tired").type(MoodLabel.Type.Negative).build())
                .userNote("Long day")
                .build();
    }



    @Test
    public void testConstructorInitializesFields() {
        assertEquals(LocalDate.of(2025, 7, 21), log.getDate());
        assertNotNull(log.getId());
        assertTrue(log.getEntries().isEmpty());
    }

    @Test
    public void testAddEntrySuccessfully() {
        log.addEntry(entry1);
        List<WellnessLogEntry> entries = log.getEntries();

        assertEquals(1, entries.size());
        assertTrue(entries.contains(entry1));
    }

    @Test
    public void testAddDuplicateEntryNotAddedAgain() {
        log.addEntry(entry1);
        log.addEntry(entry1);
        assertEquals(1, log.getEntries().size());
    }

    @Test
    public void testAddNullEntryIgnored() {
        log.addEntry(null);
        assertTrue(log.getEntries().isEmpty());
    }

    @Test
    public void testRemoveEntryByIdSuccessfully() {
        log.addEntry(entry1);
        log.addEntry(entry2);

        log.removeEntry(entry1.getId());

        List<WellnessLogEntry> remaining = log.getEntries();
        assertEquals(1, remaining.size());
        assertFalse(remaining.contains(entry1));
        assertTrue(remaining.contains(entry2));
    }

    @Test
    public void testRemoveEntryWithInvalidIdDoesNothing() {
        log.addEntry(entry1);
        log.removeEntry("non-existent-id");
        assertEquals(1, log.getEntries().size());
    }

    @Test
    public void testRemoveEntryWithNullIdDoesNothing() {
        log.addEntry(entry1);
        log.removeEntry(null);
        assertEquals(1, log.getEntries().size());
    }

    @Test
    public void testGetEntriesReturnsDefensiveCopy() {
        log.addEntry(entry1);
        List<WellnessLogEntry> entries = log.getEntries();
        entries.clear(); // modify returned list

        assertEquals(1, log.getEntries().size()); // original list remains unchanged
    }
}

