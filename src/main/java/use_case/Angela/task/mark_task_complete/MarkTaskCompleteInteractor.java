package use_case.Angela.task.mark_task_complete;

import entity.Angela.Task.Task;
import entity.Angela.DailyLog;
import use_case.repository.DailyLogRepository;
import use_case.repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Use case for marking a task as complete.
 * Updates task status and daily summary statistics.
 */
public class MarkTaskCompleteInteractor implements MarkTaskCompleteInputBoundary {
    private final TaskRepository taskRepository;
    private final DailyLogRepository dailyLogRepository;
    private final MarkTaskCompleteOutputBoundary outputBoundary;

    public MarkTaskCompleteInteractor(TaskRepository taskRepository,
                                      DailyLogRepository dailyLogRepository,
                                      MarkTaskCompleteOutputBoundary outputBoundary) {
        this.taskRepository = taskRepository;
        this.dailyLogRepository = dailyLogRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(MarkTaskCompleteInputData inputData) {
        try {
            // Find the task
            Task task = taskRepository.findById(inputData.getTaskId());
            if (task == null) {
                outputBoundary.presentError("Task not found");
                return;
            }

            if (task.isComplete()) {
                outputBoundary.presentError("Task is already completed");
                return;
            }

            // Check if task is in today's tasks
            boolean isInToday = taskRepository.findTodaysTasks().stream()
                    .anyMatch(t -> t.getInfo().getId().equals(task.getInfo().getId()));

            if (!isInToday) {
                outputBoundary.presentError("Task is not in today's tasks");
                return;
            }

            // Mark task as complete
            LocalDateTime completionTime = LocalDateTime.now();
            task.completeTask(completionTime);

            // Update in repository
            taskRepository.update(task);

            // Update today's daily log
            DailyLog todayLog = dailyLogRepository.findByDate(LocalDate.now());
            if (todayLog == null) {
                todayLog = new DailyLog(LocalDate.now());
            }
            todayLog.markTaskCompleted(task, completionTime);
            dailyLogRepository.save(todayLog);

            // Update linked goal progress if applicable
            updateLinkedGoalProgress(task);

            // Prepare output
            MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(
                    task.getInfo().getId(),
                    task.getInfo().getName(),
                    completionTime,
                    todayLog.getDailyTaskSummary().getCompletionRate()
            );

            outputBoundary.presentSuccess(outputData);

        } catch (Exception e) {
            outputBoundary.presentError("Failed to mark task complete: " + e.getMessage());
        }
    }

    /**
     * Updates linked goal progress when task is completed.
     * This will be implemented when integrating with Sophia's goal features.
     *
     * @param task The completed task
     */
    private void updateLinkedGoalProgress(Task task) {
        // TODO: Implement when integrating with goal repository
        // Find goals linked to this task
        // Update their progress counters
    }
}