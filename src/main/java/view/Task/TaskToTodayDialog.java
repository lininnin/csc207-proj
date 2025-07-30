package view.Task;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Dialog for setting priority and dates when adding a task to Today's Tasks.
 * This is where priority and dates are set according to the design.
 */
public class TaskToTodayDialog extends JDialog {
    private JComboBox<String> priorityCombo;
    private JSpinner beginDateSpinner;
    private JSpinner dueDateSpinner;
    private boolean confirmed = false;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TaskToTodayDialog(Frame parent) {
        super(parent, "Add Task to Today", true);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(400, 250);
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Priority selection
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Priority:"), gbc);

        gbc.gridx = 1;
        priorityCombo = new JComboBox<>(new String[]{"LOW", "MEDIUM", "HIGH"});
        priorityCombo.setSelectedItem("MEDIUM");
        mainPanel.add(priorityCombo, gbc);

        // Begin date (defaults to today)
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Begin Date:"), gbc);

        gbc.gridx = 1;
        beginDateSpinner = createDateSpinner(LocalDate.now());
        mainPanel.add(beginDateSpinner, gbc);

        // Due date (optional)
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Due Date (optional):"), gbc);

        gbc.gridx = 1;
        dueDateSpinner = createDateSpinner(null);
        mainPanel.add(dueDateSpinner, gbc);

        // Info label
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html><i>Note: Tasks with due dates will appear in Today's Tasks<br>every day until completed.</i></html>");
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.ITALIC, 11f));
        mainPanel.add(infoLabel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("Add to Today");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            if (validateDates()) {
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);
    }

    private JSpinner createDateSpinner(LocalDate defaultDate) {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);

        // Configure the spinner editor
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
        spinner.setEditor(editor);

        if (defaultDate != null) {
            // Convert LocalDate to Date for the spinner
            model.setValue(java.sql.Date.valueOf(defaultDate));
        }

        return spinner;
    }

    private boolean validateDates() {
        LocalDate beginDate = getBeginDate();
        LocalDate dueDate = getDueDate();

        if (beginDate == null) {
            JOptionPane.showMessageDialog(this,
                    "Begin date is required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (dueDate != null && dueDate.isBefore(beginDate)) {
            JOptionPane.showMessageDialog(this,
                    "Due date cannot be before begin date",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean showDialog() {
        setVisible(true);
        return confirmed;
    }

    public String getPriority() {
        return (String) priorityCombo.getSelectedItem();
    }

    public LocalDate getBeginDate() {
        try {
            java.util.Date date = (java.util.Date) beginDateSpinner.getValue();
            return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    public LocalDate getDueDate() {
        try {
            java.util.Date date = (java.util.Date) dueDateSpinner.getValue();
            if (date != null) {
                return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            }
        } catch (Exception e) {
            // Invalid or empty date
        }
        return null;
    }
}