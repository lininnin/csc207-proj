package view.Alex;

import interface_adapter.alex.Notification_related.NotificationState;
import interface_adapter.alex.Notification_related.NotificationViewModel;

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
        if ("state".equals(evt.getPropertyName()) && evt.getSource() == viewModel) {
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


