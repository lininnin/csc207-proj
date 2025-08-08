package entityTest.alex.wellness_log_entry;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class WellnessLogEntryTest {

    @Test
    public void testBuilderCreatesValidEntry() {
        MoodLabel mood = new MoodLabel.Builder("Calm")
                .type(MoodLabel.Type.Positive)
                .build();

        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(LocalDateTime.of(2025, 8, 8, 9, 0))
                .stressLevel(Levels.THREE)
                .energyLevel(Levels.SEVEN)
                .fatigueLevel(Levels.TWO)
                .moodLabel(mood)
                .userNote("Refreshed and ready.")
                .build();

        assertNotNull(entry.getId());
        assertEquals(Levels.THREE, entry.getStressLevel());
        assertEquals(Levels.SEVEN, entry.getEnergyLevel());
        assertEquals(Levels.TWO, entry.getFatigueLevel());
        assertEquals("Calm", entry.getMoodLabel().getName());
        assertEquals("Refreshed and ready.", entry.getUserNote());
    }

    @Test
    public void testBuilderRejectsMissingRequiredFields() {
        WellnessLogEntry.Builder builder = new WellnessLogEntry.Builder();

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertEquals("All required fields must be set.", ex.getMessage());
    }

    @Test
    public void testSettersUpdateValuesCorrectly() {
        MoodLabel mood = new MoodLabel.Builder("Happy")
                .type(MoodLabel.Type.Positive)
                .build();

        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(LocalDateTime.now())
                .stressLevel(Levels.FOUR)
                .energyLevel(Levels.FOUR)
                .fatigueLevel(Levels.FOUR)
                .moodLabel(mood)
                .userNote("initial")
                .build();

        entry.setEnergyLevel(Levels.SIX);
        entry.setFatigueLevel(Levels.ONE);
        entry.setStressLevel(Levels.TWO);
        entry.setUserNote("  updated note  ");

        assertEquals(Levels.SIX, entry.getEnergyLevel());
        assertEquals(Levels.ONE, entry.getFatigueLevel());
        assertEquals(Levels.TWO, entry.getStressLevel());
        assertEquals("updated note", entry.getUserNote());
    }

    @Test
    public void testSettersThrowOnNull() {
        MoodLabel mood = new MoodLabel.Builder("Neutral").type(MoodLabel.Type.Negative).build();
        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(LocalDateTime.now())
                .stressLevel(Levels.ONE)
                .energyLevel(Levels.TWO)
                .fatigueLevel(Levels.THREE)
                .moodLabel(mood)
                .userNote("entry")
                .build();

        assertThrows(IllegalArgumentException.class, () -> entry.setStressLevel(null));
        assertThrows(IllegalArgumentException.class, () -> entry.setEnergyLevel(null));
        assertThrows(IllegalArgumentException.class, () -> entry.setFatigueLevel(null));
        assertThrows(IllegalArgumentException.class, () -> entry.setMoodLabel(null));
    }

    @Test
    public void testSetUserNoteNullAndBlank() {
        MoodLabel mood = new MoodLabel.Builder("Focused").type(MoodLabel.Type.Positive).build();
        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(LocalDateTime.now())
                .stressLevel(Levels.TWO)
                .energyLevel(Levels.TWO)
                .fatigueLevel(Levels.TWO)
                .moodLabel(mood)
                .userNote("valid note")
                .build();

        entry.setUserNote(null);
        assertNull(entry.getUserNote());

        assertThrows(IllegalArgumentException.class, () -> entry.setUserNote("   "));
    }

    @Test
    public void testBuilderFromCopiesCorrectly() {
        MoodLabel mood = new MoodLabel.Builder("Tired").type(MoodLabel.Type.Negative).build();
        WellnessLogEntry original = new WellnessLogEntry.Builder()
                .time(LocalDateTime.of(2025, 8, 8, 20, 0))
                .stressLevel(Levels.EIGHT)
                .energyLevel(Levels.THREE)
                .fatigueLevel(Levels.NINE)
                .moodLabel(mood)
                .userNote("late night")
                .build();

        WellnessLogEntry copy = WellnessLogEntry.Builder
                .from(original)
                .userNote("copied and changed")
                .build();

        assertEquals(original.getTime(), copy.getTime());
        assertEquals("copied and changed", copy.getUserNote());
        assertEquals(original.getMoodLabel(), copy.getMoodLabel());
    }
}

