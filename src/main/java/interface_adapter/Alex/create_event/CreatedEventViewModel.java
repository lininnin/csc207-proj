package interface_adapter.Alex.create_event;

//import interface_adapter.CreateEventViewModelUpdateListener;
import interface_adapter.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class CreatedEventViewModel extends ViewModel<CreatedEventState> {

    public static final String TITLE_LABEL = "New Available Event";
    public static final String NAME_LABEL = "Name:";
    public static final String ONE_TIME_LABEL = "One-Time?";
    public static final String CATEGORY_LABEL = "Category:";
    public static final String DESCRIPTION_LABEL = "Description:";
    public static final String CREATE_EVENT_INFO_LABEL = "Create Event:";

    private final List<CreateEventViewModelUpdateListener> listeners = new ArrayList<>();

    public CreatedEventViewModel() {
        super("create event");
        setState(new CreatedEventState());
    }

    @Override
    public void setState(CreatedEventState state) {
        super.setState(state);
        notifyListeners();
    }

    public void addListener(CreateEventViewModelUpdateListener listener) {
        listeners.add(listener);
    }

    public void removeListener(CreateEventViewModelUpdateListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (CreateEventViewModelUpdateListener listener : listeners) {
            listener.onViewModelUpdated();
        }
    }
}

