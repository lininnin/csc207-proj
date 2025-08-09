package view.Angela.Task;

import interface_adapter.Angela.task.add_to_today.AddTaskToTodayController;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayViewModel;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayState;
import use_case.Angela.task.add_to_today.AddToTodayDataAccessInterface;
import use_case.Angela.category.CategoryGateway;
import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.Category;
import view.DueDatePickerPanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import view.FontUtil;

/**
 * View for adding tasks to today's list.
 * Allows selection of available tasks with optional priority and due date.
 */
public class AddToTodayView extends JPanel implements PropertyChangeListener {
    private final JComboBox<TaskItem> taskDropdown;
    private final JComboBox<String> priorityDropdown;
    private final DueDatePickerPanel dueDatePicker;
    private final JButton addButton;
    private final JLabel messageLabel;

    private AddTaskToTodayController controller;
    private AddTaskToTodayViewModel viewModel;
    private AddToTodayDataAccessInterface dataAccess;
    private CategoryGateway categoryGateway;

    public AddToTodayView(AddTaskToTodayViewModel viewModel) {
        System.out.println("DEBUG: AddToTodayView constructor called");
        this.viewModel = viewModel;
        if (viewModel != null) {
            viewModel.addPropertyChangeListener(this);
        }
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Add Today's Task"));

        // Message label for feedback
        messageLabel = new JLabel(" ");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel, BorderLayout.NORTH);

        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3); // Reduced spacing
        gbc.anchor = GridBagConstraints.WEST;

        // Select Task Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Select Task Name:"), gbc);
        gbc.gridx = 1;
        taskDropdown = new JComboBox<>();
        taskDropdown.setPreferredSize(new Dimension(200, 25)); // Restored width
        taskDropdown.setFont(FontUtil.getStandardFont()); // Fix font for selected value
        // Fix rendering issue - ensure ALL items display with black text
        taskDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TaskItem) {
                    setText(((TaskItem) value).toString());
                }
                // CRITICAL: Force black text for ALL items to fix white text bug
                if (!isSelected) {
                    setForeground(Color.BLACK);
                    setBackground(Color.WHITE);
                }
                return this;
            }
        });
        formPanel.add(taskDropdown, gbc);

        // Priority
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Priority:"), gbc);
        gbc.gridx = 1;
        priorityDropdown = new JComboBox<>(new String[]{"None", "HIGH", "MEDIUM", "LOW"});
        priorityDropdown.setPreferredSize(new Dimension(200, 25)); // Restored width
        priorityDropdown.setFont(FontUtil.getStandardFont()); // Fix font for selected value
        // Fix rendering issue - ensure ALL items display with black text
        priorityDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                // Ensure text is set properly
                String text = value != null ? value.toString() : "";
                setText(text);
                // DEBUG: Check for missing uppercase letters
                if (text.contains("L")) {
                    System.out.println("DEBUG: Priority renderer text: '" + text + "' chars: " + 
                        java.util.Arrays.toString(text.toCharArray()));
                }
                // Use FontUtil to fix missing W, E, L, F uppercase letters
                setFont(FontUtil.getStandardFont());
                // CRITICAL: Force black text for ALL items to fix white text bug
                if (!isSelected) {
                    setForeground(Color.BLACK);
                    setBackground(Color.WHITE);
                }
                return this;
            }
        });
        formPanel.add(priorityDropdown, gbc);

        // Due Date
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Due Date:"), gbc);
        gbc.gridx = 1;
        dueDatePicker = new DueDatePickerPanel();
        formPanel.add(dueDatePicker, gbc);

        // Add button
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addButton = new JButton("Add to Today");
        addButton.addActionListener(e -> handleAddToToday());
        formPanel.add(addButton, gbc);

        
        // Test button for overdue functionality (for demo purposes only)
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton testOverdueButton = new JButton("Test: Add with Yesterday");
        testOverdueButton.setForeground(Color.RED);
        testOverdueButton.setToolTipText("FOR TESTING ONLY: Adds selected task with yesterday's date to test overdue functionality");
        testOverdueButton.addActionListener(e -> handleAddWithYesterday());
        formPanel.add(testOverdueButton, gbc);
        
        add(formPanel, BorderLayout.CENTER);
    }

    public void setAddTaskToTodayController(AddTaskToTodayController controller) {
        this.controller = controller;
    }

    public void setDataAccess(AddToTodayDataAccessInterface dataAccess) {
        System.out.println("DEBUG: AddToTodayView.setDataAccess() called with dataAccess: " + dataAccess);
        this.dataAccess = dataAccess;
        refreshTasks();
    }
    
    public void setCategoryGateway(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
        refreshTasks(); // Refresh to show categories
    }

    private void refreshTasks() {
        System.out.println("DEBUG: AddToTodayView.refreshTasks() called");
        
        taskDropdown.removeAllItems();
        taskDropdown.addItem(new TaskItem("", "-- Select Task --"));

        if (dataAccess != null) {
            System.out.println("DEBUG: dataAccess is not null, fetching tasks");
            List<TaskAvailable> tasks = dataAccess.getAllAvailableTasksWithDetails();
            System.out.println("DEBUG: Number of tasks received: " + tasks.size());
            
            for (TaskAvailable task : tasks) {
                String categoryName = null;
                // Get category name if category ID exists
                if (categoryGateway != null && task.getInfo().getCategory() != null) {
                    Category category = categoryGateway.getCategoryById(task.getInfo().getCategory());
                    if (category != null) {
                        categoryName = category.getName();
                    }
                }
                System.out.println("DEBUG: Adding task to dropdown - ID: " + task.getId() + 
                                 ", Name: " + task.getInfo().getName() + 
                                 ", Category: " + categoryName);
                taskDropdown.addItem(new TaskItem(task.getId(), task.getInfo().getName(), categoryName));
            }
        } else {
            System.out.println("DEBUG: dataAccess is null!");
        }
        
        System.out.println("DEBUG: Total items in dropdown: " + taskDropdown.getItemCount());
    }

    private void handleAddToToday() {
        TaskItem selectedTask = (TaskItem) taskDropdown.getSelectedItem();
        if (selectedTask == null || selectedTask.getId().isEmpty()) {
            showMessage("Please select a task", true);
            return;
        }

        // Parse priority
        String priorityStr = (String) priorityDropdown.getSelectedItem();
        Task.Priority priority = null;
        if (priorityStr != null && !"None".equals(priorityStr)) {
            priority = Task.Priority.valueOf(priorityStr);
        }

        // Get due date from picker
        LocalDate dueDate = dueDatePicker.getSelectedDate();
        
        // Validate that due date is not in the past
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            showMessage("Due date cannot be in the past. Use 'Test: Add with Yesterday' button for testing overdue functionality.", true);
            return;
        }

        // Call controller
        if (controller != null) {
            controller.execute(selectedTask.getId(), priority, dueDate);
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (AddTaskToTodayViewModel.ADD_TO_TODAY_STATE_PROPERTY.equals(evt.getPropertyName())) {
            AddTaskToTodayState state = (AddTaskToTodayState) evt.getNewValue();
            if (state != null) {
                // Check for refresh needed (e.g., when new tasks are created)
                if (state.isRefreshNeeded()) {
                    System.out.println("DEBUG: Refresh needed flag detected, refreshing task list");
                    refreshTasks();
                    // Reset the flag
                    state.setRefreshNeeded(false);
                    viewModel.setState(state);
                }
                
                if (state.getSuccessMessage() != null) {
                    showMessage(state.getSuccessMessage(), false);
                    // Clear form on success
                    taskDropdown.setSelectedIndex(0);
                    priorityDropdown.setSelectedIndex(0);
                    dueDatePicker.clear();
                    // Refresh task list in case one-time tasks should be removed
                    refreshTasks();
                } else if (state.getError() != null) {
                    showMessage(state.getError(), true);
                }
            }
        }
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setForeground(isError ? Color.RED : new Color(0, 128, 0));
        messageLabel.setText(message);
        Timer timer = new Timer(3000, e -> messageLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * Test method to add a task with yesterday's date for testing overdue functionality.
     * This is for demo purposes only.
     */
    private void handleAddWithYesterday() {
        TaskItem selectedTask = (TaskItem) taskDropdown.getSelectedItem();
        if (selectedTask == null || selectedTask.getId().isEmpty()) {
            showMessage("Please select a task", true);
            return;
        }

        // Parse priority
        String priorityStr = (String) priorityDropdown.getSelectedItem();
        Task.Priority priority = null;
        if (priorityStr != null && !"None".equals(priorityStr)) {
            priority = Task.Priority.valueOf(priorityStr);
        }

        // Set due date to yesterday for testing overdue functionality
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // Call controller with yesterday's date
        if (controller != null) {
            controller.execute(selectedTask.getId(), priority, yesterday);
            showMessage("Task added with yesterday's date for testing overdue functionality", false);
        }
    }

    // Helper class for ComboBox items
    private static class TaskItem {
        private final String id;
        private final String name;
        private final String category;

        public TaskItem(String id, String name) {
            this(id, name, null);
        }
        
        public TaskItem(String id, String name, String category) {
            this.id = id;
            this.name = name;
            this.category = category;
        }

        public String getId() { return id; }

        @Override
        public String toString() { 
            // Display name with category for better distinction
            if (category != null && !category.isEmpty()) {
                return name + " [" + category + "]";
            }
            return name;
        }
    }
}