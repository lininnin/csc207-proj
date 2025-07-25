package interface_adapter.Alex.Event_related.todays_events_module.delete_todays_event;

import interface_adapter.ViewModel;

public class DeleteTodaysEventViewModel extends ViewModel<DeleteTodaysEventState> {

    public static final String DELETE_TODAYS_EVENT_STATE_PROPERTY = "deletedEventState";

    public DeleteTodaysEventViewModel() {
        super("deleted event view");
        this.setState(new DeleteTodaysEventState());
    }

}
