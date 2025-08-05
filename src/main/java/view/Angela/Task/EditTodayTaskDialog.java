package view.Angela.Task;

import entity.Angela.Task.Task;
import interface_adapter.Angela.task.edit_today.EditTodayTaskController;
import interface_adapter.Angela.task.edit_today.EditTodayTaskViewModel;
import interface_adapter.Angela.task.edit_today.EditTodayTaskState;
import view.FontUtil;
import view.DueDatePickerPanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

/**
 * Dialog for editing today's tasks.
 * Only allows editing of priority and due date - name, description, and category are read-only.
 */
public class EditTodayTaskDialog extends JDialog implements PropertyChangeListener {
    private final EditTodayTaskViewModel viewModel;
    private EditTodayTaskController controller;
    
    // Current task information
    private String currentTaskId;
    private String currentTaskName;
    
    // UI Components
    private JLabel taskNameLabel;
    private JComboBox<String> priorityComboBox;
    private DueDatePickerPanel dueDatePicker;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel messageLabel;

    /**
     * Creates a new edit today task dialog.
     * 
     * @param parent The parent frame
     * @param viewModel The view model for this dialog
     */
    public EditTodayTaskDialog(JFrame parent, EditTodayTaskViewModel viewModel) {
        super(parent, "Edit Today's Task", true);
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
        
        initializeUI();
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    /**
     * Sets the controller for this dialog.
     * 
     * @param controller The edit today task controller
     */
    public void setController(EditTodayTaskController controller) {
        this.controller = controller;
    }

    /**
     * Shows the dialog with the task to edit.
     * 
     * @param taskId The ID of the task
     * @param taskName The name of the task (read-only display)
     * @param currentPriority The current priority
     * @param currentDueDate The current due date
     */
    public void showEditDialog(String taskId, String taskName, Task.Priority currentPriority, LocalDate currentDueDate) {
        this.currentTaskId = taskId;
        this.currentTaskName = taskName;
        
        // Set task name (read-only)
        taskNameLabel.setText(taskName);
        
        // Set current priority
        if (currentPriority != null) {
            priorityComboBox.setSelectedItem(currentPriority.toString());
        } else {
            priorityComboBox.setSelectedIndex(0); // None
        }
        
        // Set current due date
        dueDatePicker.setSelectedDate(currentDueDate);
        
        // Clear any previous messages
        messageLabel.setText(" ");
        
        // Show dialog
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Task name (read-only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Task:");
        nameLabel.setFont(FontUtil.getStandardFont());
        mainPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        taskNameLabel = new JLabel();
        taskNameLabel.setFont(FontUtil.getBoldFont());
        mainPanel.add(taskNameLabel, gbc);
        
        // Priority dropdown
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel priorityLabel = new JLabel("Priority:");
        priorityLabel.setFont(FontUtil.getStandardFont());
        mainPanel.add(priorityLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] priorities = {"None", "HIGH", "MEDIUM", "LOW"};
        priorityComboBox = new JComboBox<>(priorities);
        priorityComboBox.setFont(FontUtil.getStandardFont());
        mainPanel.add(priorityComboBox, gbc);
        
        // Due date picker
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel dueDateLabel = new JLabel("Due Date:");
        dueDateLabel.setFont(FontUtil.getStandardFont());
        mainPanel.add(dueDateLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        dueDatePicker = new DueDatePickerPanel();
        mainPanel.add(dueDatePicker, gbc);
        
        // Message label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        messageLabel = new JLabel(" ");
        messageLabel.setFont(FontUtil.getStandardFont());
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(messageLabel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        saveButton = new JButton("Save");
        saveButton.setFont(FontUtil.getStandardFont());
        saveButton.addActionListener(e -> saveChanges());
        buttonPanel.add(saveButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(FontUtil.getStandardFont());
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveChanges() {
        if (controller == null) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Controller not set");
            return;
        }
        
        // Get selected priority
        String selectedPriority = (String) priorityComboBox.getSelectedItem();
        Task.Priority priority = null;
        if (!"None".equals(selectedPriority)) {
            priority = Task.Priority.valueOf(selectedPriority);
        }
        
        // Get selected due date
        LocalDate dueDate = dueDatePicker.getSelectedDate();
        
        // Execute the edit
        controller.execute(currentTaskId, priority, dueDate);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (EditTodayTaskViewModel.EDIT_TODAY_TASK_STATE_PROPERTY.equals(evt.getPropertyName())) {
            EditTodayTaskState state = (EditTodayTaskState) evt.getNewValue();
            
            if (state.getSuccessMessage() != null) {
                // Success - close dialog
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText(state.getSuccessMessage());
                
                // Close dialog after brief delay
                Timer timer = new Timer(1000, e -> dispose());
                timer.setRepeats(false);
                timer.start();
            } else if (state.getError() != null) {
                // Error - show message and keep dialog open
                messageLabel.setForeground(Color.RED);
                messageLabel.setText(state.getError());
            }
        }
    }
}