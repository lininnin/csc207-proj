package interface_adapter.repository;

import entity.BeginAndDueDates;
import entity.Goal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GoalRepository {
    private final List<Goal> goals = new ArrayList<>();

    public void addGoal(Goal goal) {
        goals.add(goal);
    }

    public List<Goal> getAllGoals() {
        return new ArrayList<>(goals);
    }

    public void recordCompletionAndClean(Goal goal, LocalDate completionDate) {
        if (completionDate != null && goals.contains(goal)) {
            goal.recordCompletion(completionDate);
            if (goal.isGoalAchieved()) {
                goals.remove(goal);
            }
        }
    }

    public void removeGoal(Goal goal) {
        goals.remove(goal);
    }

    /**
     * Replaces an old goal with a new goal based on updated time period and dates.
     */
    public void updateGoalTimePeriod(Goal oldGoal, Goal.TimePeriod newTimePeriod,
                                     int newFrequency, LocalDate newBegin, LocalDate newDue) {
        if (goals.contains(oldGoal)) {
            Goal updatedGoal = new Goal(
                    oldGoal.getInfo(),
                    new BeginAndDueDates(newBegin, newDue),
                    oldGoal.getTargetTask(),
                    newTimePeriod,
                    newFrequency
            );
            goals.remove(oldGoal);
            goals.add(updatedGoal);
        }
    }
}
