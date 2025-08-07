//package interface_adapter.presenter;
//
//import interface_adapter.view_model.TaskViewModel;
//import use_case.Angela.task.add_to_today.AddTaskToTodayOutputBoundary;
//import use_case.Angela.task.add_to_today.AddTaskToTodayOutputData;
//
///**
// * Presenter for adding tasks to today.
// * Implements the output boundary and updates the view model.
// */
//public class AddTaskToTodayPresenter implements AddTaskToTodayOutputBoundary {
//    private final TaskViewModel taskViewModel;
//
//    public AddTaskToTodayPresenter(TaskViewModel taskViewModel) {
//        this.taskViewModel = taskViewModel;
//    }
//
//    @Override
//    public void presentSuccess(AddTaskToTodayOutputData outputData) {
//        // Add the task to today's list in view model
//        taskViewModel.addTodaysTask(outputData.getTask());
//        taskViewModel.setSuccessMessage("Task added to today: " + outputData.getTaskName());
//        taskViewModel.clearError();
//        taskViewModel.firePropertyChanged();
//    }
//
//    @Override
//    public void presentError(String error) {
//        taskViewModel.setError(error);
//        taskViewModel.firePropertyChanged();
//    }
//}