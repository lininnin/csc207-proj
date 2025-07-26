package entityTest;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class WellnessLogEntryTest {

    private MoodLabel positiveMood;
    private LocalDateTime time;

    @BeforeEach
    public void setUp() {
        time = LocalDateTime.of(2025, 7, 21, 10, 0);
        positiveMood = new MoodLabel.Builder("Calm")
                .type(MoodLabel.Type.Positive)
                .build();
    }

    @Test
    public void testBuildValidEntry() {
        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(Levels.FIVE)
                .energyLevel(Levels.SEVEN)
                .fatigueLevel(Levels.THREE)
                .moodLabel(positiveMood)
                .userNote("Feeling good.")
                .build();

        assertNotNull(entry.getId());
        assertEquals(time, entry.getTime());
        assertEquals(Levels.FIVE, entry.getStressLevel());
        assertEquals(Levels.SEVEN, entry.getEnergyLevel());
        assertEquals(Levels.THREE, entry.getFatigueLevel());
        assertEquals(positiveMood, entry.getMoodLabel());
        assertEquals("Feeling good.", entry.getUserNote());
    }

    @Test
    public void testBuildWithoutOptionalUserNote() {
        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(Levels.SIX)
                .energyLevel(Levels.SIX)
                .fatigueLevel(Levels.SIX)
                .moodLabel(positiveMood)
                .build();

        assertNull(entry.getUserNote());
    }

    @Test
    public void testBuildRejectsMissingTime() {
        WellnessLogEntry.Builder builder = new WellnessLogEntry.Builder()
                .stressLevel(Levels.FIVE)
                .energyLevel(Levels.FIVE)
                .fatigueLevel(Levels.FIVE)
                .moodLabel(positiveMood);

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    public void testBuildRejectsMissingMoodLabel() {
        WellnessLogEntry.Builder builder = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(Levels.FIVE)
                .energyLevel(Levels.FIVE)
                .fatigueLevel(Levels.FIVE);

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    public void testNullStressLevelThrowsInBuilder() {
        assertThrows(IllegalArgumentException.class, () ->
                new WellnessLogEntry.Builder().stressLevel(null));
    }

    @Test
    public void testNullEnergyLevelThrowsInBuilder() {
        assertThrows(IllegalArgumentException.class, () ->
                new WellnessLogEntry.Builder().energyLevel(null));
    }

    @Test
    public void testNullFatigueLevelThrowsInBuilder() {
        assertThrows(IllegalArgumentException.class, () ->
                new WellnessLogEntry.Builder().fatigueLevel(null));
    }

    @Test
    public void testSettersWorkCorrectly() {
        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(Levels.FIVE)
                .energyLevel(Levels.FIVE)
                .fatigueLevel(Levels.FIVE)
                .moodLabel(positiveMood)
                .build();

        entry.setStressLevel(Levels.EIGHT);
        entry.setEnergyLevel(Levels.NINE);
        entry.setFatigueLevel(Levels.TWO);
        entry.setUserNote("Updated note");
        entry.setMoodLabel(new MoodLabel.Builder("Anxious")
                .type(MoodLabel.Type.Negative)
                .build());

        assertEquals(Levels.EIGHT, entry.getStressLevel());
        assertEquals(Levels.NINE, entry.getEnergyLevel());
        assertEquals(Levels.TWO, entry.getFatigueLevel());
        assertEquals("Updated note", entry.getUserNote());
        assertEquals("Anxious", entry.getMoodLabel().getName());
    }

    @Test
    public void testSetUserNoteNullRemovesNote() {
        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(Levels.THREE)
                .energyLevel(Levels.FOUR)
                .fatigueLevel(Levels.FIVE)
                .moodLabel(positiveMood)
                .userNote("Initial note")
                .build();

        entry.setUserNote(null);
        assertNull(entry.getUserNote());
    }

    @Test
    public void testSetUserNoteBlankThrows() {
        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(Levels.FIVE)
                .energyLevel(Levels.FIVE)
                .fatigueLevel(Levels.FIVE)
                .moodLabel(positiveMood)
                .build();

        assertThrows(IllegalArgumentException.class, () -> entry.setUserNote("   "));
    }

    @Test
    public void testSetNullMoodLabelThrows() {
        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(Levels.FIVE)
                .energyLevel(Levels.FIVE)
                .fatigueLevel(Levels.FIVE)
                .moodLabel(positiveMood)
                .build();

        assertThrows(IllegalArgumentException.class, () -> entry.setMoodLabel(null));
    }

    @Test
    public void testSetNullStressLevelThrows() {
        WellnessLogEntry entry = createDefaultEntry();
        assertThrows(IllegalArgumentException.class, () -> entry.setStressLevel(null));
    }

    @Test
    public void testSetNullEnergyLevelThrows() {
        WellnessLogEntry entry = createDefaultEntry();
        assertThrows(IllegalArgumentException.class, () -> entry.setEnergyLevel(null));
    }

    @Test
    public void testSetNullFatigueLevelThrows() {
        WellnessLogEntry entry = createDefaultEntry();
        assertThrows(IllegalArgumentException.class, () -> entry.setFatigueLevel(null));
    }

    private WellnessLogEntry createDefaultEntry() {
        return new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(Levels.FIVE)
                .energyLevel(Levels.FIVE)
                .fatigueLevel(Levels.FIVE)
                .moodLabel(positiveMood)
                .build();
    }
}


