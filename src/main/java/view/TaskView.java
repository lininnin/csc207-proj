package view;

import entity.Task;
import interface_adapter.controller.CreateTaskController;
import interface_adapter.controller.MarkTaskCompleteController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Main view for task management.
 * Implements the UI for creating tasks and marking them complete.
 */
public class TaskView extends JPanel implements TaskViewModelUpdateListener {
    private final TaskViewModel viewModel;
    private final CreateTaskController createTaskController;
    private final MarkTaskCompleteController markCompleteController;

    // UI Components
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField categoryField;
    private JComboBox<String> priorityCombo;
    private JTextField beginDateField;
    private JTextField dueDateField;


    private DefaultListModel<TaskListItem> todayTasksModel;
    private DefaultListModel<TaskListItem> completedTasksModel;
    private DefaultListModel<TaskListItem> overdueTasksModel;

    private JLabel messageLabel;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TaskView(TaskViewModel viewModel,
                    CreateTaskController createTaskController,
                    MarkTaskCompleteController markCompleteController) {
        this.viewModel = viewModel;
        this.createTaskController = createTaskController;
        this.markCompleteController = markCompleteController;

        viewModel.addUpdateListener(this);

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: Create Task Panel
        add(createTaskCreationPanel(), BorderLayout.NORTH);

        // Center: Task Lists
        add(createTaskListsPanel(), BorderLayout.CENTER);

        // Bottom: Message Label
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.BLUE);
        add(messageLabel, BorderLayout.SOUTH);
    }

    private JPanel createTaskCreationPanel() {
        JButton createButton;
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Create New Task"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Row 1: Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Row 2: Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        descriptionArea = new JTextArea(2, 20);
        descriptionArea.setLineWrap(true);
        panel.add(new JScrollPane(descriptionArea), gbc);

        // Row 3: Category and Priority
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        categoryField = new JTextField(10);
        panel.add(categoryField, gbc);

        gbc.gridx = 2;
        priorityCombo = new JComboBox<>(new String[]{"LOW", "MEDIUM", "HIGH"});
        priorityCombo.setSelectedItem("MEDIUM");
        panel.add(priorityCombo, gbc);

        // Row 4: Dates
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Begin Date:"), gbc);

        gbc.gridx = 1;
        beginDateField = new JTextField(10);
        beginDateField.setText(LocalDate.now().format(DATE_FORMATTER));
        panel.add(beginDateField, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Due Date (optional):"), gbc);

        gbc.gridx = 3;
        dueDateField = new JTextField(10);
        panel.add(dueDateField, gbc);

        // Row 5: Create Button
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        createButton = new JButton("Create Task");
        createButton.addActionListener(e -> createTask());
        panel.add(createButton, gbc);

        return panel;
    }

    private JPanel createTaskListsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 0));

        // Today's Tasks
        JPanel todayPanel = new JPanel(new BorderLayout());
        todayPanel.setBorder(BorderFactory.createTitledBorder("Today's Tasks"));
        todayTasksModel = new DefaultListModel<>();
        JList<TaskListItem> todayList = new JList<>(todayTasksModel);
        todayList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    TaskListItem item = todayList.getSelectedValue();
                    if (item != null) {
                        markCompleteController.markTaskComplete(item.task.getInfo().getId());
                    }
                }
            }
        });
        todayPanel.add(new JScrollPane(todayList), BorderLayout.CENTER);
        todayPanel.add(new JLabel("Double-click to complete"), BorderLayout.SOUTH);
        panel.add(todayPanel);

        // Completed Tasks
        JPanel completedPanel = new JPanel(new BorderLayout());
        completedPanel.setBorder(BorderFactory.createTitledBorder("Completed Today"));
        completedTasksModel = new DefaultListModel<>();
        JList<TaskListItem> completedList = new JList<>(completedTasksModel);
        completedPanel.add(new JScrollPane(completedList), BorderLayout.CENTER);
        panel.add(completedPanel);

        // Overdue Tasks
        JPanel overduePanel = new JPanel(new BorderLayout());
        overduePanel.setBorder(BorderFactory.createTitledBorder("Overdue Tasks"));
        overdueTasksModel = new DefaultListModel<>();
        JList<TaskListItem> overdueList = new JList<>(overdueTasksModel);
        overdueList.setForeground(Color.RED);
        overduePanel.add(new JScrollPane(overdueList), BorderLayout.CENTER);
        panel.add(overduePanel);

        return panel;
    }

    private void createTask() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task name is required");
                return;
            }

            createTaskController.createTask(
                    name,
                    descriptionArea.getText().trim(),
                    categoryField.getText().trim(),
                    (String) priorityCombo.getSelectedItem(),
                    beginDateField.getText().trim(),
                    dueDateField.getText().trim()
            );

            // Clear form
            nameField.setText("");
            descriptionArea.setText("");
            categoryField.setText("");
            dueDateField.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating task: " + e.getMessage());
        }
    }

    @Override
    public void onViewModelUpdated() {
        // Update task lists
        updateTaskList(todayTasksModel, viewModel.getTodaysTasks());
        updateTaskList(completedTasksModel, viewModel.getCompletedTasks());
        updateTaskList(overdueTasksModel, viewModel.getOverdueTasks());

        // Update message
        messageLabel.setText(viewModel.getMessage());
    }

    private void updateTaskList(DefaultListModel<TaskListItem> model, List<Task> tasks) {
        model.clear();
        for (Task task : tasks) {
            model.addElement(new TaskListItem(task));
        }
    }

    // Helper class for displaying tasks in lists
    private static class TaskListItem {
        final Task task;

        TaskListItem(Task task) {
            this.task = task;
        }

        @Override
        public String toString() {
            String priority = task.getTaskPriority().name();
            String category = task.getInfo().getCategory() != null ?
                    "[" + task.getInfo().getCategory() + "] " : "";
            return String.format("%s%s (%s)", category, task.getInfo().getName(), priority);
        }
    }
}
