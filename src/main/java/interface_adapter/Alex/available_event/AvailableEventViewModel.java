package interface_adapter.Alex.available_event;

import interface_adapter.ViewModel;

/**
 * ViewModel for available event list.
 * Notifies views when availableEvents list is updated.
 */
public class AvailableEventViewModel extends ViewModel<AvailableEventState> {

    public static final String AVAILABLE_EVENTS_PROPERTY = "availableEvents";

    public AvailableEventViewModel() {
        super("AvailableEventView");
        this.setState(new AvailableEventState());
    }
}


