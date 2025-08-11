package entityTest.alex.event_available;

import entity.Alex.EventAvailable.EventAvailable;
import entity.Alex.EventAvailable.EventAvailableFactory;
import entity.Alex.EventAvailable.EventAvailableInterf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventAvailableFactoryTest {

    @Test
    public void testCreateReturnsNonNullInstance() {
        EventAvailableFactory factory = new EventAvailableFactory();
        EventAvailableInterf instance = factory.create();

        assertNotNull(instance);
        assertTrue(instance instanceof EventAvailable);
    }

    @Test
    public void testCreateReturnsDifferentInstances() {
        EventAvailableFactory factory = new EventAvailableFactory();
        EventAvailableInterf first = factory.create();
        EventAvailableInterf second = factory.create();

        assertNotSame(first, second);
    }
}
