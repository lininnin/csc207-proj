package interface_adapter.Sophia.today_goal;

import entity.Sophia.Goal;
import java.util.ArrayList;
import java.util.List;

public class TodaysGoalsState {
    private List<Goal> todayGoals;

    public TodaysGoalsState() {
        this.todayGoals = new ArrayList<>();
    }

    public TodaysGoalsState(List<Goal> goals) {
        this.todayGoals = new ArrayList<>(goals);
    }

    public List<Goal> getTodayGoals() {
        return todayGoals;
    }

    public void setTodayGoals(List<Goal> todayGoals) {
        this.todayGoals = new ArrayList<>(todayGoals);
    }
}
