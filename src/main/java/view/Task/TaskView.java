package view.Task;

import entity.Angela.Task.Task;
import entity.Angela.CategoryManager;
import interface_adapter.controller.CreateTaskController;
import interface_adapter.controller.MarkTaskCompleteController;
import interface_adapter.controller.AddTaskToTodayController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Main view for task management following the GUI mockup design.
 * Correctly implements task creation without priority/dates.
 */
public class TaskView extends JPanel implements TaskViewModelUpdateListener {
    private final TaskViewModel viewModel;
    private final CreateTaskController createTaskController;
    private final MarkTaskCompleteController markCompleteController;
    private AddTaskToTodayController addTaskToTodayController;

    // UI Components for Create Task (matching design)
    private JTextField nameField;
    private JTextField categoryField;
    private JTextArea descriptionField;
    private JCheckBox oneTimeCheckBox;

    // Task Tables
    private JTable availableTasksTable;
    private JTable todaysTasksTable;
    private DefaultTableModel availableTasksModel;
    private DefaultTableModel todaysTasksModel;

    // Today's So Far panel placeholder (will be implemented separately)
    private JPanel todaysSoFarPanel;

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

    public void setTodaysSoFarPanel(JPanel panel) {
        if (todaysSoFarPanel != null) {
            remove(todaysSoFarPanel);
        }
        todaysSoFarPanel = panel;
        add(todaysSoFarPanel, BorderLayout.EAST);
        revalidate();
        repaint();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(250, 248, 240)); // Match mockup background

        // Create main content panel (left side)
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(getBackground());
        mainContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components matching the GUI layout
        mainContent.add(createHeaderPanel());
        mainContent.add(Box.createVerticalStrut(10));
        mainContent.add(createNewTaskPanel());
        mainContent.add(Box.createVerticalStrut(10));
        mainContent.add(createTaskTablesPanel());

        add(mainContent, BorderLayout.CENTER);

        // Today's So Far panel will be added to EAST when set
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(getBackground());

        // Create colored buttons matching mockup
        JButton[] buttons = {
                createStyledButton("button", new Color(255, 200, 150)),
                createStyledButton("enter", new Color(200, 180, 255)),
                createStyledButton("enter + select", new Color(180, 220, 180)),
                createStyledButton("+ create new\n+ delete", new Color(180, 220, 180)),
                createStyledButton("select", new Color(180, 220, 255))
        };

        for (JButton button : buttons) {
            panel.add(button);
        }

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(100, 30));
        button.setFont(new Font("Arial", Font.PLAIN, 11));
        return button;
    }

    private JPanel createNewTaskPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                "New Available Task",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Category field with click handler
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        categoryField = new JTextField(20);
        categoryField.setEditable(false);
        categoryField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        categoryField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showCategoryDialog();
            }
        });
        panel.add(categoryField, gbc);

        // Description field
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        descriptionField = new JTextArea(3, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionField);
        panel.add(scrollPane, gbc);

        // One-time checkbox
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(new JLabel("One-time:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        oneTimeCheckBox = new JCheckBox();
        panel.add(oneTimeCheckBox, gbc);

        // Create button
        gbc.gridx = 3; gbc.gridy = 3;
        gbc.gridwidth = 1;
        JButton createButton = new JButton("Create Task");
        createButton.setBackground(new Color(180, 220, 180));
        createButton.addActionListener(e -> createTask());
        panel.add(createButton, gbc);

        return panel;
    }

    private JPanel createTaskTablesPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBackground(getBackground());

        // Available Tasks Panel
        JPanel availablePanel = new JPanel(new BorderLayout());
        availablePanel.setBorder(BorderFactory.createTitledBorder("Available Tasks"));
        availablePanel.setBackground(Color.WHITE);

        String[] availableColumns = {"Name", "Category", "Description", "One-time"};
        availableTasksModel = new DefaultTableModel(availableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        availableTasksTable = new JTable(availableTasksModel);
        availableTasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane availableScroll = new JScrollPane(availableTasksTable);
        availablePanel.add(availableScroll, BorderLayout.CENTER);

        // Buttons for Available Tasks
        JPanel availableButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addToTodayBtn = new JButton("Add to Today");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        addToTodayBtn.addActionListener(e -> addSelectedToToday());

        availableButtons.add(addToTodayBtn);
        availableButtons.add(editBtn);
        availableButtons.add(deleteBtn);
        availablePanel.add(availableButtons, BorderLayout.SOUTH);

        // Today's Tasks Panel
        JPanel todayPanel = new JPanel(new BorderLayout());
        todayPanel.setBorder(BorderFactory.createTitledBorder("Today's Tasks"));
        todayPanel.setBackground(Color.WHITE);

        String[] todayColumns = {"✓", "Name", "Category", "Priority", "Due Date"};
        todaysTasksModel = new DefaultTableModel(todayColumns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Only checkbox is editable
            }
        };

        todaysTasksTable = new JTable(todaysTasksModel);
        todaysTasksTable.getColumnModel().getColumn(0).setMaxWidth(30);

        // Handle checkbox changes
        todaysTasksModel.addTableModelListener(e -> {
            if (e.getColumn() == 0) {
                int row = e.getFirstRow();
                Boolean completed = (Boolean) todaysTasksModel.getValueAt(row, 0);
                if (completed != null && completed) {
                    markTaskComplete(row);
                }
            }
        });

        JScrollPane todayScroll = new JScrollPane(todaysTasksTable);
        todayPanel.add(todayScroll, BorderLayout.CENTER);

        panel.add(availablePanel);
        panel.add(todayPanel);

        return panel;
    }

    private void showCategoryDialog() {
        CategorySelectionDialog dialog = new CategorySelectionDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                categoryField.getText(),
                categoryField
        );
        dialog.showDialog();
    }

    private void createTask() {
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String description = descriptionField.getText().trim();
        boolean oneTime = oneTimeCheckBox.isSelected();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Task name is required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (name.length() > 20) {
            JOptionPane.showMessageDialog(this,
                    "Task name must be 20 characters or less",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Call controller to create task (without priority/dates as per design)
        createTaskController.createTask(name, description, category, oneTime);

        // Clear form
        nameField.setText("");
        categoryField.setText("");
        descriptionField.setText("");
        oneTimeCheckBox.setSelected(false);
    }

    private void addSelectedToToday() {
        int selectedRow = availableTasksTable.getSelectedRow();
        if (selectedRow >= 0 && addTaskToTodayController != null) {
            // Get task ID from view model based on row
            List<Task> availableTasks = viewModel.getAvailableTasks();
            if (selectedRow < availableTasks.size()) {
                Task task = availableTasks.get(selectedRow);

                // Show dialog to set priority and dates
                TaskToTodayDialog dialog = new TaskToTodayDialog(
                        (Frame) SwingUtilities.getWindowAncestor(this)
                );

                if (dialog.showDialog()) {
                    addTaskToTodayController.addTaskToToday(
                            task.getInfo().getId(),
                            dialog.getPriority(),
                            dialog.getBeginDate(),
                            dialog.getDueDate()
                    );
                }
            }
        }
    }

    private void markTaskComplete(int row) {
        List<Task> todaysTasks = viewModel.getTodaysTasks();
        if (row < todaysTasks.size()) {
            Task task = todaysTasks.get(row);
            markCompleteController.markTaskComplete(task.getInfo().getId());
        }
    }

    @Override
    public void onViewModelUpdated() {
        // Update Available Tasks table
        availableTasksModel.setRowCount(0);
        for (Task task : viewModel.getAvailableTasks()) {
            availableTasksModel.addRow(new Object[]{
                    task.getInfo().getName(),
                    task.getInfo().getCategory(),
                    task.getInfo().getDescription(),
                    task.isOneTime() ? "Yes" : "No"
            });
        }

        // Update Today's Tasks table
        todaysTasksModel.setRowCount(0);
        for (Task task : viewModel.getTodaysTasks()) {
            todaysTasksModel.addRow(new Object[]{
                    task.isComplete(),
                    task.getInfo().getName(),
                    task.getInfo().getCategory(),
                    task.getPriority() != null ? task.getPriority().toString() : "",
                    task.getBeginAndDueDates().getDueDate() != null ?
                            task.getBeginAndDueDates().getDueDate().format(DATE_FORMATTER) : ""
            });
        }

        // Show any messages
        String message = viewModel.getMessage();
        if (message != null && !message.isEmpty()) {
            // Could show in status bar or temporary notification
            System.out.println(message); // Placeholder
        }
    }
}