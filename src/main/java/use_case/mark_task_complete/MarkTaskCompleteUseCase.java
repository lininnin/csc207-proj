package use_case.mark_task_complete;

import entity.Angela.Task.Task;
import entity.Angela.DailyLog;
import use_case.repository.DailyLogRepository;
import use_case.repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Use case for marking a task as complete.
 */
public class MarkTaskCompleteUseCase {
    private final TaskRepository taskRepository;
    private final DailyLogRepository dailyLogRepository;
    private final MarkTaskCompleteOutputBoundary outputBoundary;

    public MarkTaskCompleteUseCase(TaskRepository taskRepository,
                                   DailyLogRepository dailyLogRepository,
                                   MarkTaskCompleteOutputBoundary outputBoundary) {
        this.taskRepository = taskRepository;
        this.dailyLogRepository = dailyLogRepository;
        this.outputBoundary = outputBoundary;
    }

    /**
     * Marks a task as complete.
     *
     * @param inputData Contains the task ID to mark as complete
     */
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
}

