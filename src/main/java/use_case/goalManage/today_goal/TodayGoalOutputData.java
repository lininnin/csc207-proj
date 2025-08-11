package use_case.goalManage.today_goal;

import entity.Sophia.Goal;
import java.util.List;

public class TodayGoalOutputData {
    private final List<Goal> todayGoals;

    public TodayGoalOutputData(List<Goal> todayGoals) {
        this.todayGoals = todayGoals;
    }

    public List<Goal> getTodayGoals() {
        return todayGoals;
    }
}