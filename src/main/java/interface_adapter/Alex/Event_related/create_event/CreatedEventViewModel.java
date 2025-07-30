package interface_adapter.Alex.Event_related.create_event;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class CreatedEventViewModel extends ViewModel<CreatedEventState> {

    public static final String TITLE_LABEL = "New Available Event";
    public static final String NAME_LABEL = "Name:";
    public static final String ONE_TIME_LABEL = "One-Time?";
    public static final String CATEGORY_LABEL = "Category:";
    public static final String DESCRIPTION_LABEL = "Description:";
    public static final String CREATE_EVENT_INFO_LABEL = "Create Event:";

    public static final String CREATED_EVENT_STATE_PROPERTY = "createdEventState";

    private final List<CreateEventViewModelUpdateListener> listeners = new ArrayList<>();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public CreatedEventViewModel() {
        super("create event");
        setState(new CreatedEventState());
    }

    @Override
    public void setState(CreatedEventState state) {
        super.setState(state);
        notifyListeners();
        support.firePropertyChange(CREATED_EVENT_STATE_PROPERTY, null, state); // 添加监听触发
    }

    // ------------------- Listener methods -------------------
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

    // ------------------- PropertyChangeSupport -------------------
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}


