package entity.Sophia;

import java.util.ArrayList;
import java.util.List;

public class GoalAvaliable {

    /**
     * Returns a list of goals that are currently available.
     *
     * A goal is considered available if:
     * - It is not completed
     * - The current date is between its begin and due date (inclusive)
     *
     * @param goals The full list of goals
     * @return List of available goals
     */
    public static List<Goal> filterAvailableGoals(List<Goal> goals) {
        List<Goal> availableGoals = new ArrayList<>();
        for (Goal goal : goals) {
            if (goal.isAvailable()) {
                availableGoals.add(goal);
            }
        }
        return availableGoals;
    }
}
