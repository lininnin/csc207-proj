package view.Sophia;

import interface_adapter.sophia.available_goals.AvailableGoalsController;
import interface_adapter.sophia.available_goals.AvailableGoalsState;
import interface_adapter.sophia.available_goals.AvailableGoalsViewModel;
import entity.Sophia.Goal;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AvailableGoalsView extends JPanel implements PropertyChangeListener {
    private final AvailableGoalsViewModel viewModel;
    private final AvailableGoalsController controller;
    private final JList<Goal> goalsList;
    private final JButton addToTodayButton;
    private final JButton deleteButton;
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
        orderButton = new JButton("Order Goals");

        // Style buttons
        styleButton(addToTodayButton, new Color(76, 175, 80)); // Green
        styleButton(deleteButton, new Color(244, 67, 54)); // Red
        styleButton(refreshButton, new Color(33, 150, 243)); // Blue
        styleButton(orderButton, new Color(255, 193, 7)); // Yellow

        // Add action listeners
        refreshButton.addActionListener(e -> controller.execute("refresh"));
        addToTodayButton.addActionListener(e -> addSelectedToToday());
        deleteButton.addActionListener(e -> deleteSelectedGoal());
        orderButton.addActionListener(e -> showOrderDialog());

        // Enable/disable buttons based on selection
        goalsList.addListSelectionListener(e -> {
            boolean hasSelection = !goalsList.isSelectionEmpty();
            addToTodayButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
        });

        buttonPanel.add(addToTodayButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(orderButton);

        // Layout
        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

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
                        "Added to Today: " + selected.getGoalInfo().getInfo().getName(),
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

    private void showOrderDialog() {
        JDialog orderDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Order Goals", true);
        orderDialog.setLayout(new GridLayout(3, 1, 10, 10));
        orderDialog.setSize(300, 200);
        orderDialog.setLocationRelativeTo(this);

        String[] options = {"Name", "Deadline", "Period", "Frequency"};
        JComboBox<String> sortCriteriaBox = new JComboBox<>(options);

        JCheckBox reverseCheckBox = new JCheckBox("Reverse Order");

        JButton applyButton = new JButton("Apply Order");
        applyButton.addActionListener(e -> {
            String selectedCriterion = (String) sortCriteriaBox.getSelectedItem();
            boolean isReverse = reverseCheckBox.isSelected();

            // Get the current list of goals from the view model
            List<Goal> goalsToOrder = viewModel.getState().getAvailableGoals();

            // Sort the list based on the user's selection
            Comparator<Goal> comparator;
            switch (selectedCriterion) {
                case "Name":
                    comparator = Comparator.comparing(g -> g.getGoalInfo().getInfo().getName());
                    break;
                case "Deadline":
                    comparator = Comparator.comparing(g -> g.getBeginAndDueDates().getDueDate());
                    break;
                case "Period":
                    comparator = Comparator.comparing(g -> g.getTimePeriod().toString());
                    break;
                case "Frequency":
                    comparator = Comparator.comparingInt(Goal::getFrequency);
                    break;
                default:
                    comparator = Comparator.comparing(g -> g.getGoalInfo().getInfo().getName());
                    break;
            }

            if (isReverse) {
                comparator = comparator.reversed();
            }

            List<Goal> sortedGoals = goalsToOrder.stream().sorted(comparator).collect(Collectors.toList());

            // Update the view model's state with the new, sorted list
            AvailableGoalsState newState = viewModel.getState();
            newState.setAvailableGoals(sortedGoals);
            viewModel.setState(newState);
            refreshView();

            orderDialog.dispose();
        });

        orderDialog.add(new JLabel("Sort by:"));
        orderDialog.add(sortCriteriaBox);
        orderDialog.add(reverseCheckBox);
        orderDialog.add(applyButton);

        orderDialog.setVisible(true);
    }
}