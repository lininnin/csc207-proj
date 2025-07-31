package interface_adapter.Sophia.today_goal;

import entity.Sophia.Goal;
import java.util.List;

public class TodaysGoalsState {
    private List<Goal> todaysGoals;
    private String error = null;

    // Getter and setter for todaysGoals
    public List<Goal> getTodaysGoals() {
        return todaysGoals;
    }

    public void setTodaysGoals(List<Goal> todaysGoals) {
        this.todaysGoals = todaysGoals;
    }

    // Getter and setter for error
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
