package interface_adapter.alex.event_related.todays_events_module.todays_events;

import entity.Alex.Event.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the current list of available events to be shown in the view.
 * This class stores domain-level Event entities.
 */
public class TodaysEventsState {

    private List<Event> todaysEvents = new ArrayList<>();

    /**
     * Returns the current list of available event Info objects.
     */
    public List<Event> getTodaysEvents() {
        return todaysEvents;
    }

    /**
     * Sets the available event list.
     * @param todaysEvents The new list of Event objects to be shown.
     */
    public void setTodaysEvents(List<Event> todaysEvents) {
        this.todaysEvents = todaysEvents;
    }
}
