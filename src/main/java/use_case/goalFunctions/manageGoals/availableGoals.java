package use_case.goalFunctions.manageGoals;

import entity.Sophia.Goal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Repository class that manages storage and retrieval of Goal entities.
 * Provides functionality to add goals, get all goals,
 * record completions and remove goals when achieved.
 */
public class availableGoals {
    private List<Goal> availableGoals = new ArrayList<>();
    private List<Goal> currentPeriodGoals = new ArrayList<>();

    /**
     * Adds a goal to the repository.
     *
     * @param goal The Goal to add
     */
    public void addGoal(Goal goal) {
        availableGoals.add(goal);
        currentPeriodGoals.add(goal);
    }

    /**
     * Returns a defensive copy of all goals currently stored.
     *
     * @return List of all goals
     */
    public List<Goal> getAllGoals() {
        return new ArrayList<>(currentPeriodGoals);
    }

    public List<Goal> getAvailableGoals() {return new ArrayList<> (availableGoals);}

    /**
     * Records a completion date for the given goal,
     * then removes the goal from the repository if it has been achieved.
     *
     * @param goal The goal to update
     * @param completionDate The date when the task was completed
     */
    public void recordCompletionAndClean(Goal goal, LocalDate completionDate) {
        if (completionDate != null && availableGoals.contains(goal)) {
            goal.recordCompletion();
            if (goal.isCompleted()) {
                availableGoals.remove(goal);
            }
        }
    }

    /**
     * Removes a goal from the repository.
     *
     * @param goal The goal to remove
     */
    public void removeGoal(Goal goal) {
        availableGoals.remove(goal);
    }

    public void removeExpiredCompletedGoals() {
        LocalDate today = LocalDate.now();

        Iterator<Goal> iterator = currentPeriodGoals.iterator();
        while (iterator.hasNext()) {
            Goal goal = iterator.next();

            if (goal.isCompleted()) {
                LocalDate startDate = goal.getBeginAndDueDates().getBeginDate();

                boolean isWeekly = goal.getTimePeriod() == Goal.TimePeriod.WEEK;
                boolean isMonthly = goal.getTimePeriod() == Goal.TimePeriod.MONTH;

                boolean expired = false;

                if (isWeekly && today.isAfter(startDate.plusWeeks(1))) {
                    expired = true;
                } else if (isMonthly && today.isAfter(startDate.plusMonths(1))) {
                    expired = true;
                }

                if (expired) {
                    iterator.remove(); // removes from currentPeriodGoals
                }
            }
        }
    }

}
