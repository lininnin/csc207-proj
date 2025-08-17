package entityTest.alex.wellness_log_entry;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.Type;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class WellnessLogEntryTest {

    private WellnessLogEntry entry;
    private LocalDateTime time;
    private MoodLabel mood;

    @BeforeEach
    void setUp() {
        time = LocalDateTime.of(2025, 8, 14, 14, 0);
        mood = new MoodLabel.Builder("Happy")
                .type(Type.Positive)
                .build();

        entry = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(Levels.THREE)
                .energyLevel(Levels.FIVE)
                .fatigueLevel(Levels.SIX)
                .moodLabel(mood)
                .userNote("Felt good")
                .build();
    }

    @Test
    void testBuilderAndGetters() {
        assertNotNull(entry.getId());
        assertEquals(time, entry.getTime());
        assertEquals(Levels.THREE, entry.getStressLevel());
        assertEquals(Levels.FIVE, entry.getEnergyLevel());
        assertEquals(Levels.SIX, entry.getFatigueLevel());
        assertEquals(mood, entry.getMoodLabel());
        assertEquals("Felt good", entry.getUserNote());
    }

    @Test
    void testSetters() {
        entry.setStressLevel(Levels.EIGHT);
        assertEquals(Levels.EIGHT, entry.getStressLevel());

        entry.setEnergyLevel(Levels.TWO);
        assertEquals(Levels.TWO, entry.getEnergyLevel());

        entry.setFatigueLevel(Levels.NINE);
        assertEquals(Levels.NINE, entry.getFatigueLevel());

        MoodLabel newMood = new MoodLabel.Builder("Calm")
                .type(Type.Positive)
                .build();
        entry.setMoodLabel(newMood);
        assertEquals("Calm", entry.getMoodLabel().getName());

        entry.setUserNote("  Recovered well  ");
        assertEquals("Recovered well", entry.getUserNote());
    }

    @Test
    void testSetNullLevelsThrows() {
        assertThrows(IllegalArgumentException.class, () -> entry.setStressLevel(null));
        assertThrows(IllegalArgumentException.class, () -> entry.setEnergyLevel(null));
        assertThrows(IllegalArgumentException.class, () -> entry.setFatigueLevel(null));
    }

    @Test
    void testSetNullMoodThrows() {
        assertThrows(IllegalArgumentException.class, () -> entry.setMoodLabel(null));
    }

    @Test
    void testSetNullNoteIsAllowed() {
        entry.setUserNote(null);
        assertNull(entry.getUserNote());
    }

    @Test
    void testSetEmptyNoteThrows() {
        assertThrows(IllegalArgumentException.class, () -> entry.setUserNote("   "));
    }

    @Test
    void testBuilderFromCopiesAllFields() {
        WellnessLogEntryInterf copy = WellnessLogEntry.Builder.from(entry).build();

        assertNotSame(entry, copy);
        assertEquals(entry.getId(), copy.getId());
        assertEquals(entry.getTime(), copy.getTime());
        assertEquals(entry.getStressLevel(), copy.getStressLevel());
        assertEquals(entry.getEnergyLevel(), copy.getEnergyLevel());
        assertEquals(entry.getFatigueLevel(), copy.getFatigueLevel());
        assertEquals(entry.getMoodLabel(), copy.getMoodLabel());
        assertEquals(entry.getUserNote(), copy.getUserNote());
    }

    @Test
    void testBuildWithoutRequiredFieldsThrows() {
        assertThrows(IllegalStateException.class, () -> new WellnessLogEntry.Builder().build());
    }
}
