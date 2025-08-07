//package interface_adapter.presenter;
//
//import interface_adapter.ViewManagerModel;
//import interface_adapter.view_model.TaskViewModel;
//import use_case.Angela.task.delete.DeleteTaskOutputBoundary;
//import use_case.Angela.task.delete.DeleteTaskOutputData;
//
///**
// * Presenter for the delete task use case.
// * Updates the view based on deletion results.
// */
//public class DeleteTaskPresenter implements DeleteTaskOutputBoundary {
//    private final TaskViewModel taskViewModel;
//    private final ViewManagerModel viewManagerModel;
//
//    public DeleteTaskPresenter(TaskViewModel taskViewModel,
//                               ViewManagerModel viewManagerModel) {
//        this.taskViewModel = taskViewModel;
//        this.viewManagerModel = viewManagerModel;
//    }
//
//    @Override
//    public void prepareSuccessView(DeleteTaskOutputData outputData) {
//        // Remove the task from the view model
//        taskViewModel.removeAvailableTask(outputData.getDeletedTaskId());
//
//        if (outputData.isDeletedFromBoth()) {
//            taskViewModel.removeTodaysTask(outputData.getDeletedTaskId());
//        }
//
//        taskViewModel.setSuccessMessage(outputData.getMessage());
//        taskViewModel.firePropertyChanged();
//    }
//
//    @Override
//    public void prepareFailView(String error) {
//        taskViewModel.setError(error);
//        taskViewModel.firePropertyChanged();
//    }
//
//    @Override
//    public void showDeleteFromBothWarning(String taskId, String taskName) {
//        // Set up the warning dialog state
//        taskViewModel.setWarningDialogVisible(true);
//        taskViewModel.setWarningDialogTitle("Delete Task");
//        taskViewModel.setWarningDialogMessage(
//                "This item '" + taskName + "' is also in Today's list. " +
//                        "Deleting will remove it from both lists. Continue?"
//        );
//        taskViewModel.setPendingDeleteTaskId(taskId);
//        taskViewModel.firePropertyChanged();
//    }
//}