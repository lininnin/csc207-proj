package entity.alex.mood_label;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.Type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoodLabelTest {

    @Test
    void testBuilderCreatesValidMoodLabel() {
        MoodLabel label = new MoodLabel.Builder("Happy")
                .type(Type.Positive)
                .build();

        assertEquals("Happy", label.getName());
        assertEquals(Type.Positive, label.getType());
    }

    @Test
    void testBuilderTrimsName() {
        MoodLabel label = new MoodLabel.Builder("  Calm  ")
                .type(Type.Positive)
                .build();

        assertEquals("Calm", label.getName());
    }

    @Test
    void testBuilderWithNullNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new MoodLabel.Builder(null));
    }

    @Test
    void testBuilderWithEmptyNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new MoodLabel.Builder("  "));
    }

    @Test
    void testBuilderWithNullTypeThrows() {
        MoodLabel.Builder builder = new MoodLabel.Builder("Anxious");
        assertThrows(IllegalArgumentException.class, () -> builder.type(null));
    }

    @Test
    void testSetNameSuccessfully() {
        MoodLabel label = new MoodLabel.Builder("Sad")
                .type(Type.Negative)
                .build();

        label.setName("Tired");
        assertEquals("Tired", label.getName());
    }

    @Test
    void testSetNameWithNullThrows() {
        MoodLabel label = new MoodLabel.Builder("Sad")
                .type(Type.Negative)
                .build();

        assertThrows(IllegalArgumentException.class, () -> label.setName(null));
    }

    @Test
    void testSetNameWithEmptyThrows() {
        MoodLabel label = new MoodLabel.Builder("Sad")
                .type(Type.Negative)
                .build();

        assertThrows(IllegalArgumentException.class, () -> label.setName("   "));
    }

    @Test
    void testSetTypeSuccessfully() {
        MoodLabel label = new MoodLabel.Builder("Bored")
                .type(Type.Negative)
                .build();

        label.setType(Type.Positive);
        assertEquals(Type.Positive, label.getType());
    }

    @Test
    void testSetTypeWithNullThrows() {
        MoodLabel label = new MoodLabel.Builder("Bored")
                .type(Type.Negative)
                .build();

        assertThrows(IllegalArgumentException.class, () -> label.setType(null));
    }

    @Test
    void testToStringReturnsName() {
        MoodLabel label = new MoodLabel.Builder("Excited")
                .type(Type.Positive)
                .build();

        assertEquals("Excited", label.toString());
    }
}
