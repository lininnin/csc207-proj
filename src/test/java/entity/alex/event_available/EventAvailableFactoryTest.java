package entity.alex.event_available;

import entity.Alex.EventAvailable.EventAvailableFactoryInterf;
import entity.Alex.EventAvailable.EventAvailableFactory;
import entity.Alex.EventAvailable.EventAvailableInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventAvailableFactoryTest {

    private EventAvailableFactoryInterf factory;

    @BeforeEach
    void setUp() {
        factory = new EventAvailableFactory();
    }

    @Test
    void testCreateReturnsNonNullInstance() {
        EventAvailableInterf instance = factory.create();
        assertNotNull(instance, "Factory should return a non-null instance.");
    }

    @Test
    void testCreateReturnsCorrectType() {
        EventAvailableInterf instance = factory.create();
        assertTrue(instance instanceof EventAvailableInterf, "Returned instance should implement EventAvailableInterf.");
    }

    @Test
    void testMultipleCreatesReturnDifferentInstances() {
        EventAvailableInterf instance1 = factory.create();
        EventAvailableInterf instance2 = factory.create();
        assertNotSame(instance1, instance2, "Factory should return new instances each time.");
    }
}

