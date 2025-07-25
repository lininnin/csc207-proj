package entity.Alex.EventAvailable;

/**
 * Concrete factory that creates EventAvailable objects.
 */
public class EventAvailableFactory implements EventAvailableFactoryInterf {

    @Override
    public EventAvailableInterf create() {
        return new EventAvailable();
    }
}

