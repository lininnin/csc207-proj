package entity.alex.EventAvailable;

/**
 * Interface for factory that creates EventAvailable instances.
 */
public interface EventAvailableFactoryInterf {

    /**
     * Creates a new instance of {@link EventAvailableInterf}, typically returning
     * an empty pool of available events that can be further populated.
     *
     * @return a new EventAvailableInterf instance
     */
    EventAvailableInterf create();
}


