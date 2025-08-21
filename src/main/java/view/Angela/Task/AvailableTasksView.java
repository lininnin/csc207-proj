package view.Angela.Task;

import interface_adapter.Angela.task.available.AvailableTasksState;
import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.delete.DeleteTaskController;
import interface_adapter.Angela.task.delete.DeleteTaskState;
import interface_adapter.Angela.task.delete.DeleteTaskViewModel;
import interface_adapter.Angela.task.edit_available.EditAvailableTaskController;
import interface_adapter.Angela.task.edit_available.EditAvailableTaskViewModel;
import interface_adapter.Angela.task.edit_available.EditAvailableTaskState;
import use_case.Angela.task.TaskGateway;
import use_case.Angela.category.CategoryGateway;
import use_case.Angela.task.edit_available.EditAvailableTaskDataAccessInterface;
import entity.Angela.Task.TaskAvailable;
import entity.info.Info;
import entity.Category;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import view.FontUtil;

/**
 * View for displaying available tasks with inline edit and delete functionality.
 */
public class AvailableTasksView extends JPanel implements PropertyChangeListener {
    private final String viewName = "available tasks";

    private final AvailableTasksViewModel availableTasksViewModel;
    private final DeleteTaskViewModel deleteTaskViewModel;
    private EditAvailableTaskViewModel editAvailableTaskViewModel;
    private final DefaultTableModel tableModel;
    private final JTable taskTable;
    private final JLabel messageLabel;

    private DeleteTaskController deleteTaskController;
    private EditAvailableTaskController editAvailableTaskController;
    private TaskGateway taskGateway;
    private CategoryGateway categoryGateway;
    private EditAvailableTaskDataAccessInterface editTaskDataAccess;

    // Track which task is being edited (by ID, not row)
    private String editingTaskId = null;
    private int editingRow = -1; // Keep for UI purposes
    // Store components for editing
    private JTextField editNameField;
    private JComboBox<CategoryItem> editCategoryCombo;
    
    // Map row index to task ID for sorting functionality
    private final Map<Integer, String> rowToTaskIdMap = new HashMap<>();
    private JTextField editDescriptionField;
    private JCheckBox editOneTimeCheckbox;
    
    // Store original values for cancel/restore
    private String originalName;
    private String originalDescription;
    private String originalCategoryId;
    private boolean originalIsOneTime;

    // Helper class for category dropdown - defined early so it can be used in field declarations
    private static class CategoryItem {
        private final String id;
        private final String name;

        public CategoryItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }

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

        // Create table with One Time column
        String[] columnNames = {"Name", "Category", "Description", "One Time", "Edit", "Delete"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // In edit mode: ALL columns should be editable (including buttons for Save/Cancel)
                if (row == editingRow && editingTaskId != null) {
                    boolean editable = true;
                    System.out.println("DEBUG: isCellEditable - row: " + row + ", column: " + column + 
                                     ", editingRow: " + editingRow + ", in edit mode, editable: " + editable);
                    return editable;
                }
                // Normal mode: only button columns
                boolean editable = column == 4 || column == 5;
                if (editable) {
                    System.out.println("DEBUG: isCellEditable - row: " + row + ", column: " + column + 
                                     ", button column, editable: true");
                }
                return editable;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) { // One Time column
                    return Boolean.class;
                }
                return Object.class;
            }
        };

        taskTable = new JTable(tableModel);
        taskTable.setRowHeight(30);

        // Set column widths for wider layout (1100px total)
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Name - more space
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Category - more space
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(350); // Description - much more space
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // One Time
        taskTable.getColumnModel().getColumn(4).setPreferredWidth(90);  // Edit
        taskTable.getColumnModel().getColumn(5).setPreferredWidth(90);  // Delete

        // Custom renderer for One Time column to show checkmark
        taskTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(FontUtil.getStandardFont());
                
                // Show checkmark for true, empty for false
                if (value instanceof Boolean && (Boolean) value) {
                    setText("✓");
                } else {
                    setText("");
                }
                return this;
            }
        });

        // Custom renderer and editor for Edit column
        taskTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer("Edit"));
        taskTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor("Edit"));

        // Custom renderer and editor for Delete column
        taskTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Delete"));
        taskTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor("Delete"));

        // Add table sorting functionality
        addTableSorting();

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);

        // Initialize edit components
        initializeEditComponents();
    }

    private void initializeEditComponents() {
        editNameField = new JTextField();
        editNameField.setFont(FontUtil.getStandardFont());

        editCategoryCombo = new JComboBox<>();
        editCategoryCombo.setFont(FontUtil.getStandardFont());

        editDescriptionField = new JTextField();
        editDescriptionField.setFont(FontUtil.getStandardFont());

        editOneTimeCheckbox = new JCheckBox();
    }

    public void setDeleteTaskController(DeleteTaskController controller) {
        this.deleteTaskController = controller;
    }

    public void setEditAvailableTaskController(EditAvailableTaskController controller) {
        this.editAvailableTaskController = controller;
    }

    public void setEditAvailableTaskViewModel(EditAvailableTaskViewModel viewModel) {
        if (this.editAvailableTaskViewModel != null) {
            this.editAvailableTaskViewModel.removePropertyChangeListener(this);
        }
        this.editAvailableTaskViewModel = viewModel;
        if (viewModel != null) {
            viewModel.addPropertyChangeListener(this);
            System.out.println("DEBUG: AvailableTasksView registered as listener to EditAvailableTaskViewModel");
        }
    }

    public void setTaskGateway(TaskGateway taskGateway) {
        this.taskGateway = taskGateway;
        refreshTasks(); // Initial load
    }
    
    public void setCategoryGateway(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }
    
    public void setEditTaskDataAccess(EditAvailableTaskDataAccessInterface editTaskDataAccess) {
        this.editTaskDataAccess = editTaskDataAccess;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("DEBUG: propertyChange - property: " + evt.getPropertyName() + 
                         ", newValue: " + evt.getNewValue());
        
        if (AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY.equals(evt.getPropertyName())) {
            AvailableTasksState state = (AvailableTasksState) evt.getNewValue();
            if (state.isRefreshNeeded()) {
                refreshTasks();
                // Reset the refresh flag
                state.setRefreshNeeded(false);
                availableTasksViewModel.setState(state);
            }
        } else if (DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY.equals(evt.getPropertyName())) {
            DeleteTaskState state = (DeleteTaskState) evt.getNewValue();
            handleDeleteState(state);
        } else if (editAvailableTaskViewModel != null && 
                   EditAvailableTaskViewModel.EDIT_AVAILABLE_TASK_STATE_PROPERTY.equals(evt.getPropertyName())) {
            EditAvailableTaskState state = (EditAvailableTaskState) evt.getNewValue();
            System.out.println("DEBUG: Edit state property change detected, state: " + state);
            handleEditState(state);
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
            showMessage(state.getSuccessMessage(), false);
            refreshTasks();
        } else if (state.getError() != null) {
            showMessage(state.getError(), true);
        }
    }

    private void handleEditState(EditAvailableTaskState state) {
        if (state == null) {
            System.out.println("DEBUG: handleEditState - state is null, ignoring");
            return;
        }
        
        System.out.println("DEBUG: handleEditState - state received: " + state);
        
        if (state.getSuccessMessage() != null) {
            System.out.println("DEBUG: Showing success message: " + state.getSuccessMessage());
            showMessage(state.getSuccessMessage(), false);
            
            // On success, clear the editing state (but don't call exitEditMode to avoid recursion)
            editingTaskId = null;
            editingRow = -1;
            
            // Clear stored original values
            originalName = null;
            originalDescription = null;
            originalCategoryId = null;
            originalIsOneTime = false;
            
            // The refresh triggered by presenter will update the table
        } else if (state.getError() != null) {
            System.out.println("DEBUG: Showing error message: " + state.getError());
            showMessage(state.getError(), true);
            // Keep edit mode on error - don't change editingTaskId or editingRow
            System.out.println("DEBUG: Keeping edit mode due to error");
        }
    }

    private void showMessage(String message, boolean isError) {
        System.out.println("DEBUG: showMessage called - message: " + message + ", isError: " + isError);
        messageLabel.setForeground(isError ? Color.RED : new Color(0, 128, 0));
        messageLabel.setText(message);
        
        // Make sure the message is visible
        messageLabel.setVisible(true);
        messageLabel.repaint();
        
        // Use a longer delay for success messages since refresh might clear them
        int delay = isError ? 3000 : 5000;
        Timer timer = new Timer(delay, e -> {
            // Only clear if the same message is still displayed
            if (message.equals(messageLabel.getText())) {
                messageLabel.setText(" ");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void refreshTasks() {
        // Preserve any current message
        String currentMessage = messageLabel.getText();
        Color currentColor = messageLabel.getForeground();
        
        // If we're editing, cancel the edit (don't save)
        if (editingTaskId != null || editingRow != -1) {
            System.out.println("DEBUG: refreshTasks - cancelling edit mode without saving, editingTaskId: " + 
                             editingTaskId + ", editingRow: " + editingRow);
            
            // IMPORTANT: Restore original values instead of clearing
            if (editNameField != null && originalName != null) {
                editNameField.setText(originalName);
            }
            if (editDescriptionField != null && originalDescription != null) {
                editDescriptionField.setText(originalDescription);
            }
            if (editCategoryCombo != null && originalCategoryId != null) {
                // Set combo to original category
                boolean found = false;
                for (int i = 0; i < editCategoryCombo.getItemCount(); i++) {
                    CategoryItem item = editCategoryCombo.getItemAt(i);
                    if (item != null && item.getId().equals(originalCategoryId)) {
                        editCategoryCombo.setSelectedIndex(i);
                        found = true;
                        break;
                    }
                }
                if (!found && editCategoryCombo.getItemCount() > 0) {
                    editCategoryCombo.setSelectedIndex(0); // Default to "No Category"
                }
            }
            if (editOneTimeCheckbox != null) {
                editOneTimeCheckbox.setSelected(originalIsOneTime);
            }
            
            // Reset the editing state
            editingTaskId = null;
            int oldEditingRow = editingRow;
            editingRow = -1;
            
            // Restore default cell editors
            if (taskTable != null) {
                taskTable.getColumnModel().getColumn(0).setCellEditor(null);
                taskTable.getColumnModel().getColumn(1).setCellEditor(null);
                taskTable.getColumnModel().getColumn(2).setCellEditor(null);
                taskTable.getColumnModel().getColumn(3).setCellEditor(null);
            }
            
            // Fire table update to reset button appearance
            if (oldEditingRow >= 0 && oldEditingRow < tableModel.getRowCount()) {
                tableModel.fireTableRowsUpdated(oldEditingRow, oldEditingRow);
            }
        }
        
        // Don't preserve editing state during refresh - this ensures buttons reset properly
        tableModel.setRowCount(0);

        if (editTaskDataAccess != null) {
            // Get TaskAvailable objects to access isOneTime through proper interface
            List<TaskAvailable> taskAvailables = editTaskDataAccess.getAllAvailableTasksWithDetails();
            
            for (TaskAvailable taskAvailable : taskAvailables) {
                Info task = taskAvailable.getInfo();
                
                // Convert category ID to name for display
                String categoryDisplay = "";
                String categoryId = task.getCategory();
                if (categoryId != null && !categoryId.isEmpty() && categoryGateway != null) {
                    Category category = categoryGateway.getCategoryById(categoryId);
                    categoryDisplay = (category != null) ? category.getName() : "";
                }
                
                tableModel.addRow(new Object[]{
                        task.getName(),
                        categoryDisplay,
                        task.getDescription() != null ? task.getDescription() : "",
                        taskAvailable.isOneTime(), // Boolean for One Time
                        task.getId(), // Store ID for edit button
                        task.getId()  // Store ID for delete button
                });
            }
        } else if (taskGateway != null) {
            // Fallback to basic Info display if editTaskDataAccess is not available
            List<Info> tasks = taskGateway.getAllAvailableTasks();
            for (Info task : tasks) {
                String categoryDisplay = "";
                String categoryId = task.getCategory();
                if (categoryId != null && !categoryId.isEmpty() && categoryGateway != null) {
                    Category category = categoryGateway.getCategoryById(categoryId);
                    categoryDisplay = (category != null) ? category.getName() : "";
                }
                
                tableModel.addRow(new Object[]{
                        task.getName(),
                        categoryDisplay,
                        task.getDescription() != null ? task.getDescription() : "",
                        false, // Default to not one-time when using fallback
                        task.getId(),
                        task.getId()
                });
            }
        }

        // Restore the message if it was showing
        if (currentMessage != null && !currentMessage.trim().isEmpty() && !" ".equals(currentMessage)) {
            messageLabel.setText(currentMessage);
            messageLabel.setForeground(currentColor);
            System.out.println("DEBUG: Restored message after refresh: " + currentMessage);
        } else {
            messageLabel.setText(" ");
        }
    }

    private void enterEditMode(int row) {
        // Get the task ID - it's stored in column 4, but we need to get it before we change button text
        String taskId = (String) tableModel.getValueAt(row, 4);
        
        // IMPORTANT: Check if this is actually a task ID or button text
        if ("Save".equals(taskId) || "Edit".equals(taskId)) {
            System.out.println("ERROR: Got button text instead of task ID: " + taskId);
            // Try to get the task ID from our stored editing state
            if (editingTaskId != null) {
                taskId = editingTaskId;
            } else {
                System.out.println("ERROR: Cannot determine task ID for editing");
                return;
            }
        }
        
        System.out.println("DEBUG: enterEditMode called for row: " + row + ", taskId: " + taskId + 
                         ", current editingTaskId: " + editingTaskId + ", current editingRow: " + editingRow);
        
        if (editingTaskId != null || editingRow != -1) {
            // Already editing another row
            System.out.println("DEBUG: Already editing taskId " + editingTaskId + 
                             " at row " + editingRow + ", ignoring enterEditMode");
            return;
        }

        editingTaskId = taskId;
        editingRow = row;
        System.out.println("DEBUG: Set editingTaskId to: " + editingTaskId + ", editingRow to: " + editingRow);

        // Get current values
        String currentName = (String) tableModel.getValueAt(row, 0);
        String currentCategory = (String) tableModel.getValueAt(row, 1);
        String currentDescription = (String) tableModel.getValueAt(row, 2);
        boolean currentOneTime = (Boolean) tableModel.getValueAt(row, 3);

        // Store original values for potential cancel/restore
        originalName = currentName;
        originalDescription = currentDescription;
        originalIsOneTime = currentOneTime;
        // We'll set originalCategoryId after loading categories

        // Set up edit components with current values
        editNameField.setText(currentName);
        editDescriptionField.setText(currentDescription);
        editOneTimeCheckbox.setSelected(currentOneTime);

        // Load categories into combo box
        loadCategoriesIntoCombo(currentCategory);

        // Replace cell renderers with edit components - set single-click editing
        DefaultCellEditor nameEditor = new DefaultCellEditor(editNameField);
        nameEditor.setClickCountToStart(1);
        taskTable.getColumnModel().getColumn(0).setCellEditor(nameEditor);
        
        DefaultCellEditor categoryEditor = new DefaultCellEditor(editCategoryCombo);
        categoryEditor.setClickCountToStart(1);
        taskTable.getColumnModel().getColumn(1).setCellEditor(categoryEditor);
        
        DefaultCellEditor descEditor = new DefaultCellEditor(editDescriptionField);
        descEditor.setClickCountToStart(1);
        taskTable.getColumnModel().getColumn(2).setCellEditor(descEditor);
        
        DefaultCellEditor oneTimeEditor = new DefaultCellEditor(editOneTimeCheckbox);
        oneTimeEditor.setClickCountToStart(1);
        taskTable.getColumnModel().getColumn(3).setCellEditor(oneTimeEditor);

        // DON'T update button text in the model - this overwrites the task ID!
        // The ButtonRenderer will handle showing the correct text based on editingRow
        System.out.println("DEBUG: NOT updating button text in model - renderer will handle it");

        // Force table to repaint
        tableModel.fireTableRowsUpdated(row, row);
        
        // Also fire cell updates for button columns
        tableModel.fireTableCellUpdated(row, 4);
        tableModel.fireTableCellUpdated(row, 5);
        
        System.out.println("DEBUG: enterEditMode complete - editingTaskId: " + editingTaskId + 
                         ", editingRow: " + editingRow);
    }

    private void exitEditMode(int row, boolean save) {
        System.out.println("DEBUG: exitEditMode called - row: " + row + ", save: " + save + 
                         ", editingTaskId: " + editingTaskId + ", editingRow: " + editingRow);
        
        // Check if we're actually editing
        if (editingTaskId == null && editingRow == -1) {
            System.out.println("DEBUG: exitEditMode - not editing, returning");
            return;
        }

        if (save && editAvailableTaskController != null && editingTaskId != null) {
            System.out.println("DEBUG: Saving changes for taskId: " + editingTaskId);
            // Get the edited values
            String newName = editNameField.getText();
            String newDescription = editDescriptionField.getText();
            CategoryItem selectedCategory = (CategoryItem) editCategoryCombo.getSelectedItem();
            String newCategoryId = selectedCategory != null ? selectedCategory.getId() : "";
            boolean isOneTime = editOneTimeCheckbox.isSelected();
            
            System.out.println("DEBUG: Save values - name: " + newName + ", desc: " + newDescription + 
                             ", categoryId: " + newCategoryId + ", isOneTime: " + isOneTime);

            // Call controller to save
            editAvailableTaskController.execute(editingTaskId, newName, newDescription, newCategoryId, isOneTime);
            
            // IMPORTANT: Don't reset edit mode here - wait for success/error response
            // The handleEditState will decide whether to exit edit mode
            System.out.println("DEBUG: Save initiated - waiting for response to determine if edit mode should continue");
            return; // Return early, don't execute the reset code below
        } else {
            System.out.println("DEBUG: Cancel clicked - not saving changes. save=" + save + 
                             ", controller=" + (editAvailableTaskController != null) + 
                             ", editingTaskId=" + editingTaskId);
        }

        // Only reset if we're cancelling (save=false)
        if (!save) {
            System.out.println("DEBUG: Cancelling edit - resetting state");
            
            // Reset editing state
            editingTaskId = null;
            editingRow = -1;
            
            // Clear stored original values
            originalName = null;
            originalDescription = null;
            originalCategoryId = null;
            originalIsOneTime = false;

            // Restore default cell editors
            taskTable.getColumnModel().getColumn(0).setCellEditor(null);
            taskTable.getColumnModel().getColumn(1).setCellEditor(null);
            taskTable.getColumnModel().getColumn(2).setCellEditor(null);
            taskTable.getColumnModel().getColumn(3).setCellEditor(null);

            // Force the table to repaint to update button text
            tableModel.fireTableDataChanged();
            
            // Only refresh on cancel to restore original values
            refreshTasks();
        }
        System.out.println("DEBUG: exitEditMode complete - editingTaskId: " + editingTaskId + ", editingRow: " + editingRow);
    }

    private void loadCategoriesIntoCombo(String currentCategoryName) {
        editCategoryCombo.removeAllItems();
        editCategoryCombo.addItem(new CategoryItem("", "-- No Category --"));

        CategoryItem selectedItem = null;

        if (categoryGateway != null) {
            for (Category category : categoryGateway.getAllCategories()) {
                CategoryItem item = new CategoryItem(category.getId(), category.getName());
                editCategoryCombo.addItem(item);
                
                if (category.getName().equals(currentCategoryName)) {
                    selectedItem = item;
                    // Store the original category ID
                    originalCategoryId = category.getId();
                }
            }
        }

        if (selectedItem != null) {
            editCategoryCombo.setSelectedItem(selectedItem);
        } else {
            // No category selected
            originalCategoryId = "";
        }
    }

    // Button renderer for Edit and Delete columns
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        private final String defaultText;
        
        public ButtonRenderer(String text) {
            this.defaultText = text;
            setText(text);
            setOpaque(true);
            setFont(FontUtil.getStandardFont());
            setMargin(new Insets(2, 10, 2, 10));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            // Change button text based on edit mode
            if (row == editingRow && editingTaskId != null) {
                if (column == 4) {
                    setText("Save");
                    System.out.println("DEBUG: ButtonRenderer - Setting text to 'Save' for row " + row);
                } else if (column == 5) {
                    setText("Cancel");
                    System.out.println("DEBUG: ButtonRenderer - Setting text to 'Cancel' for row " + row);
                }
            } else {
                setText(defaultText);
            }
            
            setFont(FontUtil.getStandardFont());
            setForeground(UIManager.getColor("Button.foreground"));
            setBackground(UIManager.getColor("Button.background"));
            setBorderPainted(true);
            setContentAreaFilled(true);
            
            return this;
        }
    }

    // Button editor for Edit and Delete columns
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private String taskId;
        private boolean isPushed;
        private int row;
        private int column;

        public ButtonEditor(String text) {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            button.setMargin(new Insets(2, 10, 2, 10));
            button.setFont(FontUtil.getStandardFont());
            label = text;
            isPushed = false;
            setClickCountToStart(1);

            button.addActionListener(e -> {
                System.out.println("DEBUG: Button action listener triggered - label: " + label);
                isPushed = true;
                SwingUtilities.invokeLater(() -> {
                    fireEditingStopped();
                });
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            this.column = column;
            taskId = (String) value;
            
            // Set button text based on edit mode
            if (row == editingRow && editingTaskId != null) {
                button.setText(column == 4 ? "Save" : "Cancel");
            } else {
                button.setText(label);
            }
            
            System.out.println("DEBUG: getTableCellEditorComponent - row: " + row + ", column: " + column + 
                             ", editingRow: " + editingRow + ", editingTaskId: " + editingTaskId +
                             ", buttonText: " + button.getText() + ", taskId: " + taskId);
            
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            System.out.println("DEBUG: getCellEditorValue - isPushed: " + isPushed + ", row: " + row + 
                             ", editingRow: " + editingRow + ", editingTaskId: " + editingTaskId +
                             ", label: " + label + ", buttonText: " + button.getText());
            
            if (isPushed) {
                // Check button text to determine action (more reliable than row comparison)
                String buttonText = button.getText();
                System.out.println("DEBUG: Button pushed with text: " + buttonText);
                
                if ("Save".equals(buttonText)) {
                    System.out.println("DEBUG: Save button clicked for row " + row);
                    SwingUtilities.invokeLater(() -> {
                        exitEditMode(row, true);
                    });
                } else if ("Cancel".equals(buttonText)) {
                    System.out.println("DEBUG: Cancel button clicked for row " + row);
                    SwingUtilities.invokeLater(() -> {
                        exitEditMode(row, false);
                    });
                } else if ("Delete".equals(buttonText) && deleteTaskController != null) {
                    System.out.println("DEBUG: Delete button clicked for taskId: " + taskId);
                    SwingUtilities.invokeLater(() -> {
                        deleteTaskController.execute(taskId, true);
                    });
                } else if ("Edit".equals(buttonText)) {
                    System.out.println("DEBUG: Edit button clicked - calling enterEditMode for row " + row);
                    SwingUtilities.invokeLater(() -> {
                        enterEditMode(row);
                    });
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

    /**
     * Adds table sorting functionality to clickable column headers.
     * Supports sorting by Name, Category, and Description.
     */
    private void addTableSorting() {
        JTableHeader header = taskTable.getTableHeader();
        header.addMouseListener(new MouseAdapter() {
            private int lastSortColumn = -1;
            private boolean ascending = true;

            @Override
            public void mouseClicked(MouseEvent e) {
                int columnIndex = header.columnAtPoint(e.getPoint());
                
                // Only allow sorting on sortable columns (skip One Time, Edit, Delete columns)
                if (columnIndex == 3 || columnIndex == 4 || columnIndex == 5) {
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

    public String getViewName() {
        return viewName;
    }
}