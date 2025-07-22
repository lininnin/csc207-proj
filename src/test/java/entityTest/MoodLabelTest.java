package entityTest;

import entity.MoodLabel;
import entity.MoodLabel.Type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoodLabelTest {

    @Test
    public void testBuildMoodLabelSuccessfully() {
        MoodLabel mood = new MoodLabel.Builder("Happy")
                .type(Type.Positive)
                .build();

        assertEquals("Happy", mood.getName());
        assertEquals(Type.Positive, mood.getType());
    }

    @Test
    public void testBuilderTrimsName() {
        MoodLabel mood = new MoodLabel.Builder("  Calm  ")
                .type(Type.Positive)
                .build();

        assertEquals("Calm", mood.getName());
    }

    @Test
    public void testBuilderRejectsNullOrEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new MoodLabel.Builder(null));
        assertThrows(IllegalArgumentException.class, () -> new MoodLabel.Builder("   "));
    }

    @Test
    public void testBuilderRejectsNullTypeInSetter() {
        MoodLabel.Builder builder = new MoodLabel.Builder("Relaxed");
        assertThrows(IllegalArgumentException.class, () -> builder.type(null));
    }

    @Test
    public void testSetNameSuccessfully() {
        MoodLabel mood = new MoodLabel.Builder("Tense")
                .type(Type.Negative)
                .build();

        mood.setName("Anxious");
        assertEquals("Anxious", mood.getName());
    }

    @Test
    public void testSetNameTrimsWhitespace() {
        MoodLabel mood = new MoodLabel.Builder("Tired")
                .type(Type.Negative)
                .build();

        mood.setName("  Drained ");
        assertEquals("Drained", mood.getName());
    }

    @Test
    public void testSetNameRejectsNullOrEmpty() {
        MoodLabel mood = new MoodLabel.Builder("Sleepy")
                .type(Type.Negative)
                .build();

        assertThrows(IllegalArgumentException.class, () -> mood.setName(null));
        assertThrows(IllegalArgumentException.class, () -> mood.setName("   "));
    }

    @Test
    public void testSetTypeSuccessfully() {
        MoodLabel mood = new MoodLabel.Builder("Hopeful")
                .type(Type.Positive)
                .build();

        mood.setType(Type.Negative);
        assertEquals(Type.Negative, mood.getType());
    }

    @Test
    public void testSetTypeRejectsNull() {
        MoodLabel mood = new MoodLabel.Builder("Confused")
                .type(Type.Negative)
                .build();

        assertThrows(IllegalArgumentException.class, () -> mood.setType(null));
    }
}

