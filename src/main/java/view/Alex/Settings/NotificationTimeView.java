package view.Alex.Settings;

import interface_adapter.Alex.Settings_related.notificationTime_module.NotificationTimeState;
import interface_adapter.Alex.Settings_related.notificationTime_module.NotificationTimeViewModel;
import interface_adapter.Alex.Settings_related.edit_notificationTime.EditNotificationTimeViewModel;
import interface_adapter.Alex.Settings_related.edit_notificationTime.EditNotificationTimeState;
import interface_adapter.Alex.Settings_related.edit_notificationTime.EditNotificationTimeController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NotificationTimeView extends JPanel implements PropertyChangeListener {

    private final NotificationTimeViewModel viewModel;
    private final EditNotificationTimeViewModel editViewModel;
    private final EditNotificationTimeController controller;

    private final JTextField reminder1Field = new JTextField(5);
    private final JTextField reminder2Field = new JTextField(5);
    private final JTextField reminder3Field = new JTextField(5);

    private final JButton editButton = new JButton("Edit");
    private final JButton saveButton = new JButton("Save");
    private final JButton cancelButton = new JButton("Cancel");

    public NotificationTimeView(NotificationTimeViewModel viewModel,
                                EditNotificationTimeViewModel editViewModel,
                                EditNotificationTimeController controller) {
        this.viewModel = viewModel;
        this.editViewModel = editViewModel;
        this.controller = controller;

        this.viewModel.addPropertyChangeListener(this);
        this.editViewModel.addPropertyChangeListener(this);

        setupUI();
        refresh(viewModel.getState());
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Notification Times", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        fieldsPanel.add(new JLabel("Reminder 1 (HH:mm):"));
        fieldsPanel.add(reminder1Field);
        fieldsPanel.add(new JLabel("Reminder 2 (HH:mm):"));
        fieldsPanel.add(reminder2Field);
        fieldsPanel.add(new JLabel("Reminder 3 (HH:mm):"));
        fieldsPanel.add(reminder3Field);
        add(fieldsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        toggleEditing(false);

        // --------------------- Action Listeners ---------------------
        editButton.addActionListener(e -> {
            viewModel.getState().setEditing(true);
            viewModel.firePropertyChanged("state");
        });

        cancelButton.addActionListener(e -> {
            viewModel.getState().setEditing(false);
            viewModel.firePropertyChanged("state");
        });

        saveButton.addActionListener(e -> {
            System.out.println("✅ [View] Save clicked with values: "
                    + getReminder1Text() + ", " + getReminder2Text() + ", " + getReminder3Text());
            controller.execute(
                    getReminder1Text(),
                    getReminder2Text(),
                    getReminder3Text()
            );
        });
    }

    private void toggleEditing(boolean editable) {
        reminder1Field.setEditable(editable);
        reminder2Field.setEditable(editable);
        reminder3Field.setEditable(editable);

        saveButton.setEnabled(editable);
        cancelButton.setEnabled(editable);
        editButton.setEnabled(!editable);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) return;

        if (evt.getSource() == viewModel) {
            NotificationTimeState state = (NotificationTimeState) evt.getNewValue();
            refresh(state);
        }

        if (evt.getSource() == editViewModel) {
            EditNotificationTimeState state = (EditNotificationTimeState) evt.getNewValue();

            // 更新字段（可选）
            reminder1Field.setText(state.getReminder1());
            reminder2Field.setText(state.getReminder2());
            reminder3Field.setText(state.getReminder3());
            toggleEditing(state.isEditing());

            // 错误弹窗
            if (state.getErrorMessage() != null) {
                JOptionPane.showMessageDialog(this, state.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refresh(NotificationTimeState state) {
        reminder1Field.setText(state.getReminder1());
        reminder2Field.setText(state.getReminder2());
        reminder3Field.setText(state.getReminder3());

        toggleEditing(state.isEditing());

        if (state.getErrorMessage() != null) {
            JOptionPane.showMessageDialog(this, state.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Public getters
    public String getReminder1Text() {
        return reminder1Field.getText().trim();
    }

    public String getReminder2Text() {
        return reminder2Field.getText().trim();
    }

    public String getReminder3Text() {
        return reminder3Field.getText().trim();
    }
}
