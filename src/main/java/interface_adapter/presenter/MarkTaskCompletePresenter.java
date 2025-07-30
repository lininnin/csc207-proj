package interface_adapter.presenter;

import interface_adapter.view_model.TaskViewModel;
import use_case.Angela.task.mark_complete.MarkTaskCompleteOutputBoundary;
import use_case.Angela.task.mark_complete.MarkTaskCompleteOutputData;
import java.time.format.DateTimeFormatter;

/**
 * Presenter for marking tasks complete.
 */
public class MarkTaskCompletePresenter implements MarkTaskCompleteOutputBoundary {
    private final TaskViewModel taskViewModel;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public MarkTaskCompletePresenter(TaskViewModel taskViewModel) {
        this.taskViewModel = taskViewModel;
    }

    @Override
    public void presentSuccess(MarkTaskCompleteOutputData outputData) {
        String message = String.format("Task '%s' completed at %s. Today's completion rate: %.1f%%",
                outputData.getTaskName(),
                outputData.getCompletionTime().format(TIME_FORMATTER),
                outputData.getCompletionRate() * 100
        );

        taskViewModel.setSuccessMessage(message);
        taskViewModel.clearError();

        // Update the task status in view model
        taskViewModel.getTodaysTasks().stream()
                .filter(task -> task.getId().equals(outputData.getTaskId()))
                .findFirst()
                .ifPresent(task -> task.setComplete(true));

        taskViewModel.firePropertyChanged();
    }

    @Override
    public void presentError(String error) {
        taskViewModel.setError(error);
        taskViewModel.firePropertyChanged();
    }
}