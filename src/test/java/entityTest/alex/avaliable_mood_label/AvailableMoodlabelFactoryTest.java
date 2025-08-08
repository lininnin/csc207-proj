package entityTest.alex.avaliable_mood_label;

import entity.Alex.AvalibleMoodLabel.AvaliableMoodLabel;
import entity.Alex.AvalibleMoodLabel.AvaliableMoodLabelFactory;
import entity.Alex.AvalibleMoodLabel.AvaliableMoodLabelInterf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AvailableMoodlabelFactoryTest {

    @Test
    public void testFactoryCreatesNewInstance() {
        AvaliableMoodLabelFactory factory = new AvaliableMoodLabelFactory();
        AvaliableMoodLabelInterf result = factory.create();

        assertNotNull(result);
        assertTrue(result instanceof AvaliableMoodLabel);
    }

    @Test
    public void testFactoryCreatesDistinctInstances() {
        AvaliableMoodLabelFactory factory = new AvaliableMoodLabelFactory();
        AvaliableMoodLabelInterf first = factory.create();
        AvaliableMoodLabelInterf second = factory.create();

        assertNotSame(first, second);
    }
}

