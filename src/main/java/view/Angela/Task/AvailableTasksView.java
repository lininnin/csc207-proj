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
 * View for displaying available tasks with edit and delete functionality.
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
        String[] columnNames = {"Name", "Category", "Description", "Edit", "Delete"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make Edit and Delete columns editable so buttons work
                return column == 3 || column == 4;
            }
        };

        taskTable = new JTable(tableModel);
        taskTable.setRowHeight(30);

        // Set column widths
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Name
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Category
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Description
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(60);  // Edit
        taskTable.getColumnModel().getColumn(4).setPreferredWidth(60);  // Delete

        // Custom renderer and editor for Edit column
        taskTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer("Edit"));
        taskTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor("Edit"));

        // Custom renderer and editor for Delete column
        taskTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer("Delete"));
        taskTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor("Delete"));

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);
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
        System.out.println("DEBUG: AvailableTasksView received property change: " + evt.getPropertyName());
        if (AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY.equals(evt.getPropertyName())) {
            AvailableTasksState state = (AvailableTasksState) evt.getNewValue();
            System.out.println("DEBUG: Refresh needed: " + state.isRefreshNeeded());
            if (state.isRefreshNeeded()) {
                refreshTasks();
                // Reset the refresh flag
                state.setRefreshNeeded(false);
                availableTasksViewModel.setState(state);
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
            refreshTasks();
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
                        task.getCategory() != null ? task.getCategory() : "",
                        task.getDescription() != null ? task.getDescription() : "",
                        task.getId(), // Store ID for edit button
                        task.getId()  // Store ID for delete button
                });
            }
        }

        messageLabel.setText(" ");
    }

    // Button renderer for Edit and Delete columns
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            return this;
        }
    }

    // Button editor for Edit and Delete columns
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private String taskId;
        private boolean isPushed;

        public ButtonEditor(String text) {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            label = text;

            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            taskId = (String) value;
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                if ("Delete".equals(label) && deleteTaskController != null) {
                    deleteTaskController.execute(taskId, true);
                } else if ("Edit".equals(label)) {
                    // Get task details from the row
                    int row = taskTable.getSelectedRow();
                    if (row >= 0) {
                        String name = (String) tableModel.getValueAt(row, 0);
                        String category = (String) tableModel.getValueAt(row, 1);
                        String description = (String) tableModel.getValueAt(row, 2);

                        // Show edit dialog
                        JPanel editPanel = new JPanel(new GridLayout(3, 2, 5, 5));
                        JTextField nameField = new JTextField(name);
                        JTextField categoryField = new JTextField(category);
                        JTextArea descriptionArea = new JTextArea(description);
                        descriptionArea.setRows(3);

                        editPanel.add(new JLabel("Name:"));
                        editPanel.add(nameField);
                        editPanel.add(new JLabel("Category:"));
                        editPanel.add(categoryField);
                        editPanel.add(new JLabel("Description:"));
                        editPanel.add(new JScrollPane(descriptionArea));

                        int result = JOptionPane.showConfirmDialog(
                                AvailableTasksView.this,
                                editPanel,
                                "Edit Task",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE
                        );

                        if (result == JOptionPane.OK_OPTION) {
                            // TODO: Implement edit functionality with controller
                            JOptionPane.showMessageDialog(
                                    AvailableTasksView.this,
                                    "Edit functionality coming soon - will be implemented with edit_available_task use case completion",
                                    "Info",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    }
                }
            }
            isPushed = false;
            return taskId;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    public String getViewName() { return viewName; }
}