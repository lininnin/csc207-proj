package use_case.goalManage.today_goal;

import entity.Sophia.Goal;
import java.util.List;

public class TodayGoalOutputData {
    private final List<Goal> todayGoals;
    private final String message;

    public TodayGoalOutputData(List<Goal> todayGoals) {
        this(todayGoals, "");
    }

    public TodayGoalOutputData(List<Goal> todayGoals, String message) {
        this.todayGoals = todayGoals;
        this.message = message;
    }

    public List<Goal> getTodayGoals() {
        return todayGoals;
    }

    public String getMessage() {
        return message;
    }
}