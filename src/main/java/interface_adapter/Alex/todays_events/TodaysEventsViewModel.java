package interface_adapter.Alex.todays_events;

import interface_adapter.ViewModel;

public class TodaysEventsViewModel extends ViewModel<TodaysEventsState> {

    public static final String TODAYS_EVENTS_PROPERTY = "todaysEvents";

    public TodaysEventsViewModel() {
        super(TODAYS_EVENTS_PROPERTY);
        this.setState(new TodaysEventsState());
    }
}

