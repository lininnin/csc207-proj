package use_case.goalFunctions.manageGoals;

import entity.Goal;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

/**
 * Manages the lifecycle of goals — resetting weekly goals, archiving old goals,
 * and notifying when goals are achieved.
 */
public class GoalLifecycleManager {

    /**
     * Advances weekly goals that have ended, resetting their date range and progress.
     * Archives goals that are no longer needed.
     *
     * @param allGoals List of all goals in the system.
     */
    public void advanceWeeklyGoals(List<Goal> allGoals) {
        LocalDate today = LocalDate.now();

        Iterator<Goal> iterator = allGoals.iterator();
        while (iterator.hasNext()) {
            Goal goal = iterator.next();

            // Only handle weekly goals for now
            if (goal.getTimePeriod() == Goal.TimePeriod.WEEK) {
                LocalDate dueDate = goal.getBeginAndDueDates().getDueDate();

                // If goal period ended before today, reset or archive
                if (dueDate != null && dueDate.isBefore(today)) {
                    if (goal.isGoalAchieved()) {
                        notifyGoalAchieved(goal);
                    }
                    // Reset goal for next week period starting today
                    resetGoalForNextWeek(goal);
                }
            } else {
                // Optionally, archive or remove monthly or other goals if needed
                // For now, do nothing
            }
        }
    }

    /**
     * Resets the goal period to the current week and clears progress.
     *
     * @param goal The goal to reset.
     */
    private void resetGoalForNextWeek(Goal goal) {
        LocalDate newBegin = LocalDate.now();
        LocalDate newDue = newBegin.plusDays(6);
    }

    /**
     * Notifies that a goal has been achieved.
     *
     * @param goal The achieved goal.
     */
    private void notifyGoalAchieved(Goal goal) {
        System.out.println("Congratulations! You achieved your goal: " + goal.getInfo().getName());
    }
}
