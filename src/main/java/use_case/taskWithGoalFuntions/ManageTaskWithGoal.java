package use_case.taskWithGoalFuntions;

import entity.Goal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages goals associated with a task and tracks if the task is regular (i.e., tied to a goal).
 *
 * Todo: not reallu sure how to deal with regular
 */
public class ManageTaskWithGoal {
    private List<Goal> goals;
    private boolean isRegular;

    public void ManageTaskWithGoal() {
        this.goals = new ArrayList<>();
    }

    public void addGoal(Goal goal) {
        if (goal != null && !goals.contains(goal)) {
            goals.add(goal);
        }
    }

    public List<Goal> getGoals() {
        return Collections.unmodifiableList(goals); // prevents external modification
    }

    public boolean isRegular() {
        return isRegular;
    }
}
