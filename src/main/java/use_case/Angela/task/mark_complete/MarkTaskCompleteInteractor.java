package use_case.Angela.task.mark_complete;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskInterf;
import entity.Sophia.Goal;
import use_case.repository.GoalRepository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interactor for marking a task complete or incomplete.
 * Handles the business logic including one-time task removal.
 */
public class MarkTaskCompleteInteractor implements MarkTaskCompleteInputBoundary {
    private final MarkTaskCompleteDataAccessInterface dataAccess;
    private final MarkTaskCompleteOutputBoundary outputBoundary;
    private final GoalRepository goalRepository;

    public MarkTaskCompleteInteractor(MarkTaskCompleteDataAccessInterface dataAccess,
                                      MarkTaskCompleteOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
        this.goalRepository = null; // Will be set via setter or new constructor
    }
    
    public MarkTaskCompleteInteractor(MarkTaskCompleteDataAccessInterface dataAccess,
                                      MarkTaskCompleteOutputBoundary outputBoundary,
                                      GoalRepository goalRepository) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
        this.goalRepository = goalRepository;
    }

    @Override
    public void execute(MarkTaskCompleteInputData inputData) {
        String taskId = inputData.getTaskId();
        boolean markAsComplete = inputData.isMarkAsComplete();

        // Get the task from today's list
        TaskInterf task = dataAccess.getTodayTaskById(taskId);
        if (task == null) {
            outputBoundary.presentError("Task not found in Today's Tasks");
            return;
        }

        // Update the task's completion status
        boolean updated = dataAccess.updateTaskCompletionStatus(taskId, markAsComplete);
        if (!updated) {
            outputBoundary.presentError("Failed to update task completion status");
            return;
        }

        // Update goal progress if this task is a target for any today's goals
        if (goalRepository != null) {
            updateGoalProgressForTask(task, markAsComplete);
        }

        // One-time tasks are NOT removed when completed - they're removed at midnight reset

        // Create success output
        String taskName = task.getInfo().getName();
        String message = markAsComplete ? 
            "Task \"" + taskName + "\" marked as complete" : 
            "Task \"" + taskName + "\" marked as incomplete";

        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(
                taskId, taskName, LocalDateTime.now(), 0.0 // We'll calculate completion rate in presenter
        );
        outputBoundary.presentSuccess(outputData, message);
    }
    
    /**
     * Updates the progress of any goals that have the given task as their target.
     * @param task The task that was marked complete/incomplete
     * @param isCompleted Whether the task was marked as complete (true) or incomplete (false)
     */
    private void updateGoalProgressForTask(TaskInterf task, boolean isCompleted) {
        try {
            // Get today's goals
            List<Goal> todayGoals = goalRepository.getTodayGoals();
            
            for (Goal goal : todayGoals) {
                
                // Check if this goal's target task matches the completed task
                if (goal.getGoalInfo() == null) {
                } else if (goal.getGoalInfo().getTargetTaskInfo() == null) {
                } else {
                    
                    String targetTaskName = goal.getGoalInfo().getTargetTaskInfo().getName();
                    String completedTaskName = task.getInfo().getName();
                    
                    
                    // Match by task name (could also match by ID if available)
                    if (targetTaskName != null && targetTaskName.equals(completedTaskName)) {
                        // Update the goal's progress
                        int currentProgress = goal.getCurrentProgress();
                        if (isCompleted) {
                            // Increment progress if task is completed
                            goal.setCurrentProgress(currentProgress + 1);
                        } else if (currentProgress > 0) {
                            // Decrement progress if task is marked incomplete (but don't go below 0)
                            goal.setCurrentProgress(currentProgress - 1);
                        }
                        
                        // Save the updated goal
                        goalRepository.save(goal);
                    } else {
                    }
                }
            }
        } catch (Exception e) {
            // Log but don't fail the task completion if goal update fails
            System.err.println("Error updating goal progress: " + e.getMessage());
        }
    }
}