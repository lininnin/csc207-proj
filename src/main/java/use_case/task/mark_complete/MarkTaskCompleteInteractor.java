package use_case.task.mark_complete;

import entity.TodaysTask;
import use_case.task.TaskRepository;
import java.util.List;

/**
 * Interactor for marking tasks as complete or incomplete.
 */
public class MarkTaskCompleteInteractor implements MarkTaskCompleteInputBoundary {
    private final TaskRepository taskRepository;
    private final MarkTaskCompleteOutputBoundary outputBoundary;

    public MarkTaskCompleteInteractor(TaskRepository taskRepository,
                                      MarkTaskCompleteOutputBoundary outputBoundary) {
        this.taskRepository = taskRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void markComplete(String taskId) {
        try {
            List<TodaysTask> todaysTasks = taskRepository.findAllTodaysTasks();
            TodaysTask task = findTaskById(todaysTasks, taskId);

            if (task == null) {
                outputBoundary.presentError("Task not found in today's list");
                return;
            }

            if (task.isCompleted()) {
                outputBoundary.presentError("Task is already completed");
                return;
            }

            // Mark as complete
            task.markComplete();
            taskRepository.updateTodaysTask(task);

            outputBoundary.presentSuccess(new MarkTaskCompleteOutputData(
                    taskId,
                    task.getInfo().getName(),
                    true,
                    task.getCompletedDateTime()
            ));

        } catch (Exception e) {
            outputBoundary.presentError("Failed to mark task complete: " + e.getMessage());
        }
    }

    @Override
    public void markIncomplete(String taskId) {
        try {
            List<TodaysTask> todaysTasks = taskRepository.findAllTodaysTasks();
            TodaysTask task = findTaskById(todaysTasks, taskId);

            if (task == null) {
                outputBoundary.presentError("Task not found in today's list");
                return;
            }

            if (!task.isCompleted()) {
                outputBoundary.presentError("Task is not completed");
                return;
            }

            // Mark as incomplete
            task.markIncomplete();
            taskRepository.updateTodaysTask(task);

            outputBoundary.presentSuccess(new MarkTaskCompleteOutputData(
                    taskId,
                    task.getInfo().getName(),
                    false,
                    null
            ));

        } catch (Exception e) {
            outputBoundary.presentError("Failed to mark task incomplete: " + e.getMessage());
        }
    }

    private TodaysTask findTaskById(List<TodaysTask> tasks, String taskId) {
        return tasks.stream()
                .filter(t -> t.getInfo().getId().equals(taskId))
                .findFirst()
                .orElse(null);
    }
}