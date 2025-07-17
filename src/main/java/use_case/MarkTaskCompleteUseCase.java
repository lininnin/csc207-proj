package use_case;

import entity.Task;
import entity.DailyLog;
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

/**
 * Input data for marking task complete.
 */
class MarkTaskCompleteInputData {
    private final String taskId;

    public MarkTaskCompleteInputData(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() { return taskId; }
}

/**
 * Output data for marking task complete.
 */
class MarkTaskCompleteOutputData {
    private final String taskId;
    private final String taskName;
    private final LocalDateTime completionTime;
    private final double completionRate;

    public MarkTaskCompleteOutputData(String taskId, String taskName,
                                      LocalDateTime completionTime, double completionRate) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.completionTime = completionTime;
        this.completionRate = completionRate;
    }

    // Getters
    public String getTaskId() { return taskId; }
    public String getTaskName() { return taskName; }
    public LocalDateTime getCompletionTime() { return completionTime; }
    public double getCompletionRate() { return completionRate; }
}

/**
 * Output boundary interface for marking task complete.
 */
interface MarkTaskCompleteOutputBoundary {
    void presentSuccess(MarkTaskCompleteOutputData outputData);
    void presentError(String error);
}