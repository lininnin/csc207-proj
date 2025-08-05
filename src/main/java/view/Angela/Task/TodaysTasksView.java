package view.Angela.Task;

import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import entity.Angela.Task.Task;
import entity.Category;
import use_case.Angela.task.TaskGateway;
import use_case.Angela.category.CategoryGateway;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;
import java.util.List;
import view.FontUtil;

/**
 * View for displaying today's tasks with priority, status, and due date.
 */
public class TodaysTasksView extends JPanel implements PropertyChangeListener {
    private final DefaultTableModel tableModel;
    private final JTable taskTable;
    private final JLabel completedLabel;
    private final JLabel inProgressLabel;
    private final JLabel notStartedLabel;
    private final JLabel messageLabel;

    private TodayTasksViewModel viewModel;
    private TaskGateway taskGateway;
    private CategoryGateway categoryGateway;

    public TodaysTasksView(TodayTasksViewModel viewModel) {
        this.viewModel = viewModel;
        if (viewModel != null) {
            viewModel.addPropertyChangeListener(this);
        }
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Today's Tasks"));
        setBackground(new Color(240, 240, 255));

        // Message label for feedback
        messageLabel = new JLabel(" ");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel, BorderLayout.NORTH);

        // Create table
        String[] columnNames = {"Status", "Name", "Category", "Priority", "Due", "Edit", "Delete"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only the status checkbox column (0) and button columns (5, 6) are editable
                return column == 0 || column == 5 || column == 6;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class; // Status checkbox
                }
                return Object.class;
            }
        };

        taskTable = new JTable(tableModel);
        taskTable.setFont(FontUtil.getStandardFont());
        taskTable.getTableHeader().setFont(FontUtil.getBoldFont());
        taskTable.setRowHeight(30);

        // Set column widths
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // Status checkbox
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Category
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Priority
        taskTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Due
        taskTable.getColumnModel().getColumn(5).setPreferredWidth(60);  // Edit
        taskTable.getColumnModel().getColumn(6).setPreferredWidth(60);  // Delete

        // Add button renderers and editors for Edit and Delete columns
        taskTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Edit"));
        taskTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor("Edit"));
        
        taskTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("Delete"));
        taskTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor("Delete"));

        // Add checkbox listener for status column
        taskTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 0) { // Status checkbox column
                int row = e.getFirstRow();
                Boolean isCompleted = (Boolean) tableModel.getValueAt(row, 0);
                String taskId = (String) tableModel.getValueAt(row, 6); // Get task ID from delete column
                System.out.println("DEBUG: Task " + taskId + " completion status changed to: " + isCompleted);
                // TODO: Call controller to update task completion status
            }
        });

        // No sample data - start with empty table

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom stats panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        completedLabel = new JLabel("Completed: 0/0");
        completedLabel.setFont(FontUtil.getStandardFont());
        statsPanel.add(completedLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        inProgressLabel = new JLabel("In Progress: 0");
        inProgressLabel.setFont(FontUtil.getStandardFont());
        statsPanel.add(inProgressLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        notStartedLabel = new JLabel("Not Started: 0");
        notStartedLabel.setFont(FontUtil.getStandardFont());
        statsPanel.add(notStartedLabel);
        add(statsPanel, BorderLayout.SOUTH);
    }

    public void setTaskGateway(TaskGateway taskGateway) {
        this.taskGateway = taskGateway;
        refreshTasks();
    }

    public void setCategoryGateway(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (TodayTasksViewModel.TODAY_TASKS_STATE_PROPERTY.equals(evt.getPropertyName())) {
            TodayTasksState state = (TodayTasksState) evt.getNewValue();
            if (state != null && state.isRefreshNeeded()) {
                refreshTasks();
                // Reset refresh flag
                state.setRefreshNeeded(false);
                viewModel.setState(state);
            }
        }
    }

    private void refreshTasks() {
        tableModel.setRowCount(0);

        if (taskGateway != null) {
            List<Task> todaysTasks = taskGateway.getTodaysTasks();
            int completedCount = 0;
            int inProgressCount = 0;
            int notStartedCount = 0;

            for (Task task : todaysTasks) {
                // Get category name
                String categoryDisplay = "";
                String categoryId = task.getInfo().getCategory();
                if (categoryId != null && !categoryId.isEmpty() && categoryGateway != null) {
                    Category category = categoryGateway.getCategoryById(categoryId);
                    categoryDisplay = (category != null) ? category.getName() : "";
                }

                // Priority display
                String priorityDisplay = task.getPriority() != null ? task.getPriority().toString() : "";

                // Status checkbox
                boolean isCompleted = task.isCompleted();
                if (isCompleted) {
                    completedCount++;
                } else if (task.getDates().getBeginDate() != null && task.getDates().getBeginDate().isBefore(java.time.LocalDate.now().plusDays(1))) {
                    inProgressCount++;
                } else {
                    notStartedCount++;
                }

                // Due date display
                String dueDateDisplay = "";
                if (task.getDates().getDueDate() != null) {
                    dueDateDisplay = task.getDates().getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                }

                tableModel.addRow(new Object[]{
                    isCompleted, // Status checkbox
                    task.getInfo().getName(),
                    categoryDisplay,
                    priorityDisplay,
                    dueDateDisplay,
                    task.getInfo().getId(), // Task ID for Edit button
                    task.getInfo().getId()  // Task ID for Delete button
                });
            }

            // Update stats
            int totalTasks = todaysTasks.size();
            completedLabel.setText("Completed: " + completedCount + "/" + totalTasks);
            inProgressLabel.setText("In Progress: " + inProgressCount);
            notStartedLabel.setText("Not Started: " + notStartedCount);
        }
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setForeground(isError ? Color.RED : new Color(0, 128, 0));
        messageLabel.setText(message);
        Timer timer = new Timer(3000, e -> messageLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
    
    // Button renderer for Edit and Delete columns
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
            setOpaque(true);
            setFont(FontUtil.getStandardFont());
            setMargin(new Insets(2, 5, 2, 5));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setForeground(UIManager.getColor("Button.foreground"));
            setBackground(UIManager.getColor("Button.background"));
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
            button.setFont(FontUtil.getStandardFont());
            button.setMargin(new Insets(2, 5, 2, 5));
            label = text;
            isPushed = false;
            setClickCountToStart(1);

            button.addActionListener(e -> {
                isPushed = true;
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            taskId = (String) value;
            button.setText(label);
            isPushed = false;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                System.out.println("DEBUG: " + label + " button clicked for task ID: " + taskId);
                // TODO: Call appropriate controller based on label
                if ("Edit".equals(label)) {
                    // TODO: Call EditTodaysTaskController
                } else if ("Delete".equals(label)) {
                    // TODO: Call RemoveTaskFromTodayController
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
}