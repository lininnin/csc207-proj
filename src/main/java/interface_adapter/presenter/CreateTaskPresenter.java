//package interface_adapter.presenter;
//
//import interface_adapter.view_model.TaskViewModel;
//import use_case.Angela.task.create.CreateTaskOutputBoundary;
//import use_case.Angela.task.create.CreateTaskOutputData;
//
///**
// * Presenter for task creation.
// * Implements the output boundary and updates the view model.
// */
//public class CreateTaskPresenter implements CreateTaskOutputBoundary {
//    private final TaskViewModel taskViewModel;
//
//    public CreateTaskPresenter(TaskViewModel taskViewModel) {
//        this.taskViewModel = taskViewModel;
//    }
//
//    @Override
//    public void presentSuccess(CreateTaskOutputData outputData) {
//        // Update view model with success message
//        taskViewModel.setSuccessMessage("Success: " + outputData.getMessage());
//        taskViewModel.clearError();
//        taskViewModel.clearForm();
//        taskViewModel.firePropertyChanged();
//    }
//
//    @Override
//    public void presentError(String error) {
//        // Update view model with error message
//        taskViewModel.setError("Error: " + error);
//        taskViewModel.firePropertyChanged();
//    }
//}