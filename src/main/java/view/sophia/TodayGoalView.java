package view.sophia;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;

import entity.Sophia.Goal;
import interface_adapter.sophia.today_goal.TodayGoalController;
import interface_adapter.sophia.today_goal.TodayGoalsViewModel;
import interface_adapter.sophia.today_goal.TodaysGoalsState;

/**
 * A Swing view that displays and manages today's goals.
 * This panel observes the {@link TodayGoalsViewModel} and updates the
 * displayed list of {@link Goal} objects whenever the state changes.
 * Users can double-click a goal to edit progress or remove it from
 * today's list through a popup menu.
 */
public class TodayGoalView extends JPanel implements PropertyChangeListener {
    private final TodayGoalsViewModel viewModel;
    private final TodayGoalController todayGoalController;
    private JList<Goal> goalsList;
     /**
     * Constructs a new {@code TodayGoalView}.
     *
     * @param viewModel            the view model containing goal data
     * @param todayGoalController  the controller for updating and removing goals
     */
    public TodayGoalView(TodayGoalsViewModel viewModel, TodayGoalController todayGoalController) {
        this.viewModel = viewModel;
        this.todayGoalController = todayGoalController;
        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        initializeUI();
    }

     /**
     * Initializes the UI components, including the list of goals and
     * event listeners for user interactions.
     */
    private void initializeUI() {
        goalsList = new JList<>();
        goalsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        goalsList.setCellRenderer(new GoalListCellRenderer());

        goalsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Goal selected = goalsList.getSelectedValue();
                    if (selected != null) {
                        showGoalOptions(selected);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(goalsList);
        add(scrollPane, BorderLayout.CENTER);
    }

     /**
     * Displays a popup menu with options to edit or remove the selected goal.
     * @param goal the selected goal
     */
    private void showGoalOptions(Goal goal) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(e -> showEditDialog(goal));
        popupMenu.add(editItem);

        JMenuItem deleteItem = new JMenuItem("Remove from Today");
        deleteItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Remove '" + goal.getGoalInfo().getInfo().getName() + "' from today?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                todayGoalController.removeFromToday(goal.getGoalInfo().getInfo().getName(), 0);}
        });
        popupMenu.add(deleteItem);

        popupMenu.show(goalsList,
                MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x,
                MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y
        );
    }

    /**
     * Opens a dialog allowing the user to edit the current progress of a goal.
     * @param goal the goal being edited
     */
    private void showEditDialog(Goal goal) {
        JDialog editDialog = new JDialog();
        editDialog.setTitle("Edit Goal");
        editDialog.setLayout(new GridLayout(0, 2, 5, 5));
        editDialog.setSize(400, 300);
        editDialog.setModal(true);

        editDialog.add(new JLabel("Current Progress:"));
        JSpinner currentSpinner = new JSpinner(new SpinnerNumberModel(
                goal.getCurrentProgress(), 0, goal.getFrequency(), 1
        ));
        editDialog.add(currentSpinner);

        editDialog.add(new JLabel("Target:"));
        JLabel targetLabel = new JLabel(String.valueOf(goal.getFrequency()));
        editDialog.add(targetLabel);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            int newProgress = (Integer) currentSpinner.getValue();
            todayGoalController.updateProgress(
                    goal.getGoalInfo().getInfo().getName(),
                    newProgress
            );
            editDialog.dispose();
        });
        editDialog.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> editDialog.dispose());
        editDialog.add(cancelButton);

        editDialog.setLocationRelativeTo(this);
        editDialog.setVisible(true);
    }

    /**
     * Handles property change events from the view model.
     * Updates the goal list when the "state" property changes.
     * @param evt the property change event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            TodaysGoalsState state = (TodaysGoalsState) evt.getNewValue();
            List<Goal> goals = state.getTodayGoals();
            goalsList.setListData(goals.toArray(new Goal[0]));

        }
    }

    /**
     * Custom cell renderer for displaying {@link Goal} objects in the list.
     * Shows name, description, and progress. Completed goals are highlighted.
     */
    private static class GoalListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Goal) {
                Goal goal = (Goal) value;
                setText(String.format("<html><b>%s</b> - %s<br/>Progress: %d of %d</html>",
                        goal.getGoalInfo().getInfo().getName(),
                        goal.getGoalInfo().getInfo().getDescription(),
                        goal.getCurrentProgress(),
                        goal.getFrequency()));

                if (goal.getCurrentProgress() >= goal.getFrequency()) {
                    setBackground(new Color(200, 255, 200));
                } else {
                    setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
                }
            }
            return this;
        }
    }
}
