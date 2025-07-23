package use_case.goalFunctions.manageGoals;

import entity.Sophia.Goal;
import entity.Angela.Task.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Use case class responsible for marking a task as complete
 * and updating all active goals associated with that task.
 * It marks the task complete, finds relevant goals whose active period
 * includes the completion date, records the completion for those goals,
 * and removes goals from the repository if they are achieved.
 */
public class CompleteTaskAndUpdateGoalUseCase {

    private final availableGoals goalRepository;

    /**
     * Constructs the use case with a GoalRepository dependency.
     *
     * @param goalRepository The repository managing goal storage and updates.
     */
    public CompleteTaskAndUpdateGoalUseCase(availableGoals goalRepository) {
        this.goalRepository = goalRepository;
    }

    /**
     * Marks the given task as complete at the specified completion time.
     * Then updates all goals tracking this task that are active during the completion date,
     * records completion dates in those goals,
     * and removes goals from the repository if their completion criteria are met.
     *
     * @param task           The task to mark complete.
     * @param completionTime The date and time when the task was completed.
     * @throws IllegalArgumentException if task or completionTime is null.
     */
    public void execute(Task task, LocalDateTime completionTime) {
        if (task == null || completionTime == null) {
            throw new IllegalArgumentException("Task and completionTime cannot be null");
        }

        // Mark the task complete
        task.completeTask(completionTime);

        // Extract completion date without time component
        LocalDate completionDate = completionTime.toLocalDate();

        // Get all goals from repository
        List<Goal> allGoals = goalRepository.getAllGoals();

        // Update relevant goals
        for (Goal goal : allGoals) {
            if (goal.getTargetTask().getInfo().getId().equals(task.getInfo().getId())) {
                LocalDate start = goal.getBeginAndDueDates().getBeginDate();
                LocalDate end = goal.getBeginAndDueDates().getDueDate();

                boolean afterStart = !completionDate.isBefore(start);
                boolean beforeEnd = (end == null) || !completionDate.isAfter(end);

                if (afterStart && beforeEnd) {
                    goal.recordCompletion(completionDate);

                    // Remove the goal from repository if achieved
                    if (goal.isGoalAchieved()) {
                        goalRepository.removeGoal(goal);
                    }
                }
            }
        }
    }
}
