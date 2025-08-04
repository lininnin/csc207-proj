package interface_adapter.Alex.Settings_related.edit_notificationTime;

import use_case.Alex.Settings_related.edit_notificationTime.EditNotificationTimeOutputBoundary;
import use_case.Alex.Settings_related.edit_notificationTime.EditNotificationTimeOutputData;

/**
 * Presenter for the EditNotificationTime use case.
 * Updates the EditNotificationTimeViewModel based on success or failure.
 */
public class EditNotificationTimePresenter implements EditNotificationTimeOutputBoundary {

    private final EditNotificationTimeViewModel editViewModel;

    public EditNotificationTimePresenter(EditNotificationTimeViewModel editViewModel) {
        this.editViewModel = editViewModel;
    }

    @Override
    public void prepareSuccessView(EditNotificationTimeOutputData outputData) {
        System.out.println("🟢 [Presenter] Success with times: "
                + outputData.getReminder1() + ", " + outputData.getReminder2() + ", " + outputData.getReminder3());
        EditNotificationTimeState newState = new EditNotificationTimeState();

        newState.setReminder1(outputData.getReminder1());
        newState.setReminder2(outputData.getReminder2());
        newState.setReminder3(outputData.getReminder3());
        newState.setEditing(false);
        newState.setErrorMessage(null);

        editViewModel.setState(newState);
        editViewModel.firePropertyChanged(EditNotificationTimeViewModel.EDIT_NOTIFICATION_TIME_STATE_PROPERTY);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        EditNotificationTimeState errorState = new EditNotificationTimeState();
        errorState.setErrorMessage(errorMessage);
        errorState.setEditing(false);

        editViewModel.setState(errorState);
        editViewModel.firePropertyChanged(EditNotificationTimeViewModel.EDIT_NOTIFICATION_TIME_STATE_PROPERTY);
    }
}



