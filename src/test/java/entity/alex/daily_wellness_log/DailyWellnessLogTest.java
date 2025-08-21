package entity.alex.daily_wellness_log;

import entity.Alex.DailyWellnessLog.DailyWellnessLog;
import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.Type;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DailyWellnessLog.
 */
public class DailyWellnessLogTest {

    private DailyWellnessLog log;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2025, 8, 14);
        log = new DailyWellnessLog(testDate);
    }

    @Test
    void testGetDateReturnsCorrectValue() {
        assertEquals(testDate, log.getDate());
    }

    @Test
    void testGetIdNotNullAndUnique() {
        String id1 = log.getId();
        String id2 = new DailyWellnessLog(LocalDate.of(2025, 8, 15)).getId();
        assertNotNull(id1);
        assertNotEquals(id1, id2, "Each DailyWellnessLog should have a unique ID.");
    }

    @Test
    void testAddEntrySuccess() {
        WellnessLogEntryInterf entry = mock(WellnessLogEntryInterf.class);
        log.addEntry(entry);
        List<WellnessLogEntryInterf> entries = log.getEntries();
        assertEquals(1, entries.size());
        assertTrue(entries.contains(entry));
    }

    @Test
    void testAddEntryNullIgnored() {
        log.addEntry(null);
        assertTrue(log.getEntries().isEmpty());
    }

    @Test
    void testAddEntryDuplicateIgnored() {
        WellnessLogEntryInterf entry = mock(WellnessLogEntryInterf.class);
        log.addEntry(entry);
        log.addEntry(entry);  // duplicate
        assertEquals(1, log.getEntries().size());
    }

    @Test
    void testRemoveEntryByIdSuccess() {
        WellnessLogEntryInterf entry = mock(WellnessLogEntryInterf.class);
        when(entry.getId()).thenReturn("log-1");
        log.addEntry(entry);
        log.removeEntry("log-1");
        assertTrue(log.getEntries().isEmpty());
    }

    @Test
    void testRemoveEntryByIdNotFound() {
        WellnessLogEntryInterf entry = mock(WellnessLogEntryInterf.class);
        when(entry.getId()).thenReturn("log-1");
        log.addEntry(entry);
        log.removeEntry("non-existent");
        assertEquals(1, log.getEntries().size());
    }

    @Test
    void testRemoveEntryNullIdDoesNothing() {
        WellnessLogEntryInterf entry = mock(WellnessLogEntryInterf.class);
        when(entry.getId()).thenReturn("log-1");
        log.addEntry(entry);
        log.removeEntry(null);
        assertEquals(1, log.getEntries().size());
    }

    @Test
    void testGetEntriesReturnsDefensiveCopy() {
        WellnessLogEntryInterf entry = mock(WellnessLogEntryInterf.class);
        log.addEntry(entry);
        List<WellnessLogEntryInterf> entries = log.getEntries();
        entries.clear();  // modify copy
        assertEquals(1, log.getEntries().size(), "Original list should remain unaffected.");
    }

    @Test
    void testIntegrationWithRealEntry() {
        MoodLabel mood = new MoodLabel.Builder("Happy").type(Type.Positive).build();
        WellnessLogEntry realEntry = new WellnessLogEntry.Builder()
                .time(LocalDateTime.of(testDate, LocalDateTime.now().toLocalTime()))
                .moodLabel(mood)
                .energyLevel(Levels.SEVEN)
                .stressLevel(Levels.THREE)
                .fatigueLevel(Levels.FIVE)
                .userNote("Feeling okay")
                .build();

        log.addEntry(realEntry);
        assertEquals(1, log.getEntries().size());
        assertEquals("Happy", log.getEntries().get(0).getMoodLabel().getName());
    }
}
