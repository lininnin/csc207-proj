package interface_adapter.Sophia.today_goal;

import entity.Sophia.Goal;
import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class TodayGoalsViewModel extends ViewModel {
    private TodaysGoalsState state = new TodaysGoalsState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public TodayGoalsViewModel() {
        super("today goals");
    }


    public TodaysGoalsState getState() {
        return state;
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void setState(TodaysGoalsState state) {
        this.state = state;
        firePropertyChanged();
    }

    public void removeGoalByName(String goalName) {
        List<Goal> currentTodayGoals = new ArrayList<>(state.getTodayGoals());
        currentTodayGoals.removeIf(goal -> goal.getGoalInfo().getInfo().getName().equals(goalName));
        setState(new TodaysGoalsState(currentTodayGoals));
    }

}