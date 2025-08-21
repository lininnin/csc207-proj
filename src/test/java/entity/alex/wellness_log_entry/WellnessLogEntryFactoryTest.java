package entity.alex.wellness_log_entry;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelInterf;
import entity.Alex.MoodLabel.Type;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactory;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactoryInterf;
import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class WellnessLogEntryFactoryTest {

    private WellnessLogEntryFactoryInterf factory;
    private MoodLabelInterf moodLabel;

    @BeforeEach
    void setUp() {
        factory = new WellnessLogEntryFactory();
        moodLabel = new MoodLabel.Builder("Grateful")
                .type(Type.Positive)
                .build();
    }

    @Test
    void testCreateWellnessLogEntrySuccessfully() {
        LocalDateTime time = LocalDateTime.of(2025, 8, 14, 10, 30);
        Levels stress = Levels.THREE;
        Levels energy = Levels.SIX;
        Levels fatigue = Levels.FOUR;
        String note = "Felt more energetic after walk.";

        WellnessLogEntryInterf entry = factory.create(
                time, stress, energy, fatigue, moodLabel, note
        );

        assertEquals(time, entry.getTime());
        assertEquals(stress, entry.getStressLevel());
        assertEquals(energy, entry.getEnergyLevel());
        assertEquals(fatigue, entry.getFatigueLevel());
        assertEquals(moodLabel, entry.getMoodLabel());
        assertEquals(note, entry.getUserNote());
    }

    @Test
    void testCreateWithNullNoteIsAllowed() {
        LocalDateTime time = LocalDateTime.now();
        WellnessLogEntryInterf entry = factory.create(
                time,
                Levels.FIVE,
                Levels.FIVE,
                Levels.FIVE,
                moodLabel,
                null
        );

        assertNull(entry.getUserNote());
    }

    @Test
    void testCreateWithEmptyNoteIsAllowed() {
        LocalDateTime time = LocalDateTime.now();
        WellnessLogEntryInterf entry = factory.create(
                time,
                Levels.SEVEN,
                Levels.TWO,
                Levels.SIX,
                moodLabel,
                ""
        );

        assertEquals("", entry.getUserNote());
    }
}
