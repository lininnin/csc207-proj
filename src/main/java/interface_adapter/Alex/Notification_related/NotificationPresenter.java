package interface_adapter.Alex.Notification_related;

import use_case.Alex.Notification_related.NotificationOutputBoundary;
import use_case.Alex.Notification_related.NotificationOutputData;

/**
 * Presenter for the notification use case.
 * Updates the NotificationViewModel to trigger reminder display in the UI.
 */
public class NotificationPresenter implements NotificationOutputBoundary {

    private final NotificationViewModel viewModel;

    public NotificationPresenter(NotificationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareReminderView(NotificationOutputData outputData) {
        NotificationState newState = new NotificationState();
        newState.setShouldShowReminder(true);
        newState.setReminderMessage(outputData.getReminderMessage());

        viewModel.setState(newState);
        viewModel.firePropertyChanged(viewModel.getViewName());
    }
}

