package entityTest.alex.daily_wellness_log;

import entity.Alex.DailyWellnessLog.DailyWellnessLog;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Alex.MoodLabel.MoodLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DailyWellnessLogTest {

    private DailyWellnessLog dailyLog;
    private WellnessLogEntry entry;

    @BeforeEach
    public void setUp() {
        dailyLog = new DailyWellnessLog(LocalDate.of(2025, 8, 8));

        MoodLabel mood = new MoodLabel.Builder("Happy")
                .type(MoodLabel.Type.Positive)
                .build();

        entry = new WellnessLogEntry.Builder()
                .time(LocalDateTime.of(2025, 8, 8, 8, 0))
                .stressLevel(Levels.THREE)
                .energyLevel(Levels.EIGHT)
                .fatigueLevel(Levels.TWO)
                .moodLabel(mood)
                .userNote("Feeling good today.")
                .build();
    }

    @Test
    public void testAddEntry() {
        dailyLog.addEntry(entry);
        List<WellnessLogEntry> entries = dailyLog.getEntries();

        assertEquals(1, entries.size());
        assertEquals(entry.getId(), entries.get(0).getId());
    }

    @Test
    public void testAddDuplicateEntryIgnored() {
        dailyLog.addEntry(entry);
        dailyLog.addEntry(entry);
        assertEquals(1, dailyLog.getEntries().size());
    }

    @Test
    public void testAddNullEntryIgnored() {
        dailyLog.addEntry(null);
        assertTrue(dailyLog.getEntries().isEmpty());
    }

    @Test
    public void testRemoveEntryById() {
        dailyLog.addEntry(entry);
        dailyLog.removeEntry(entry.getId());

        assertTrue(dailyLog.getEntries().isEmpty());
    }

    @Test
    public void testRemoveInvalidIdDoesNothing() {
        dailyLog.addEntry(entry);
        dailyLog.removeEntry("non-existent-id");

        assertEquals(1, dailyLog.getEntries().size());
    }

    @Test
    public void testGetDate() {
        assertEquals(LocalDate.of(2025, 8, 8), dailyLog.getDate());
    }

    @Test
    public void testGetIdIsNotNull() {
        assertNotNull(dailyLog.getId());
    }

    @Test
    public void testGetEntriesReturnsCopy() {
        dailyLog.addEntry(entry);
        List<WellnessLogEntry> copy = dailyLog.getEntries();
        copy.clear();

        assertEquals(1, dailyLog.getEntries().size());
    }
}
