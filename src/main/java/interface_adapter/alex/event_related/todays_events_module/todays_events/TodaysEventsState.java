package interface_adapter.alex.event_related.todays_events_module.todays_events;

import entity.alex.Event.EventInterf;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the current list of available events to be shown in the view.
 * This class stores domain-level EventInterf entities.
 */
public class TodaysEventsState {

    private List<EventInterf> todaysEvents = new ArrayList<>();

    /**
     * Returns the current list of available events.
     *
     * @return list of EventInterf objects
     */
    public List<EventInterf> getTodaysEvents() {
        return todaysEvents;
    }

    /**
     * Sets the available event list.
     *
     * @param todaysEvents The new list of EventInterf objects to be shown.
     */
    public void setTodaysEvents(List<EventInterf> todaysEvents) {
        this.todaysEvents = todaysEvents;
    }
}

