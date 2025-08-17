package interface_adapter.sophia.today_goal;

import java.util.ArrayList;
import java.util.List;

import entity.Sophia.Goal;

/**
 * Represents the state of today's goals.
 * Maintains the current list of goals for today.
 */
public class TodaysGoalsState {
    private List<Goal> todayGoals;

    /**
     * Constructs an empty TodaysGoalsState.
     */
    public TodaysGoalsState() {
        this.todayGoals = new ArrayList<>();
    }

    /**
     * Constructs a TodaysGoalsState with the specified goals.
     * @param goals The initial list of goals
     */
    public TodaysGoalsState(List<Goal> goals) {
        this.todayGoals = new ArrayList<>(goals);
    }

    /**
     * Gets the list of today's goals.
     * @return List of today's goals
     */
    public List<Goal> getTodayGoals() {
        return todayGoals;
    }

    /**
     * Sets the list of today's goals.
     * @param todayGoals The new list of goals
     */
    public void setTodayGoals(List<Goal> todayGoals) {
        this.todayGoals = new ArrayList<>(todayGoals);
    }
}
