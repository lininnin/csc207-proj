package entityTest.alex.wellness_log_entry;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactory;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactoryInterf;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class WellnessLogEntryFactoryTest {

    @Test
    public void testCreateValidWellnessLogEntry() {
        LocalDateTime time = LocalDateTime.of(2025, 8, 8, 10, 0);
        MoodLabel mood = new MoodLabel.Builder("Relaxed")
                .type(MoodLabel.Type.Positive)
                .build();

        WellnessLogEntryFactoryInterf factory = new WellnessLogEntryFactory();
        WellnessLogEntry entry = factory.create(
                time,
                Levels.THREE,
                Levels.SIX,
                Levels.TWO,
                mood,
                "Morning walk."
        );

        assertNotNull(entry);
        assertEquals(time, entry.getTime());
        assertEquals(Levels.THREE, entry.getStressLevel());
        assertEquals(Levels.SIX, entry.getEnergyLevel());
        assertEquals(Levels.TWO, entry.getFatigueLevel());
        assertEquals("Relaxed", entry.getMoodLabel().getName());
        assertEquals("Morning walk.", entry.getUserNote());
    }

    @Test
    public void testCreateWithNullFieldsThrows() {
        MoodLabel mood = new MoodLabel.Builder("Blank").type(MoodLabel.Type.Negative).build();
        WellnessLogEntryFactory factory = new WellnessLogEntryFactory();

        assertThrows(IllegalStateException.class, () ->
                factory.create(null, Levels.ONE, Levels.ONE, Levels.ONE, mood, "note"));

        assertThrows(IllegalStateException.class, () ->
                factory.create(LocalDateTime.now(), null, Levels.ONE, Levels.ONE, mood, "note"));

        assertThrows(IllegalStateException.class, () ->
                factory.create(LocalDateTime.now(), Levels.ONE, null, Levels.ONE, mood, "note"));

        assertThrows(IllegalStateException.class, () ->
                factory.create(LocalDateTime.now(), Levels.ONE, Levels.ONE, null, mood, "note"));

        assertThrows(IllegalStateException.class, () ->
                factory.create(LocalDateTime.now(), Levels.ONE, Levels.ONE, Levels.ONE, null, "note"));
    }
}

