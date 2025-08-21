package entity.alex.mood_label;

import entity.Alex.MoodLabel.MoodLabelFactory;
import entity.Alex.MoodLabel.MoodLabelFactoryInterf;
import entity.Alex.MoodLabel.MoodLabelInterf;
import entity.Alex.MoodLabel.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoodLabelFactoryTest {

    private MoodLabelFactoryInterf factory;

    @BeforeEach
    void setUp() {
        factory = new MoodLabelFactory();
    }

    @Test
    void testCreateMoodLabelSuccessfully() {
        MoodLabelInterf label = factory.create("Happy", Type.Positive);
        assertNotNull(label);
        assertEquals("Happy", label.getName());
        assertEquals(Type.Positive, label.getType());
    }

    @Test
    void testCreateMoodLabelTrimsWhitespace() {
        MoodLabelInterf label = factory.create("  Calm  ", Type.Positive);
        assertEquals("Calm", label.getName());
    }

    @Test
    void testCreateMoodLabelWithEmptyNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> factory.create("  ", Type.Negative));
    }

    @Test
    void testCreateMoodLabelWithNullNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, Type.Positive));
    }

    @Test
    void testCreateMoodLabelWithNullTypeThrows() {
        assertThrows(IllegalArgumentException.class, () -> factory.create("Sad", null));
    }
}
