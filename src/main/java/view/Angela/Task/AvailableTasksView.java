package view.Angela.Task;

import interface_adapter.Angela.task.available.AvailableTasksState;
import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.delete.DeleteTaskController;
import interface_adapter.Angela.task.delete.DeleteTaskState;
import interface_adapter.Angela.task.delete.DeleteTaskViewModel;
import use_case.Angela.task.TaskGateway;
import entity.info.Info;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * View for displaying available tasks with delete functionality.
 */
public class AvailableTasksView extends JPanel implements PropertyChangeListener {
    private final String viewName = "available tasks";

    private final AvailableTasksViewModel availableTasksViewModel;
    private final DeleteTaskViewModel deleteTaskViewModel;
    private final DefaultTableModel tableModel;
    private final JTable taskTable;
    private final JLabel messageLabel;

    private DeleteTaskController deleteTaskController;
    private TaskGateway taskGateway;

    public AvailableTasksView(AvailableTasksViewModel availableTasksViewModel,
                              DeleteTaskViewModel deleteTaskViewModel) {
        this.availableTasksViewModel = availableTasksViewModel;
        this.deleteTaskViewModel = deleteTaskViewModel;
        this.availableTasksViewModel.addPropertyChangeListener(this);
        this.deleteTaskViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Available Tasks"));

        // Message label for feedback
        messageLabel = new JLabel(" ");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel, BorderLayout.NORTH);

        // Create table
        String[] columnNames = {"Task Name", "Description", "Category", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        taskTable = new JTable(tableModel);
        taskTable.setRowHeight(30);

        // Custom renderer for the Actions column
        taskTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        taskTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTasks());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setDeleteTaskController(DeleteTaskController controller) {
        this.deleteTaskController = controller;
    }

    public void setTaskGateway(TaskGateway taskGateway) {
        this.taskGateway = taskGateway;
        refreshTasks(); // Initial load
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY.equals(evt.getPropertyName())) {
            AvailableTasksState state = (AvailableTasksState) evt.getNewValue();
            if (state.isRefreshNeeded()) {
                refreshTasks();
            }
        } else if (DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY.equals(evt.getPropertyName())) {
            DeleteTaskState state = (DeleteTaskState) evt.getNewValue();
            handleDeleteState(state);
        }
    }

    private void handleDeleteState(DeleteTaskState state) {
        if (state.isShowWarningDialog()) {
            // Show warning dialog
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Task '" + state.getPendingDeleteTaskName() +
                            "' exists in Today's Tasks.\nDelete from both lists?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION && deleteTaskController != null) {
                deleteTaskController.confirmDeleteFromBoth(state.getPendingDeleteTaskId());
            }
        } else if (state.getSuccessMessage() != null) {
            messageLabel.setForeground(new Color(0, 128, 0));
            messageLabel.setText(state.getSuccessMessage());
            Timer timer = new Timer(3000, e -> messageLabel.setText(" "));
            timer.setRepeats(false);
            timer.start();
        } else if (state.getError() != null) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(state.getError());
        }
    }

    private void refreshTasks() {
        tableModel.setRowCount(0);

        if (taskGateway != null) {
            List<Info> tasks = taskGateway.getAllAvailableTasks();
            for (Info task : tasks) {
                tableModel.addRow(new Object[]{
                        task.getName(),
                        task.getDescription(),
                        task.getCategory() != null ? task.getCategory() : "",
                        task.getId() // Store ID in actions column for button handling
                });
            }
        }

        messageLabel.setText(" ");
    }

    // Button renderer for Actions column
    private class ButtonRenderer extends JPanel implements TableCellRenderer {
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();

            JButton editButton = new JButton("Edit");
            JButton deleteButton = new JButton("Delete");

            editButton.setMargin(new Insets(2, 5, 2, 5));
            deleteButton.setMargin(new Insets(2, 5, 2, 5));

            add(editButton);
            add(deleteButton);

            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }

            return this;
        }
    }

    // Button editor for Actions column
    private class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private String currentTaskId;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            panel.removeAll();

            currentTaskId = (String) value;

            JButton editButton = new JButton("Edit");
            JButton deleteButton = new JButton("Delete");

            editButton.setMargin(new Insets(2, 5, 2, 5));
            deleteButton.setMargin(new Insets(2, 5, 2, 5));

            editButton.addActionListener(e -> {
                // TODO: Implement edit functionality
                JOptionPane.showMessageDialog(panel, "Edit feature coming soon!");
                fireEditingStopped();
            });

            deleteButton.addActionListener(e -> {
                if (deleteTaskController != null) {
                    deleteTaskController.execute(currentTaskId, true);
                }
                fireEditingStopped();
            });

            panel.add(editButton);
            panel.add(deleteButton);
            panel.setBackground(table.getBackground());

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentTaskId;
        }
    }

    public String getViewName() { return viewName; }
}