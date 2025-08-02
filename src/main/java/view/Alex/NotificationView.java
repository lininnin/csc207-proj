package view.Alex;

import interface_adapter.Alex.Notification_related.NotificationState;
import interface_adapter.Alex.Notification_related.NotificationViewModel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * NotificationView listens to the NotificationViewModel
 * and displays a popup reminder when triggered.
 */
public class NotificationView implements PropertyChangeListener {

    private final NotificationViewModel viewModel;

    public NotificationView(NotificationViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(viewModel.getViewName())) {
            NotificationState state = (NotificationState) evt.getNewValue();

            if (state.shouldShowReminder()) {
                JOptionPane.showMessageDialog(
                        null,
                        state.getReminderMessage(),
                        "Reminder",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }
}

