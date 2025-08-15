package views;

import interface_adapter.Sophia.available_goals.AvailableGoalsController;
import interface_adapter.Sophia.available_goals.AvailableGoalsState;
import interface_adapter.Sophia.available_goals.AvailableGoalsViewModel;
import entity.Sophia.Goal;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AvailableGoalsView extends JPanel implements PropertyChangeListener {
    private final AvailableGoalsViewModel viewModel;
    private final AvailableGoalsController controller;
    private final JList<Goal> goalsList;
    private final JButton addToTodayButton;
    private final JButton deleteButton;
    // The reverseOrderButton is not a class member, it will be created dynamically
    // private final JButton reverseOrderButton;
    private final JButton orderButton;

    public AvailableGoalsView(AvailableGoalsViewModel viewModel,
                              AvailableGoalsController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Available Goals");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Goals list with custom renderer
        goalsList = new JList<>();
        goalsList.setCellRenderer(new GoalListCellRenderer());
        goalsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(goalsList);

        // Action buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        addToTodayButton = new JButton("Add to Today");
        deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");
        // NEW BUTTON
        orderButton = new JButton("Order Goals"); // Changed button text

        // Style buttons
        styleButton(addToTodayButton, new Color(76, 175, 80)); // Green
        styleButton(deleteButton, new Color(244, 67, 54)); // Red
        styleButton(refreshButton, new Color(33, 150, 243)); // Blue
        // Style the new button
        styleButton(orderButton, new Color(255, 193, 7)); // Yellow

        // Add action listeners
        refreshButton.addActionListener(e -> controller.execute("refresh"));
        addToTodayButton.addActionListener(e -> addSelectedToToday());
        deleteButton.addActionListener(e -> deleteSelectedGoal());
        // Modified listener for the "Order Goals" button
        orderButton.addActionListener(e -> showOrderOptions());

        // Enable/disable buttons based on selection
        goalsList.addListSelectionListener(e -> {
            boolean hasSelection = !goalsList.isSelectionEmpty();
            addToTodayButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
        });

        buttonPanel.add(addToTodayButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        // Add the new button to the panel
        buttonPanel.add(orderButton);

        // Layout
        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Remove this block since the reverse order button will be part of the dialog
        // reverseOrderButton = new JButton("Reverse Order");
        // styleButton(reverseOrderButton, new Color(255, 152, 0)); // Orange
        // reverseOrderButton.addActionListener(e -> showOrderOptions(true));
        // buttonPanel.add(reverseOrderButton);

        // Initial load
        refreshView();
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    public Goal getSelectedGoal() {
        return goalsList.getSelectedValue();
    }

    private void refreshView() {
        AvailableGoalsState state = viewModel.getState();
        DefaultListModel<Goal> model = new DefaultListModel<>();
        if (state.getAvailableGoals() != null) {
            state.getAvailableGoals().forEach(model::addElement);
        }
        goalsList.setModel(model);

        // Reset selection state
        addToTodayButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            refreshView();
        }
    }

    private class GoalListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Goal) {
                Goal goal = (Goal) value;
                setText(formatGoalText(goal));
                setToolTipText(createTooltipText(goal));

                if (isSelected) {
                    setBackground(new Color(200, 220, 255));
                    setForeground(Color.BLACK);
                } else {
                    setBackground(index % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                }
            }
            return this;
        }

        /**
         * Adds more detail to the formatted text displayed in the JList.
         */
        private String formatGoalText(Goal goal) {
            String period = goal.getTimePeriod().toString().equals("MONTH") ? "Monthly" : "Weekly";
            return String.format("<html><b>%s</b> - %s<br/>" +
                            "Progress: %d of %d (%s)<br/>" +
                            "Dates: %s to %s</html>",
                    goal.getGoalInfo().getInfo().getName(),
                    goal.getGoalInfo().getInfo().getDescription(),
                    goal.getCurrentProgress(),
                    goal.getFrequency(),
                    period,
                    goal.getBeginAndDueDates().getBeginDate(),
                    goal.getBeginAndDueDates().getDueDate());
        }

        private String createTooltipText(Goal goal) {
            String period = goal.getTimePeriod().toString().equals("MONTH") ? "Monthly" : "Weekly";
            return String.format(
                    "<html><b>Name:</b> %s<br><b>Description:</b> %s<br><b>Period:</b> %s<br>" +
                            "<b>Progress:</b> %d/%d<br><b>Dates:</b> %s to %s</html>",
                    goal.getGoalInfo().getInfo().getName(),
                    goal.getGoalInfo().getInfo().getDescription(),
                    period,
                    goal.getCurrentProgress(),
                    goal.getFrequency(),
                    goal.getBeginAndDueDates().getBeginDate(),
                    goal.getBeginAndDueDates().getDueDate());
        }
    }

    // These methods were moved here to resolve the error.
    private void addSelectedToToday() {
        Goal selected = goalsList.getSelectedValue();
        if (selected != null) {
            try {
                // Convert goal to command string
                String goalCommand = "add_to_today:" + selected.getGoalInfo().getInfo().getName();

                // Execute through controller
                controller.execute(goalCommand);

                JOptionPane.showMessageDialog(this,
                        "Added to Today: " + selected,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Refresh the view
                controller.execute("refresh");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error adding to today: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedGoal() {
        Goal selected = goalsList.getSelectedValue();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Permanently delete this goal?\n" + selected.getGoalInfo().getInfo().getName(),
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // New, direct call to the controller's deleteGoal method
                controller.deleteGoal(selected);
            }
        }
    }

    private void showOrderOptions() {
        String[] options = {"name", "deadline", "period"};
        String selection = (String) JOptionPane.showInputDialog(
                this,
                "Select ordering criteria:",
                "Order Goals",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
        if (selection != null) {
            // Add a confirmation for reverse order
            int reverseOption = JOptionPane.showConfirmDialog(
                    this,
                    "Order in reverse?",
                    "Reverse Order",
                    JOptionPane.YES_NO_OPTION
            );
            boolean reverse = (reverseOption == JOptionPane.YES_OPTION);
            controller.orderGoals(selection, reverse);
        }
    }
}