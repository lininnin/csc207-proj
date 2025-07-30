package interface_adapter.Alex.Event_related.available_event_module.available_event;

import entity.Info.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the current list of available events to be shown in the view.
 * This class stores domain-level Info entities.
 */
public class AvailableEventState {

    private List<Info> availableEvents = new ArrayList<>();

    /**
     * Returns the current list of available event Info objects.
     */
    public List<Info> getAvailableEvents() {
        return availableEvents;
    }

    /**
     * Sets the available event list.
     * @param availableEvents The new list of Info objects to be shown.
     */
    public void setAvailableEvents(List<Info> availableEvents) {
        this.availableEvents = availableEvents;
    }
}



