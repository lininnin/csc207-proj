package interface_adapter.Sophia.today_goal;

import view.ViewModel;
import java.beans.PropertyChangeListener;

public class TodayGoalsViewModel extends ViewModel {
    private TodaysGoalsState state = new TodaysGoalsState();

    public TodayGoalsViewModel() {
        super("today's goals");
    }

    public TodaysGoalsState getState() {
        return state;
    }

    public void setState(TodaysGoalsState state) {
        this.state = state;
        firePropertyChanged();
    }

    // Add this method to properly notify listeners
    public void firePropertyChanged() {
        super.firePropertyChange("state", null, this.state);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
    }
}