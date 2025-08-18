package use_case.goalManage.today_goal;

import java.util.List;

import entity.Sophia.Goal;

/**
 * A data structure for the output data of the today's goals use case.
 * It encapsulates the list of goals for the current day to be displayed.
 */
public class TodayGoalOutputData {
    private final List<Goal> todayGoals;

    /**
     * Constructs a {@code TodayGoalOutputData} object.
     *
     * @param todayGoals The list of goals for the current day.
     */
    public TodayGoalOutputData(List<Goal> todayGoals) {
        this.todayGoals = todayGoals;
    }

    /**
     * Gets the list of today's goals.
     *
     * @return The list of {@code Goal} objects.
     */
    public List<Goal> getTodayGoals() {
        return todayGoals;
    }
}
