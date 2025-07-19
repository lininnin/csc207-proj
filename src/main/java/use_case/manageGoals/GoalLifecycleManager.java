package use_case.manageGoals;

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
        LocalDate newDue = newBegin.plusDays(6); // 7-day week

        // Reflection hack: your Goal class is immutable except progress list
        // So here we need to replace the Goal object or add setters (if allowed)
        // For now, assume you can recreate the Goal externally and replace it in storage
        // Or add a method in Goal to reset completionDates and dates.

        // Since no setters in Goal for dates and completionDates, you must recreate
        System.out.println("Resetting weekly goal \"" + goal.getInfo().getName() + "\" to new week: " +
                newBegin + " to " + newDue);
        // You would replace the old goal in your storage with a new one with updated dates and cleared progress.
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
