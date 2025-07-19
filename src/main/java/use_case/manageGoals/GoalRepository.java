package use_case.manageGoals;

import entity.Goal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class that manages storage and retrieval of Goal entities.
 * Provides functionality to add goals, get all goals,
 * record completions and remove goals when achieved.
 */
public class GoalRepository {
    private final List<Goal> goals = new ArrayList<>();

    /**
     * Adds a goal to the repository.
     *
     * @param goal The Goal to add
     */
    public void addGoal(Goal goal) {
        goals.add(goal);
    }

    /**
     * Returns a defensive copy of all goals currently stored.
     *
     * @return List of all goals
     */
    public List<Goal> getAllGoals() {
        return new ArrayList<>(goals);
    }

    /**
     * Records a completion date for the given goal,
     * then removes the goal from the repository if it has been achieved.
     *
     * @param goal The goal to update
     * @param completionDate The date when the task was completed
     */
    public void recordCompletionAndClean(Goal goal, LocalDate completionDate) {
        if (completionDate != null && goals.contains(goal)) {
            goal.recordCompletion(completionDate);
            if (goal.isGoalAchieved()) {
                goals.remove(goal);
            }
        }
    }

    /**
     * Removes a goal from the repository.
     *
     * @param goal The goal to remove
     */
    public void removeGoal(Goal goal) {
        goals.remove(goal);
    }
}
