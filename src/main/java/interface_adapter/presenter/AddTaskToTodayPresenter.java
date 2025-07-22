package interface_adapter.presenter;

import use_case.add_task_to_today.AddTaskToTodayOutputBoundary;
import use_case.add_task_to_today.AddTaskToTodayOutputData;
import view.Task.TaskViewModel;

/**
 * Presenter for adding tasks to today.
 */
public class AddTaskToTodayPresenter implements AddTaskToTodayOutputBoundary{
    private final TaskViewModel taskViewModel;

    public AddTaskToTodayPresenter(TaskViewModel taskViewModel) {
        this.taskViewModel = taskViewModel;
    }

    @Override
    public void presentSuccess(AddTaskToTodayOutputData outputData) {
        String message = String.format("'%s' added to today's tasks", outputData.getTaskName());
        taskViewModel.setMessage(message);
    }

    @Override
    public void presentError(String error) {
        taskViewModel.setMessage("Error: " + error);
    }
}
