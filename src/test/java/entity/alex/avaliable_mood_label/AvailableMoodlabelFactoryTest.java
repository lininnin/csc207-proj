package entity.alex.avaliable_mood_label;

import entity.alex.AvalibleMoodLabel.AvaliableMoodLabelFactory;
import entity.alex.AvalibleMoodLabel.AvaliableMoodLabelInterf;
import entity.alex.AvalibleMoodLabel.AvaliableMoodlabelFactoryInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AvaliableMoodLabelFactory.
 */
public class AvailableMoodlabelFactoryTest {

    private AvaliableMoodlabelFactoryInterf factory;

    @BeforeEach
    void setUp() {
        factory = new AvaliableMoodLabelFactory();
    }

    @Test
    void testCreateReturnsNonNullInstance() {
        AvaliableMoodLabelInterf label = factory.create();
        assertNotNull(label, "Factory should return a non-null instance.");
    }

    @Test
    void testCreateReturnsCorrectType() {
        AvaliableMoodLabelInterf label = factory.create();
        assertTrue(label instanceof AvaliableMoodLabelInterf,
                "Returned object should implement AvaliableMoodLabelInterf.");
    }
}
