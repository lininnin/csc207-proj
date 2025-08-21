package view.sophia;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import entity.Sophia.Goal;
import interface_adapter.sophia.available_goals.AvailableGoalsController;
import interface_adapter.sophia.available_goals.AvailableGoalsState;
import interface_adapter.sophia.available_goals.AvailableGoalsViewModel;

/**
 * A Swing view for displaying and managing a list of available goals.
 * It allows users to view, add, delete, and order goals.
 */
public class AvailableGoalsView extends JPanel implements PropertyChangeListener {
    // Magic Numbers as Constants
    private static final String AVAILABLE_GOALS_TITLE = "Available Goals";
    private static final String ADD_TO_TODAY_BUTTON_TEXT = "Add to Today";
    private static final String DELETE_BUTTON_TEXT = "Delete";
    private static final String REFRESH_BUTTON_TEXT = "Refresh";
    private static final String ORDER_BUTTON_TEXT = "Order Goals";
    private static final String FONT_NAME = "Arial";
    private static final int FONT_SIZE = 18;
    private static final int BUTTON_FONT_SIZE = 12;
    private static final int BUTTON_SPACING = 10;
    private static final int VERTICAL_PADDING = 5;
    private static final int HORIZONTAL_PADDING = 15;
    private static final String ORDER_DIALOG_TITLE = "Order Goals";
    private static final int ORDER_DIALOG_GRID_ROWS = 3;
    private static final int ORDER_DIALOG_GRID_COLS = 1;
    private static final int ORDER_DIALOG_WIDTH = 300;
    private static final int ORDER_DIALOG_HEIGHT = 200;
    private static final String ADD_SUCCESS_MESSAGE = "Added to Today: ";
    private static final String ADD_SUCCESS_TITLE = "Success";
    private static final String ADD_ERROR_MESSAGE = "Error adding to today: ";
    private static final String ADD_ERROR_TITLE = "Error";
    private static final String DELETE_CONFIRM_MESSAGE = "Permanently delete this goal?\n";
    private static final String DELETE_CONFIRM_TITLE = "Confirm Delete";
    private static final String REFRESH_COMMAND = "refresh";
    private static final String ADD_COMMAND_PREFIX = "add_to_today:";
    private static final String PROPERTY_CHANGE_STATE = "state";

    // Color Constants
    private static final Color ADD_BUTTON_COLOR = new Color(76, 175, 80);
    private static final Color DELETE_BUTTON_COLOR = new Color(244, 67, 54);
    private static final Color REFRESH_BUTTON_COLOR = new Color(33, 150, 243);
    private static final Color ORDER_BUTTON_COLOR = new Color(255, 193, 7);
    private static final Color SELECTED_COLOR = new Color(200, 220, 255);
    private static final Color EVEN_ROW_COLOR = new Color(240, 240, 240);

    // Instance variables
    private final AvailableGoalsViewModel viewModel;
    private final AvailableGoalsController controller;
    private final JList<Goal> goalsList;
    private final JButton addToTodayButton;
    private final JButton deleteButton;
    private final JButton orderButton;

    /**
     * Constructs an {@code AvailableGoalsView}.
     *
     * @param viewModel The view model that holds the state of available goals.
     * @param controller The controller that handles user actions on the goals.
     */
    public AvailableGoalsView(final AvailableGoalsViewModel viewModel,
                              final AvailableGoalsController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // Title
        final JLabel title = new JLabel(AVAILABLE_GOALS_TITLE);
        title.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Goals list with custom renderer
        goalsList = new JList<>();
        goalsList.setCellRenderer(new GoalListCellRenderer());
        goalsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane scrollPane = new JScrollPane(goalsList);

        // Action buttons panel
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_SPACING, BUTTON_SPACING));

        addToTodayButton = new JButton(ADD_TO_TODAY_BUTTON_TEXT);
        deleteButton = new JButton(DELETE_BUTTON_TEXT);
        final JButton refreshButton = new JButton(REFRESH_BUTTON_TEXT);
        orderButton = new JButton(ORDER_BUTTON_TEXT);

        // Style buttons
        styleButton(addToTodayButton, ADD_BUTTON_COLOR);
        styleButton(deleteButton, DELETE_BUTTON_COLOR);
        styleButton(refreshButton, REFRESH_BUTTON_COLOR);
        styleButton(orderButton, ORDER_BUTTON_COLOR);

        // Add action listeners
        refreshButton.addActionListener(ex1 -> controller.execute(REFRESH_COMMAND));
        addToTodayButton.addActionListener(ex2 -> addSelectedToToday());
        deleteButton.addActionListener(ex3 -> deleteSelectedGoal());
        orderButton.addActionListener(ex4 -> showOrderDialog());

        // Enable/disable buttons based on selection
        goalsList.addListSelectionListener(ex5 -> {
            final boolean hasSelection = !goalsList.isSelectionEmpty();
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

    /**
     * Applies a consistent style to a button.
     *
     * @param button The JButton to style.
     * @param color The background color for the button.
     */
    private void styleButton(final JButton button, final Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font(FONT_NAME, Font.BOLD, BUTTON_FONT_SIZE));
        button.setBorder(BorderFactory.createEmptyBorder(VERTICAL_PADDING,
                HORIZONTAL_PADDING,
                VERTICAL_PADDING,
                HORIZONTAL_PADDING));
    }

    /**
     * Gets the currently selected goal from the list.
     *
     * @return The selected {@code Goal} object, or {@code null} if no goal is selected.
     */
    public Goal getSelectedGoal() {
        return goalsList.getSelectedValue();
    }

    /**
     * Refreshes the list of goals displayed in the view based on the current state of the view model.
     */
    private void refreshView() {
        final AvailableGoalsState state = viewModel.getState();
        final DefaultListModel<Goal> model = new DefaultListModel<>();
        if (state.getAvailableGoals() != null) {
            state.getAvailableGoals().forEach(model::addElement);
        }
        goalsList.setModel(model);

        // Reset selection state
        addToTodayButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    /**
     * Listens for property changes in the view model to update the view.
     *
     * @param evt The property change event.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (PROPERTY_CHANGE_STATE.equals(evt.getPropertyName())) {
            refreshView();
        }
    }

    /**
     * Handles the action of adding a selected goal to today's goals.
     */
    private void addSelectedToToday() {
        final Goal selected = goalsList.getSelectedValue();
        if (selected != null) {
            try {
                // Convert goal to command string
                final String goalCommand = ADD_COMMAND_PREFIX + selected.getGoalInfo().getInfo().getName();

                // Execute through controller
                controller.execute(goalCommand);

                JOptionPane.showMessageDialog(this,
                        ADD_SUCCESS_MESSAGE + selected.getGoalInfo().getInfo().getName(),
                        ADD_SUCCESS_TITLE,
                        JOptionPane.INFORMATION_MESSAGE);

                // Refresh the view
                controller.execute(REFRESH_COMMAND);

            }
            catch (final RuntimeException ex) {
                JOptionPane.showMessageDialog(this,
                        ADD_ERROR_MESSAGE + ex.getMessage(),
                        ADD_ERROR_TITLE,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Handles the action of deleting a selected goal.
     */
    private void deleteSelectedGoal() {
        final Goal selected = goalsList.getSelectedValue();
        if (selected != null) {
            final int confirm = JOptionPane.showConfirmDialog(this,
                    DELETE_CONFIRM_MESSAGE + selected.getGoalInfo().getInfo().getName(),
                    DELETE_CONFIRM_TITLE,
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // New, direct call to the controller's deleteGoal method
                controller.deleteGoal(selected);
            }
        }
    }

    /**
     * Displays a dialog for ordering the goals.
     */
    private void showOrderDialog() {
        final JDialog orderDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                ORDER_DIALOG_TITLE, true);
        orderDialog.setLayout(new GridLayout(ORDER_DIALOG_GRID_ROWS,
                ORDER_DIALOG_GRID_COLS,
                BUTTON_SPACING,
                BUTTON_SPACING));
        orderDialog.setSize(ORDER_DIALOG_WIDTH, ORDER_DIALOG_HEIGHT);
        orderDialog.setLocationRelativeTo(this);

        final String[] options = {"Name", "Deadline", "Period", "Frequency"};
        final JComboBox<String> sortCriteriaBox = new JComboBox<>(options);

        final JCheckBox reverseCheckBox = new JCheckBox("Reverse Order");

        final JButton applyButton = new JButton("Apply Order");
        applyButton.addActionListener(acEven -> {
            final String selectedCriterion = (String) sortCriteriaBox.getSelectedItem();
            final boolean isReverse = reverseCheckBox.isSelected();

            // Get the current list of goals from the view model
            final List<Goal> goalsToOrder = viewModel.getState().getAvailableGoals();

            // Sort the list based on the user's selection
            Comparator<Goal> comparator;
            switch (selectedCriterion) {
                case "Name":
                    comparator = Comparator.comparing(goal -> goal.getGoalInfo().getInfo().getName());
                    break;
                case "Deadline":
                    comparator = Comparator.comparing(goal -> goal.getBeginAndDueDates().getDueDate());
                    break;
                case "Period":
                    comparator = Comparator.comparing(goal -> goal.getTimePeriod().toString());
                    break;
                case "Frequency":
                    comparator = Comparator.comparingInt(Goal::getFrequency);
                    break;
                default:
                    comparator = Comparator.comparing(goal -> goal.getGoalInfo().getInfo().getName());
                    break;
            }

            if (isReverse) {
                comparator = comparator.reversed();
            }

            final List<Goal> sortedGoals = goalsToOrder.stream().sorted(comparator).collect(Collectors.toList());

            // Update the view model's state with the new, sorted list
            final AvailableGoalsState newState = viewModel.getState();
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

    /**
     * Custom cell renderer for the goals list to display goal details.
     */
    private final class GoalListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index,
                                                      final boolean isSelected, final boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Goal) {
                final Goal goal = (Goal) value;
                setText(formatGoalText(goal));
                setToolTipText(createTooltipText(goal));

                final Color rowColor;
                if (isSelected) {
                    rowColor = SELECTED_COLOR;
                    setForeground(Color.BLACK);
                }
                else {
                    if (isSelected) {
                        rowColor = SELECTED_COLOR;
                        setForeground(Color.BLACK);
                    }
                    else {
                        if (index % 2 == 0) {
                            rowColor = Color.WHITE;
                        }
                        else {
                            rowColor = EVEN_ROW_COLOR;
                        }
                    }
                    setBackground(rowColor);
                }
            }
            return this;
        }

        /**
         * Adds more detail to the formatted text displayed in the JList.
         *
         * @param goal The goal to format.
         * @return An HTML formatted string with goal details.
         */
        private String formatGoalText(final Goal goal) {
            final String period;
            if (goal.getTimePeriod().toString().equals("MONTH")) {
                period = "Monthly";
            }
            else {
                period = "Weekly";
            }
            return String.format("<html><b>%s</b> - %s<br/>"
                            + "Progress: %d of %d (%s)<br/>"
                            + "Dates: %s to %s</html>",
                    goal.getGoalInfo().getInfo().getName(),
                    goal.getGoalInfo().getInfo().getDescription(),
                    goal.getCurrentProgress(),
                    goal.getFrequency(),
                    period,
                    goal.getBeginAndDueDates().getBeginDate(),
                    goal.getBeginAndDueDates().getDueDate());
        }

        /**
         * Creates a detailed tooltip for a goal.
         *
         * @param goal The goal for which to create the tooltip.
         * @return An HTML formatted string for the tooltip.
         */
        private String createTooltipText(final Goal goal) {
            final String period;
            if (goal.getTimePeriod().toString().equals("MONTH")) {
                period = "Monthly";
            }
            else {
                period = "Weekly";
            }
            return String.format(
                    "<html><b>Name:</b> %s<br><b>Description:</b> %s<br><b>Period:</b> %s<br>"
                            + "<b>Progress:</b> %d/%d<br><b>Dates:</b> %s to %s</html>",
                    goal.getGoalInfo().getInfo().getName(),
                    goal.getGoalInfo().getInfo().getDescription(),
                    period,
                    goal.getCurrentProgress(),
                    goal.getFrequency(),
                    goal.getBeginAndDueDates().getBeginDate(),
                    goal.getBeginAndDueDates().getDueDate());
        }
    }
}
