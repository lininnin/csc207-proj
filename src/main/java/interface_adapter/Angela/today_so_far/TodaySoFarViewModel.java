package interface_adapter.Angela.today_so_far;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View Model for the Today So Far panel.
 */
public class TodaySoFarViewModel extends ViewModel {
    
    public static final String TODAY_SO_FAR_STATE_PROPERTY = "todaySoFarState";
    
    private TodaySoFarState state = new TodaySoFarState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    
    public TodaySoFarViewModel() {
        super("today_so_far");
    }
    
    public void setState(TodaySoFarState state) {
        this.state = state;
    }
    
    public TodaySoFarState getState() {
        return state;
    }
    
    @Override
    public void firePropertyChanged() {
        support.firePropertyChange(TODAY_SO_FAR_STATE_PROPERTY, null, state);
    }
    
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}