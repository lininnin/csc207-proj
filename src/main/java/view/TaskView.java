package view;

import entity.Task;
import interface_adapter.controller.CreateTaskController;
import interface_adapter.controller.MarkTaskCompleteController;
import interface_adapter.controller.AddTaskToTodayController;

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
    private AddTaskToTodayController addTaskToTodayController;
    private Runnable dataReloader; // Callback to reload data

    // UI Components
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField categoryField;
    private JComboBox<String> priorityCombo;
    private JTextField beginDateField;
    private JTextField dueDateField;

    private DefaultListModel<TaskListItem> availableTasksModel;
    private DefaultListModel<TaskListItem> todayTasksModel;
    private DefaultListModel<TaskListItem> completedTasksModel;
    private DefaultListModel<TaskListItem> overdueTasksModel;

    private JLabel messageLabel;
    private JLabel completionRateLabel;

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

    public void setAddTaskToTodayController(AddTaskToTodayController controller) {
        this.addTaskToTodayController = controller;
    }

    public void setDataReloader(Runnable dataReloader) {
        this.dataReloader = dataReloader;
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: Create Task Panel
        add(createTaskCreationPanel(), BorderLayout.NORTH);

        // Center: Task Lists
        add(createTaskListsPanel(), BorderLayout.CENTER);

        // Bottom: Status Panel
        add(createStatusPanel(), BorderLayout.SOUTH);
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
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        // Available Tasks (top-left)
        JPanel availablePanel = new JPanel(new BorderLayout());
        availablePanel.setBorder(BorderFactory.createTitledBorder("Available Tasks"));
        availableTasksModel = new DefaultListModel<>();
        JList<TaskListItem> availableList = new JList<>(availableTasksModel);
        availablePanel.add(new JScrollPane(availableList), BorderLayout.CENTER);

        JButton addToTodayButton = new JButton("Add to Today");
        addToTodayButton.addActionListener(e -> {
            TaskListItem selected = availableList.getSelectedValue();
            if (selected != null && addTaskToTodayController != null) {
                addTaskToTodayController.addTaskToToday(selected.task.getInfo().getId());
                if (dataReloader != null) {
                    dataReloader.run();
                }
            }
        });
        availablePanel.add(addToTodayButton, BorderLayout.SOUTH);
        panel.add(availablePanel);

        // Today's Tasks (top-right)
        JPanel todayPanel = new JPanel(new BorderLayout());
        todayPanel.setBorder(BorderFactory.createTitledBorder("Today's Tasks"));
        todayTasksModel = new DefaultListModel<>();
        JList<TaskListItem> todayList = new JList<>(todayTasksModel);
        todayList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    TaskListItem item = todayList.getSelectedValue();
                    if (item != null) {
                        markCompleteController.markTaskComplete(item.task.getInfo().getId());
                        if (dataReloader != null) {
                            dataReloader.run();
                        }
                    }
                }
            }
        });
        todayPanel.add(new JScrollPane(todayList), BorderLayout.CENTER);
        todayPanel.add(new JLabel("Double-click to complete"), BorderLayout.SOUTH);
        panel.add(todayPanel);

        // Completed Tasks (bottom-left)
        JPanel completedPanel = new JPanel(new BorderLayout());
        completedPanel.setBorder(BorderFactory.createTitledBorder("Completed Today"));
        completedTasksModel = new DefaultListModel<>();
        JList<TaskListItem> completedList = new JList<>(completedTasksModel);
        completedPanel.add(new JScrollPane(completedList), BorderLayout.CENTER);
        panel.add(completedPanel);

        // Overdue Tasks (bottom-right)
        JPanel overduePanel = new JPanel(new BorderLayout());
        overduePanel.setBorder(BorderFactory.createTitledBorder("Overdue Tasks"));
        overdueTasksModel = new DefaultListModel<>();
        JList<TaskListItem> overdueList = new JList<>(overdueTasksModel);
        overdueList.setForeground(Color.RED);
        overduePanel.add(new JScrollPane(overdueList), BorderLayout.CENTER);
        panel.add(overduePanel);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        completionRateLabel = new JLabel("Today's Completion Rate: 0%");
        completionRateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(completionRateLabel, BorderLayout.WEST);

        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.BLUE);
        panel.add(messageLabel, BorderLayout.CENTER);

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

            // Reload data
            if (dataReloader != null) {
                dataReloader.run();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating task: " + e.getMessage());
        }
    }

    @Override
    public void onViewModelUpdated() {
        // Update task lists
        updateTaskList(availableTasksModel, viewModel.getAvailableTasks());
        updateTaskList(todayTasksModel, viewModel.getTodaysTasks());
        updateTaskList(completedTasksModel, viewModel.getCompletedTasks());
        updateTaskList(overdueTasksModel, viewModel.getOverdueTasks());

        // Update completion rate
        double rate = viewModel.getCompletionRate();
        completionRateLabel.setText(String.format("Today's Completion Rate: %.1f%%", rate * 100));

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
            String dates = "";
            if (task.getBeginAndDueDates().getDueDate() != null) {
                dates = " (due: " + task.getBeginAndDueDates().getDueDate() + ")";
            }
            return String.format("%s%s (%s)%s", category, task.getInfo().getName(), priority, dates);
        }
    }
}
