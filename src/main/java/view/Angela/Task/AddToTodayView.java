package view.Angela.Task;

import use_case.Angela.task.TaskGateway;
import entity.info.Info;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import view.Angela.FontUtil;

/**
 * Placeholder view for Add to Today functionality.
 * This will be properly implemented when you work on the add_task_to_today use case.
 */
public class AddToTodayView extends JPanel {
    private final JComboBox<TaskItem> taskDropdown;
    private final JComboBox<String> priorityDropdown;
    private final JTextField dueDateField;
    private final JButton addButton;

    private TaskGateway taskGateway;

    public AddToTodayView() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Add Today's Task"));

        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Select Task Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Select Task Name:"), gbc);
        gbc.gridx = 1;
        taskDropdown = new JComboBox<>();
        taskDropdown.setPreferredSize(new Dimension(200, 25)); // Increased width
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
        priorityDropdown = new JComboBox<>(new String[]{"", "Low", "Medium", "High"});
        priorityDropdown.setPreferredSize(new Dimension(200, 25)); // Increased width
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
        dueDateField = new JTextField(12);
        dueDateField.setFont(FontUtil.getStandardFont()); // Fix font for input field
        formPanel.add(dueDateField, gbc);

        // Add button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addButton = new JButton("Add to Today");
        addButton.addActionListener(e -> {
            // Do nothing for now as requested
        });
        formPanel.add(addButton, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    public void setTaskGateway(TaskGateway taskGateway) {
        this.taskGateway = taskGateway;
        refreshTasks();
    }

    private void refreshTasks() {
        taskDropdown.removeAllItems();
        taskDropdown.addItem(new TaskItem("", "-- Select Task --"));

        if (taskGateway != null) {
            List<Info> tasks = taskGateway.getAllAvailableTasks();
            for (Info task : tasks) {
                taskDropdown.addItem(new TaskItem(task.getId(), task.getName()));
            }
        }
    }

    // Helper class for ComboBox items
    private static class TaskItem {
        private final String id;
        private final String name;

        public TaskItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() { return id; }

        @Override
        public String toString() { return name; }
    }
}