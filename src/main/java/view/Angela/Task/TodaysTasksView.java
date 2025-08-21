package view.Angela.Task;

import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.mark_complete.MarkTaskCompleteController;
import interface_adapter.Angela.task.edit_today.EditTodayTaskController;
import interface_adapter.Angela.task.edit_today.EditTodayTaskViewModel;
import interface_adapter.Angela.task.remove_from_today.RemoveFromTodayController;
import entity.Angela.Task.Task;
import entity.Category;
import use_case.Angela.task.TaskGateway;
import use_case.Angela.category.CategoryGateway;
import view.DueDatePickerPanel;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
    private MarkTaskCompleteController markTaskCompleteController;
    private EditTodayTaskController editTodayTaskController;
    private RemoveFromTodayController removeFromTodayController;
    
    // Inline editing state
    private String editingTaskId = null;
    private int editingRow = -1;
    
    // Edit components
    private JComboBox<String> editPriorityCombo;
    private DatePicker editDatePicker; // Use DatePicker directly for inline editing
    
    // Store original values for cancel
    private Task.Priority originalPriority;
    private LocalDate originalDueDate;
    
    // Map row index to task ID for button handling
    private final Map<Integer, String> rowToTaskIdMap = new HashMap<>();

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
                // Status checkbox (0), Priority (3), Due Date (4), and button columns (5, 6) are editable
                // But Priority and Due Date are only editable when in edit mode for that row
                if (column == 0 || column == 5 || column == 6) {
                    return true;
                }
                if ((column == 3 || column == 4) && editingRow == row) {
                    return true;
                }
                return false;
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

        // Set column widths with good proportions for adjustable panel (default 720px)
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(65);  // Status checkbox
        taskTable.getColumnModel().getColumn(0).setMinWidth(50);
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(220); // Name - good space
        taskTable.getColumnModel().getColumn(1).setMinWidth(120);
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(130); // Category
        taskTable.getColumnModel().getColumn(2).setMinWidth(80);
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(110); // Priority
        taskTable.getColumnModel().getColumn(3).setMinWidth(70);
        taskTable.getColumnModel().getColumn(4).setPreferredWidth(130); // Due
        taskTable.getColumnModel().getColumn(4).setMinWidth(80);
        taskTable.getColumnModel().getColumn(5).setPreferredWidth(75);  // Edit
        taskTable.getColumnModel().getColumn(5).setMinWidth(60);
        taskTable.getColumnModel().getColumn(6).setPreferredWidth(75);  // Delete
        taskTable.getColumnModel().getColumn(6).setMinWidth(60);

        // Add button renderers and editors for Edit and Delete columns
        taskTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Edit"));
        taskTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor("Edit"));
        
        taskTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("Delete"));
        taskTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor("Delete"));

        // Add checkbox listener for status column
        taskTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 0 && markTaskCompleteController != null) { // Status checkbox column
                int row = e.getFirstRow();
                Boolean isCompleted = (Boolean) tableModel.getValueAt(row, 0);
                String taskId = rowToTaskIdMap.get(row); // Get task ID from our map
                System.out.println("DEBUG: Task " + taskId + " completion status changed to: " + isCompleted);
                // Call controller to update task completion status
                if (taskId != null && isCompleted != null) {
                    markTaskCompleteController.execute(taskId, isCompleted);
                }
            }
        });

        // Add table sorting functionality
        addTableSorting();

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
        
        // Initialize inline edit components
        initializeEditComponents();
    }
    
    private void initializeEditComponents() {
        // Priority combo box
        editPriorityCombo = new JComboBox<>(new String[]{"None", "HIGH", "MEDIUM", "LOW"});
        editPriorityCombo.setFont(FontUtil.getStandardFont());
        
        // Due date picker - use DatePicker directly for inline editing
        DatePickerSettings settings = new DatePickerSettings();
        settings.setAllowEmptyDates(true);
        settings.setAllowKeyboardEditing(false);
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        editDatePicker = new DatePicker(settings);
        settings.setDateRangeLimits(LocalDate.now(), null); // Only allow today or future dates
        editDatePicker.setPreferredSize(new Dimension(120, 25));
    }

    public void setTaskGateway(TaskGateway taskGateway) {
        this.taskGateway = taskGateway;
        refreshTasks();
    }

    public void setCategoryGateway(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    public void setMarkTaskCompleteController(MarkTaskCompleteController controller) {
        this.markTaskCompleteController = controller;
    }

    public void setEditTodayTaskController(EditTodayTaskController controller) {
        this.editTodayTaskController = controller;
    }

    public void setRemoveFromTodayController(RemoveFromTodayController controller) {
        this.removeFromTodayController = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (TodayTasksViewModel.TODAY_TASKS_STATE_PROPERTY.equals(evt.getPropertyName())) {
            TodayTasksState state = (TodayTasksState) evt.getNewValue();
            if (state != null) {
                // Display any messages
                if (state.getSuccessMessage() != null) {
                    showMessage(state.getSuccessMessage(), false);
                } else if (state.getError() != null) {
                    showMessage(state.getError(), true);
                }
                
                // Refresh if needed
                if (state.isRefreshNeeded()) {
                    refreshTasks();
                    // Reset refresh flag
                    state.setRefreshNeeded(false);
                    viewModel.setState(state);
                }
            }
        }
    }

    private void refreshTasks() {
        tableModel.setRowCount(0);
        rowToTaskIdMap.clear();

        if (taskGateway != null) {
            List<Task> todaysTasks = taskGateway.getTodaysTasks();
            int completedCount = 0;
            int inProgressCount = 0;
            int notStartedCount = 0;
            int rowIndex = 0;

            for (Task task : todaysTasks) {
                // Skip overdue tasks - they should only appear in the Overdue panel
                if (task.isOverdue()) {
                    continue;
                }
                
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

                // Check if this task is being edited (use row index, not task ID)
                boolean isBeingEdited = (editingRow == rowIndex);
                
                // Store task ID for this row (use Task's ID, not Info's ID)
                rowToTaskIdMap.put(rowIndex, task.getId());
                
                tableModel.addRow(new Object[]{
                    isCompleted, // Status checkbox
                    task.getInfo().getName(),
                    categoryDisplay,
                    priorityDisplay,
                    dueDateDisplay,
                    isBeingEdited ? "Save" : "Edit",    // Edit/Save button
                    isBeingEdited ? "Cancel" : "Delete"  // Delete/Cancel button
                });
                
                if (isBeingEdited) {
                    System.out.println("DEBUG: Row " + rowIndex + " is in edit mode during refresh");
                }
                
                rowIndex++;
            }

            // Update stats (only count non-overdue tasks)
            int totalNonOverdueTasks = completedCount + inProgressCount + notStartedCount;
            completedLabel.setText("Completed: " + completedCount + "/" + totalNonOverdueTasks);
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
    
    private void handleEditTask(String taskId) {
        System.out.println("DEBUG: handleEditTask called with taskId: " + taskId);
        System.out.println("DEBUG: editTodayTaskController is " + (editTodayTaskController != null ? "NOT NULL" : "NULL"));
        
        // The taskId passed in is actually the task ID we need
        // Find the row for this task by comparing with tasks from gateway
        // IMPORTANT: Skip overdue tasks since they're not shown in the table
        int row = -1;
        List<Task> todaysTasks = taskGateway.getTodaysTasks();
        int tableRow = 0;
        for (Task task : todaysTasks) {
            // Skip overdue tasks - they're not in the table
            if (task.isOverdue()) {
                continue;
            }
            // Compare with Task's ID, not Info's ID
            if (task.getId().equals(taskId)) {
                row = tableRow;
                break;
            }
            tableRow++;
        }
        
        System.out.println("DEBUG: Found row " + row + " for taskId " + taskId);
        
        if (row >= 0) {
            enterEditMode(row);
        } else {
            showMessage("Task not found in table", true);
        }
    }
    
    private void enterEditMode(int row) {
        // Get the task ID from the tasks list, skipping overdue tasks
        List<Task> todaysTasks = taskGateway.getTodaysTasks();
        
        // Find the actual task at this row, skipping overdue tasks
        int currentRow = 0;
        Task targetTask = null;
        for (Task task : todaysTasks) {
            // Skip overdue tasks - they're not in the table
            if (task.isOverdue()) {
                continue;
            }
            if (currentRow == row) {
                targetTask = task;
                break;
            }
            currentRow++;
        }
        
        if (targetTask == null) {
            showMessage("Invalid row selected", true);
            return;
        }
        
        // Use Task's ID, not Info's ID, for editing Today's tasks
        String taskId = targetTask.getId();
        
        System.out.println("DEBUG: enterEditMode for row: " + row + ", taskId: " + taskId);
        
        // ALWAYS exit any previous edit mode first
        if (editingRow != -1 && editingRow != row) {
            System.out.println("DEBUG: Exiting edit mode for row " + editingRow + " before entering row " + row);
            exitEditMode(editingRow, false); // Cancel the previous edit
        }
        
        // If clicking Edit on the same row that's already editing, do nothing
        if (editingRow == row) {
            System.out.println("DEBUG: Row " + row + " is already in edit mode");
            return;
        }
        
        // Now set the new editing state
        editingTaskId = taskId;
        editingRow = row;
        System.out.println("DEBUG: Set editingTaskId = " + editingTaskId + ", editingRow = " + editingRow);
        
        // Get current values
        String currentPriorityStr = (String) tableModel.getValueAt(row, 3);
        String currentDueDateStr = (String) tableModel.getValueAt(row, 4);
        
        // Parse priority
        if (currentPriorityStr == null || currentPriorityStr.isEmpty()) {
            originalPriority = null;
            editPriorityCombo.setSelectedItem("None");
        } else {
            try {
                originalPriority = Task.Priority.valueOf(currentPriorityStr);
                editPriorityCombo.setSelectedItem(currentPriorityStr);
            } catch (IllegalArgumentException e) {
                originalPriority = null;
                editPriorityCombo.setSelectedItem("None");
            }
        }
        
        // Parse due date
        if (currentDueDateStr == null || currentDueDateStr.isEmpty()) {
            originalDueDate = null;
            editDatePicker.clear();
        } else {
            try {
                originalDueDate = LocalDate.parse(currentDueDateStr);
                editDatePicker.setDate(originalDueDate);
            } catch (Exception e) {
                originalDueDate = null;
                editDatePicker.clear();
            }
        }
        
        // Set up cell editors for Priority and Due Date columns
        DefaultCellEditor priorityEditor = new DefaultCellEditor(editPriorityCombo);
        priorityEditor.setClickCountToStart(1);
        taskTable.getColumnModel().getColumn(3).setCellEditor(priorityEditor);
        
        // For due date, we need a custom editor that uses the date picker directly
        DefaultCellEditor dueDateEditor = new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                                                         boolean isSelected, int row, int column) {
                // The DatePicker is already properly sized
                return editDatePicker;
            }
            
            @Override
            public Object getCellEditorValue() {
                LocalDate date = editDatePicker.getDate();
                return date != null ? date.toString() : "";
            }
            
            @Override
            public boolean stopCellEditing() {
                // Ensure the date picker value is captured
                return super.stopCellEditing();
            }
        };
        dueDateEditor.setClickCountToStart(1);
        taskTable.getColumnModel().getColumn(4).setCellEditor(dueDateEditor);
        
        // Stop any active cell editing first
        if (taskTable.isEditing()) {
            taskTable.getCellEditor().stopCellEditing();
        }
        
        // Change button text from "Edit" to "Save" and "Delete" to "Cancel"
        tableModel.setValueAt("Save", row, 5);
        tableModel.setValueAt("Cancel", row, 6);
        
        // Verify the values were set
        System.out.println("DEBUG: After setValueAt - Column 5: '" + tableModel.getValueAt(row, 5) + "', Column 6: '" + tableModel.getValueAt(row, 6) + "'");
        
        // Force the model to notify listeners about these specific cells
        tableModel.fireTableCellUpdated(row, 5);
        tableModel.fireTableCellUpdated(row, 6);
        
        // Force complete row refresh
        SwingUtilities.invokeLater(() -> {
            // Re-fire the updates on the EDT to ensure they take effect
            tableModel.fireTableRowsUpdated(row, row);
            taskTable.revalidate();
            taskTable.repaint();
        });
        System.out.println("DEBUG: Changed buttons to Save/Cancel for row " + row);
        
        // Manually verify the change
        SwingUtilities.invokeLater(() -> {
            String col5 = (String) tableModel.getValueAt(row, 5);
            String col6 = (String) tableModel.getValueAt(row, 6);
            System.out.println("DEBUG: After invokeLater - Row " + row + " buttons: col5='" + col5 + "', col6='" + col6 + "'");
        });
    }
    
    private void exitEditMode(int row, boolean save) {
        System.out.println("DEBUG: exitEditMode - row: " + row + ", save: " + save);
        
        // Check if we're actually editing this row
        if (editingRow != row) {
            System.out.println("DEBUG: Not editing row " + row + ", actual editingRow: " + editingRow);
            return;
        }
        
        if (save && editTodayTaskController != null && editingTaskId != null) {
            // Get edited values
            String selectedPriority = (String) editPriorityCombo.getSelectedItem();
            Task.Priority priority = "None".equals(selectedPriority) ? null : Task.Priority.valueOf(selectedPriority);
            LocalDate dueDate = editDatePicker.getDate();
            
            System.out.println("DEBUG: Saving - priority: " + priority + ", dueDate: " + dueDate);
            
            // Call controller to save
            editTodayTaskController.execute(editingTaskId, priority, dueDate);
        }
        
        // Clear editing state BEFORE refresh
        editingTaskId = null;
        editingRow = -1;
        
        // Remove custom cell editors
        taskTable.getColumnModel().getColumn(3).setCellEditor(null);
        taskTable.getColumnModel().getColumn(4).setCellEditor(null);
        
        // Stop any active editing
        if (taskTable.isEditing()) {
            taskTable.getCellEditor().stopCellEditing();
        }
        
        // Refresh to restore buttons and values
        refreshTasks();
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
            // Get the actual button text from the table model
            String buttonText = (String) table.getModel().getValueAt(row, column);
            setText(buttonText);
            
            // Debug: Always log for edit column during edit mode
            if (editingRow != -1 && row == editingRow && column == 5) {
                System.out.println("DEBUG: ButtonRenderer - edit mode row " + row + ", column 5 (Edit/Save), text from model: '" + buttonText + "'");
            }
            
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
        private int currentRow;

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
                // Don't fire editing stopped here - it causes issues with row -1
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            // Validate row is in bounds
            if (row < 0 || row >= table.getRowCount()) {
                System.out.println("ERROR: Invalid row " + row + " in ButtonEditor");
                return button;
            }
            
            currentRow = row;
            // Get the task ID from our map, NOT from the cell value
            taskId = rowToTaskIdMap.get(row);
            
            // The value parameter contains the button text (Edit/Save/Delete/Cancel)
            String buttonText = (String) value;
            button.setText(buttonText);
            
            System.out.println("DEBUG: ButtonEditor - row: " + row + ", taskId from map: " + taskId + ", buttonText: " + buttonText);
            
            isPushed = false;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed && currentRow >= 0) {  // Check currentRow is valid
                String buttonText = button.getText();
                System.out.println("DEBUG: Button " + buttonText + " clicked at row: " + currentRow + ", taskId: " + taskId);
                
                SwingUtilities.invokeLater(() -> {
                    // Handle button actions after editor completes
                    if ("Edit".equals(buttonText)) {
                        if (taskId != null) {
                            handleEditTask(taskId);
                        } else {
                            System.out.println("ERROR: taskId is null when Edit clicked!");
                            showMessage("Error: Could not identify task", true);
                        }
                    } else if ("Save".equals(buttonText)) {
                        exitEditMode(currentRow, true);
                    } else if ("Cancel".equals(buttonText)) {
                        exitEditMode(currentRow, false);
                    } else if ("Delete".equals(buttonText)) {
                        if (removeFromTodayController != null && taskId != null) {
                            // Show confirmation dialog
                            int result = JOptionPane.showConfirmDialog(
                                TodaysTasksView.this,
                                "Remove this task from Today's list?",
                                "Confirm Removal",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                            );
                            
                            if (result == JOptionPane.YES_OPTION) {
                                removeFromTodayController.execute(taskId);
                            }
                        } else {
                            System.out.println("ERROR: removeFromTodayController is null or taskId is null!");
                            showMessage("Error: Cannot remove task", true);
                        }
                    }
                });
            }
            isPushed = false;
            return button.getText();
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            currentRow = -1; // Reset current row
            return super.stopCellEditing();
        }
        
        @Override
        protected void fireEditingStopped() {
            // Only fire if we have a valid row
            if (currentRow >= 0) {
                super.fireEditingStopped();
            }
        }
    }

    /**
     * Adds table sorting functionality to clickable column headers.
     * Supports sorting by Name, Category, Priority, and Due Date.
     */
    private void addTableSorting() {
        JTableHeader header = taskTable.getTableHeader();
        header.addMouseListener(new MouseAdapter() {
            private int lastSortColumn = -1;
            private boolean ascending = true;

            @Override
            public void mouseClicked(MouseEvent e) {
                int columnIndex = header.columnAtPoint(e.getPoint());
                
                // Only allow sorting on sortable columns (skip Status, Edit, Delete buttons)
                if (columnIndex == 0 || columnIndex == 5 || columnIndex == 6) {
                    return;
                }
                
                // Toggle sort order if clicking same column
                if (columnIndex == lastSortColumn) {
                    ascending = !ascending;
                } else {
                    ascending = true;
                    lastSortColumn = columnIndex;
                }
                
                sortTableByColumn(columnIndex, ascending);
            }
        });
    }

    /**
     * Sorts the table by the specified column.
     */
    private void sortTableByColumn(int columnIndex, boolean ascending) {
        // Create list of row data with task IDs for sorting
        List<RowData> rowDataList = new ArrayList<>();
        
        // Collect all rows and their corresponding task IDs
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object[] row = new Object[tableModel.getColumnCount()];
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                row[j] = tableModel.getValueAt(i, j);
            }
            rowDataList.add(new RowData(row, rowToTaskIdMap.get(i)));
        }
        
        // Sort rows based on the selected column
        rowDataList.sort((rowData1, rowData2) -> {
            Object val1 = rowData1.row[columnIndex];
            Object val2 = rowData2.row[columnIndex];
            
            // Handle null values
            if (val1 == null && val2 == null) return 0;
            if (val1 == null) return ascending ? -1 : 1;
            if (val2 == null) return ascending ? 1 : -1;
            
            // Sort by string comparison for most columns
            String str1 = val1.toString();
            String str2 = val2.toString();
            
            int result = str1.compareToIgnoreCase(str2);
            return ascending ? result : -result;
        });
        
        // Clear and repopulate the table
        tableModel.setRowCount(0);
        rowToTaskIdMap.clear();
        
        for (int i = 0; i < rowDataList.size(); i++) {
            RowData rowData = rowDataList.get(i);
            tableModel.addRow(rowData.row);
            rowToTaskIdMap.put(i, rowData.taskId);
        }
    }

    /**
     * Helper class to keep row data and task ID together during sorting.
     */
    private static class RowData {
        final Object[] row;
        final String taskId;
        
        RowData(Object[] row, String taskId) {
            this.row = row;
            this.taskId = taskId;
        }
    }
}