package interface_adapter.presenter;

import use_case.mark_task_complete.MarkTaskCompleteOutputBoundary;
import use_case.mark_task_complete.MarkTaskCompleteOutputData;
import view.TaskViewModel;
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
        taskViewModel.setMessage(message);
    }

    @Override
    public void presentError(String error) {
        taskViewModel.setMessage("Error: " + error);
    }
}
