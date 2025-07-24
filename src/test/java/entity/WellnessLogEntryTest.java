package entityTest;

import entity.Alex.MoodLabel.MoodLabel;
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
                .stressLevel(5)
                .energyLevel(7)
                .fatigueLevel(3)
                .moodLabel(positiveMood)
                .userNote("Feeling good.")
                .build();

        assertNotNull(entry.getId());
        assertEquals(time, entry.getTime());
        assertEquals(5, entry.getStressLevel());
        assertEquals(7, entry.getEnergyLevel());
        assertEquals(3, entry.getFatigueLevel());
        assertEquals(positiveMood, entry.getMoodLabel());
        assertEquals("Feeling good.", entry.getUserNote());
    }

    @Test
    public void testBuildWithoutOptionalUserNote() {
        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(6)
                .energyLevel(6)
                .fatigueLevel(6)
                .moodLabel(positiveMood)
                .build();

        assertNull(entry.getUserNote());
    }

    @Test
    public void testBuildRejectsMissingTime() {
        WellnessLogEntry.Builder builder = new WellnessLogEntry.Builder()
                .stressLevel(5)
                .energyLevel(5)
                .fatigueLevel(5)
                .moodLabel(positiveMood);

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    public void testBuildRejectsMissingMoodLabel() {
        WellnessLogEntry.Builder builder = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(5)
                .energyLevel(5)
                .fatigueLevel(5);

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    public void testInvalidStressLevelThrowsInBuilder() {
        assertThrows(IllegalArgumentException.class, () ->
                new WellnessLogEntry.Builder().stressLevel(0));
        assertThrows(IllegalArgumentException.class, () ->
                new WellnessLogEntry.Builder().stressLevel(11));
    }

    @Test
    public void testInvalidEnergyLevelThrowsInBuilder() {
        assertThrows(IllegalArgumentException.class, () ->
                new WellnessLogEntry.Builder().energyLevel(0));
        assertThrows(IllegalArgumentException.class, () ->
                new WellnessLogEntry.Builder().energyLevel(15));
    }

    @Test
    public void testInvalidFatigueLevelThrowsInBuilder() {
        assertThrows(IllegalArgumentException.class, () ->
                new WellnessLogEntry.Builder().fatigueLevel(-1));
    }

    @Test
    public void testSettersWorkCorrectly() {
        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(5)
                .energyLevel(5)
                .fatigueLevel(5)
                .moodLabel(positiveMood)
                .build();

        entry.setStressLevel(8);
        entry.setEnergyLevel(9);
        entry.setFatigueLevel(2);
        entry.setUserNote("Updated note");
        entry.setMoodLabel(new MoodLabel.Builder("Anxious")
                .type(MoodLabel.Type.Negative)
                .build());

        assertEquals(8, entry.getStressLevel());
        assertEquals(9, entry.getEnergyLevel());
        assertEquals(2, entry.getFatigueLevel());
        assertEquals("Updated note", entry.getUserNote());
        assertEquals("Anxious", entry.getMoodLabel().getName());
    }

    @Test
    public void testSetUserNoteNullRemovesNote() {
        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(3)
                .energyLevel(4)
                .fatigueLevel(5)
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
                .stressLevel(5)
                .energyLevel(5)
                .fatigueLevel(5)
                .moodLabel(positiveMood)
                .build();

        assertThrows(IllegalArgumentException.class, () -> entry.setUserNote("   "));
    }

    @Test
    public void testSetNullMoodLabelThrows() {
        WellnessLogEntry entry = new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(5)
                .energyLevel(5)
                .fatigueLevel(5)
                .moodLabel(positiveMood)
                .build();

        assertThrows(IllegalArgumentException.class, () -> entry.setMoodLabel(null));
    }

    @Test
    public void testSetInvalidStressLevelThrows() {
        WellnessLogEntry entry = createDefaultEntry();
        assertThrows(IllegalArgumentException.class, () -> entry.setStressLevel(0));
    }

    @Test
    public void testSetInvalidEnergyLevelThrows() {
        WellnessLogEntry entry = createDefaultEntry();
        assertThrows(IllegalArgumentException.class, () -> entry.setEnergyLevel(100));
    }

    @Test
    public void testSetInvalidFatigueLevelThrows() {
        WellnessLogEntry entry = createDefaultEntry();
        assertThrows(IllegalArgumentException.class, () -> entry.setFatigueLevel(-5));
    }

    private WellnessLogEntry createDefaultEntry() {
        return new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(5)
                .energyLevel(5)
                .fatigueLevel(5)
                .moodLabel(positiveMood)
                .build();
    }
}

