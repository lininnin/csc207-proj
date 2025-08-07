//package interface_adapter.presenter;
//
//import interface_adapter.view_model.TaskViewModel;
//import use_case.Angela.task.edit_available.EditAvailableTaskOutputBoundary;
//import use_case.Angela.task.edit_available.EditAvailableTaskOutputData;
//
///**
// * Presenter for the edit available task use case.
// */
//public class EditAvailableTaskPresenter implements EditAvailableTaskOutputBoundary {
//    private final TaskViewModel taskViewModel;
//
//    public EditAvailableTaskPresenter(TaskViewModel taskViewModel) {
//        this.taskViewModel = taskViewModel;
//    }
//
//    @Override
//    public void prepareSuccessView(EditAvailableTaskOutputData outputData) {
//        // Update the task in the view model
//        // In a real implementation, we would update the specific task
//        taskViewModel.setSuccessMessage(outputData.getMessage());
//        taskViewModel.clearError();
//        taskViewModel.firePropertyChanged();
//    }
//
//    @Override
//    public void prepareFailView(String error) {
//        taskViewModel.setError(error);
//        taskViewModel.firePropertyChanged();
//    }
//}