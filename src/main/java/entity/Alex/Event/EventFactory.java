package entity.Alex.Event;

import entity.Info.Info;
import entity.BeginAndDueDates.BeginAndDueDates;

/**
 * Factory class for creating Event instances.
 */
public class EventFactory implements EventFactoryInterf {

    /**
     * Creates a new Event object with given info and begin/due dates.
     *
     * @param info Info of the event
     * @param beginAndDueDates Begin and due dates
     * @return a new Event instance
     * @throws IllegalArgumentException if any required field is null
     */
    @Override
    public Event createEvent(Info info, BeginAndDueDates beginAndDueDates) {
        if (info == null) {
            throw new IllegalArgumentException("Info is required");
        }
        if (beginAndDueDates == null) {
            throw new IllegalArgumentException("BeginAndDueDates is required");
        }

        return new Event.Builder(info)
                .beginAndDueDates(beginAndDueDates)
                .build();
    }
}


