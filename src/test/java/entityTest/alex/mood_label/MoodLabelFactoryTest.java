package entityTest.alex.mood_label;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelFactory;
import entity.Alex.MoodLabel.MoodLabelInterf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoodLabelFactoryTest {

    @Test
    public void testCreateValidMoodLabel() {
        MoodLabelFactory factory = new MoodLabelFactory();

        MoodLabelInterf mood = factory.create("Joyful", MoodLabel.Type.Positive);

        assertNotNull(mood);
        assertTrue(mood instanceof MoodLabel);
        assertEquals("Joyful", mood.getName());
        assertEquals(MoodLabel.Type.Positive, mood.getType());
    }

    @Test
    public void testCreateWithNullNameThrowsException() {
        MoodLabelFactory factory = new MoodLabelFactory();

        assertThrows(IllegalArgumentException.class, () -> {
            factory.create(null, MoodLabel.Type.Negative);
        });
    }

    @Test
    public void testCreateWithEmptyNameThrowsException() {
        MoodLabelFactory factory = new MoodLabelFactory();

        assertThrows(IllegalArgumentException.class, () -> {
            factory.create("   ", MoodLabel.Type.Positive);
        });
    }

    @Test
    public void testCreateWithNullTypeThrowsException() {
        MoodLabelFactory factory = new MoodLabelFactory();

        assertThrows(IllegalArgumentException.class, () -> {
            factory.create("Worried", null);
        });
    }
}

