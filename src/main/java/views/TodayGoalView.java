package views;

import entity.Sophia.Goal;
import interface_adapter.Sophia.today_goal.TodayGoalsViewModel;
import interface_adapter.Sophia.today_goal.TodayGoalController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class TodayGoalView extends JPanel implements PropertyChangeListener {
    private final JList<Goal> goalList;
    private final TodayGoalController controller;
    private final TodayGoalsViewModel viewModel;

    public TodayGoalView(TodayGoalsViewModel viewModel, TodayGoalController controller) {
        this.controller = controller;
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder("Today's Goals"));

        // Create the goal list with custom renderer
        goalList = new JList<>();
        goalList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        goalList.setCellRenderer(new GoalListCellRenderer());

        this.add(new JScrollPane(goalList), BorderLayout.CENTER);
        this.add(createButtonPanel(), BorderLayout.SOUTH);

        // Initial data load
        refreshGoalsList();
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton removeButton = new JButton("Remove from Today");
        removeButton.addActionListener(e -> removeSelectedGoal());

        buttonPanel.add(removeButton);
        return buttonPanel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            refreshGoalsList();
        }
    }

    private void refreshGoalsList() {
        SwingUtilities.invokeLater(() -> {
            List<Goal> goals = viewModel.getState().getTodaysGoals();
            goalList.setListData(goals.toArray(new Goal[0]));
        });
    }

    private void removeSelectedGoal() {
        Goal selected = goalList.getSelectedValue();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Remove '" + selected.getGoalInfo().getInfo().getName() + "'?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                controller.removeFromToday(
                        selected.getGoalInfo().getInfo().getName(),
                        true
                );
            }
        }
    }

    public Goal getSelectedGoal() {
        return goalList.getSelectedValue();
    }

    // Custom cell renderer class
    private static class GoalListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Goal) {
                Goal goal = (Goal) value;
                setText(String.format("%s - %d/%d",
                        goal.getGoalInfo().getInfo().getName(),
                        goal.getCurrentProgress(),
                        goal.getFrequency()));

                // Visual feedback
                if (goal.getCurrentProgress() >= goal.getFrequency()) {
                    setBackground(new Color(200, 255, 200)); // Light green for completed
                }
            }
            return this;
        }
    }
}