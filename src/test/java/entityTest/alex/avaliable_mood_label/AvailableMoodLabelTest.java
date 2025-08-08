package entityTest.alex.avaliable_mood_label;

import entity.Alex.AvalibleMoodLabel.AvaliableMoodLabel;
import entity.Alex.MoodLabel.MoodLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AvailableMoodLabelTest {

    private AvaliableMoodLabel labelStore;

    @BeforeEach
    public void setUp() {
        labelStore = new AvaliableMoodLabel();
    }

    @Test
    public void testAddPositiveLabel() {
        MoodLabel happy = new MoodLabel.Builder("Happy").type(MoodLabel.Type.Positive).build();
        labelStore.addLabel(happy);

        List<String> positiveNames = labelStore.getPositiveLabels();
        assertEquals(1, positiveNames.size());
        assertTrue(positiveNames.contains("Happy"));

        assertTrue(labelStore.getNegativeLabels().isEmpty());
    }

    @Test
    public void testAddNegativeLabel() {
        MoodLabel sad = new MoodLabel.Builder("Sad").type(MoodLabel.Type.Negative).build();
        labelStore.addLabel(sad);

        List<String> negativeNames = labelStore.getNegativeLabels();
        assertEquals(1, negativeNames.size());
        assertTrue(negativeNames.contains("Sad"));

        assertTrue(labelStore.getPositiveLabels().isEmpty());
    }

    @Test
    public void testAddDuplicateLabelName() {
        MoodLabel relaxed = new MoodLabel.Builder("Relaxed").type(MoodLabel.Type.Positive).build();
        MoodLabel duplicate = new MoodLabel.Builder("Relaxed").type(MoodLabel.Type.Negative).build();

        labelStore.addLabel(relaxed);
        labelStore.addLabel(duplicate);  // 应该被忽略

        List<String> positiveNames = labelStore.getPositiveLabels();
        List<String> negativeNames = labelStore.getNegativeLabels();

        assertEquals(1, positiveNames.size());
        assertEquals(0, negativeNames.size());
    }

    @Test
    public void testRemoveLabelByName() {
        MoodLabel cheerful = new MoodLabel.Builder("Cheerful").type(MoodLabel.Type.Positive).build();
        MoodLabel upset = new MoodLabel.Builder("Upset").type(MoodLabel.Type.Negative).build();

        labelStore.addLabel(cheerful);
        labelStore.addLabel(upset);

        labelStore.removeLabelByName("Cheerful");
        assertFalse(labelStore.getPositiveLabels().contains("Cheerful"));
        assertTrue(labelStore.getNegativeLabels().contains("Upset"));
    }

    @Test
    public void testClear() {
        labelStore.addLabel(new MoodLabel.Builder("Grateful").type(MoodLabel.Type.Positive).build());
        labelStore.addLabel(new MoodLabel.Builder("Anxious").type(MoodLabel.Type.Negative).build());

        labelStore.clear();

        assertTrue(labelStore.getPositiveLabels().isEmpty());
        assertTrue(labelStore.getNegativeLabels().isEmpty());
    }

    @Test
    public void testAddNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> labelStore.addLabel(null));
    }
}

