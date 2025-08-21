package entity.alex.avaliable_mood_label;

import entity.Alex.AvalibleMoodLabel.AvaliableMoodLabel;
import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.Type;
import entity.Alex.MoodLabel.MoodLabelInterf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AvailableMoodLabelTest {

    private AvaliableMoodLabel labelStore;

    @BeforeEach
    void setUp() {
        labelStore = new AvaliableMoodLabel();
    }

    @Test
    void testAddPositiveLabel() {
        MoodLabelInterf happy = new MoodLabel.Builder("Happy").type(Type.Positive).build();
        labelStore.addLabel(happy);

        List<String> positives = labelStore.getPositiveLabels();
        assertEquals(1, positives.size());
        assertTrue(positives.contains("Happy"));
    }

    @Test
    void testAddNegativeLabel() {
        MoodLabelInterf sad = new MoodLabel.Builder("Sad").type(Type.Negative).build();
        labelStore.addLabel(sad);

        List<String> negatives = labelStore.getNegativeLabels();
        assertEquals(1, negatives.size());
        assertTrue(negatives.contains("Sad"));
    }

    @Test
    void testAddNullLabelThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> labelStore.addLabel(null));
    }

    @Test
    void testAvoidDuplicateByName() {
        MoodLabelInterf calm1 = new MoodLabel.Builder("Calm").type(Type.Positive).build();
        MoodLabelInterf calm2 = new MoodLabel.Builder("Calm").type(Type.Negative).build(); // conflicting type

        labelStore.addLabel(calm1);
        labelStore.addLabel(calm2); // should be ignored

        assertEquals(1, labelStore.getPositiveLabels().size());
        assertEquals(0, labelStore.getNegativeLabels().size());
    }

    @Test
    void testRemoveLabelByName() {
        MoodLabelInterf anxious = new MoodLabel.Builder("Anxious").type(Type.Negative).build();
        labelStore.addLabel(anxious);

        assertEquals(1, labelStore.getNegativeLabels().size());

        labelStore.removeLabelByName("Anxious");
        assertEquals(0, labelStore.getNegativeLabels().size());
    }

    @Test
    void testClearRemovesAllLabels() {
        labelStore.addLabel(new MoodLabel.Builder("Excited").type(Type.Positive).build());
        labelStore.addLabel(new MoodLabel.Builder("Tired").type(Type.Negative).build());

        labelStore.clear();

        assertEquals(0, labelStore.getPositiveLabels().size());
        assertEquals(0, labelStore.getNegativeLabels().size());
    }

    @Test
    void testGetPositiveLabelObjects() {
        MoodLabelInterf joyful = new MoodLabel.Builder("Joyful").type(Type.Positive).build();
        labelStore.addLabel(joyful);

        List<MoodLabelInterf> result = labelStore.getPositiveLabelObjects();
        assertEquals(1, result.size());
        assertEquals("Joyful", result.get(0).getName());
    }

    @Test
    void testGetNegativeLabelObjects() {
        MoodLabelInterf angry = new MoodLabel.Builder("Angry").type(Type.Negative).build();
        labelStore.addLabel(angry);

        List<MoodLabelInterf> result = labelStore.getNegativeLabelObjects();
        assertEquals(1, result.size());
        assertEquals("Angry", result.get(0).getName());
    }
}
