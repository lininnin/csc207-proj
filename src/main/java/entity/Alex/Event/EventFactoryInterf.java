package entity.Alex.Event;

import entity.BeginAndDueDates.BeginAndDueDatesInterf;
import entity.info.Info;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.InfoInterf;

/**
 * Factory interface for creating Event objects.
 */
public interface EventFactoryInterf {
    /**
     * Creates a new Event object.
     *
     * @param info Info of the event
     * @param beginAndDueDates Begin and due dates
     * @return a new Event instance
     */
    EventInterf createEvent(InfoInterf info, BeginAndDueDatesInterf beginAndDueDates);
}

