package entity.Alex.EventAvailable;

/**
 * Concrete factory that creates EventAvailable objects.
 */
public class EventAvailableFactory implements EventAvailableFactoryInterf {

    /**
     * Creates and returns a new instance of {@link EventAvailable}.
     *
     * @return a new {@link EventAvailableInterf} representing an empty pool of available events
     */
    @Override
    public EventAvailableInterf create() {
        return new EventAvailable();
    }
}

