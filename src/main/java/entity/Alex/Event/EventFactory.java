package entity.Alex.Event;

import entity.BeginAndDueDates.BeginAndDueDatesInterf;
import entity.info.InfoInterf;

/**
 * Factory class for creating Event instances.
 * Follows the Dependency Inversion Principle by depending only on abstractions.
 */
public class EventFactory implements EventFactoryInterf {

    /**
     * Creates a new Event object with given info and begin/due dates.
     *
     * @param info Info of the event (interface type)
     * @param beginAndDueDates Begin and due dates (interface type)
     * @return a new EventInterf instance
     * @throws IllegalArgumentException if any required field is null
     */
    @Override
    public EventInterf createEvent(final InfoInterf info, final BeginAndDueDatesInterf beginAndDueDates) {
        if (info == null) {
            throw new IllegalArgumentException("Info is required");
        }
        if (beginAndDueDates == null) {
            throw new IllegalArgumentException("BeginAndDueDates is required");
        }

        return new Event.Builder(info)  // Builder 接受 InfoInterf
                .beginAndDueDates(beginAndDueDates) // 传入接口类型
                .build();
    }
}


