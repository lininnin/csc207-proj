package views;

import interface_adapter.Sophia.edit_todays_goal.EditTodaysGoalViewModel;
import interface_adapter.Sophia.delete_goal.DeleteGoalController;
import interface_adapter.Sophia.edit_todays_goal.EditTodaysGoalController;
import interface_adapter.Sophia.today_goal.TodayGoalController;
import interface_adapter.Sophia.order_goal.OrderGoalController;
import use_case.goalManage.delete_goal.DeleteGoalInputData;
import use_case.goalManage.edit_todays_goal.EditTodaysGoalInputData;
import use_case.goalManage.order_goal.OrderGoalsInputData;
import use_case.goalManage.today_goal.TodayGoalInputData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.List;

public class EditTodayGoalView extends JPanel implements PropertyChangeListener {
    private final EditTodaysGoalViewModel viewModel;
    private final JList<String> goalList = new JList<>();
    private final DeleteGoalController deleteController;
    private final EditTodaysGoalController editController;
    private final TodayGoalController todayController;
    private final OrderGoalController orderController;

    public EditTodayGoalView(EditTodaysGoalViewModel viewModel,
                             DeleteGoalController deleteController,
                             EditTodaysGoalController editController,
                             TodayGoalController todayController,
                             OrderGoalController orderController) {
        this.viewModel = viewModel;
        this.deleteController = deleteController;
        this.editController = editController;
        this.todayController = todayController;
        this.orderController = orderController;

        viewModel.addPropertyChangeListener(this);
        initializeUI();
        updateGoals(viewModel.getGoalNames()); // Use getGoalNames instead of getGoals
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 300));

        // Goal list
        goalList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(goalList);
        this.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));

        // Delete button
        buttonPanel.add(new JButton(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = goalList.getSelectedValue();
                if (selected != null) {
                    // First show confirmation dialog
                    int confirm = JOptionPane.showConfirmDialog(
                            EditTodayGoalView.this,
                            "Are you sure you want to delete '" + selected + "'?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION
                    );

                    boolean confirmed = (confirm == JOptionPane.YES_OPTION);
                    deleteController.execute(selected, confirmed);
                }
            }
        }));

        // Edit button
        buttonPanel.add(new JButton(new AbstractAction("Edit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = goalList.getSelectedValue();
                if (selected != null) {
                    String newDueDateStr = JOptionPane.showInputDialog(
                            EditTodayGoalView.this,
                            "Enter new due date (YYYY-MM-DD):",
                            "Edit Goal Due Date",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    if (newDueDateStr != null && !newDueDateStr.isEmpty()) {
                        try {
                            LocalDate newDueDate = LocalDate.parse(newDueDateStr);
                            editController.execute(selected, newDueDate, false);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(
                                    EditTodayGoalView.this,
                                    "Invalid date format. Please use YYYY-MM-DD",
                                    "Input Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            }
        }));

        // Add to Today button
        buttonPanel.add(new JButton(new AbstractAction("Add to Today") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = goalList.getSelectedValue();
                if (selected != null) {
                    // Directly add without confirmation since controller doesn't need it
                    todayController.addToToday(selected);

                    // Optional: Show success feedback
                    JOptionPane.showMessageDialog(
                            EditTodayGoalView.this,
                            "'" + selected + "' was added to Today's Goals!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        }));

        // Order button
        buttonPanel.add(new JButton(new AbstractAction("Order") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"name", "deadline", "period"};
                String selection = (String) JOptionPane.showInputDialog(
                        EditTodayGoalView.this,
                        "Select ordering criteria:",
                        "Order Goals",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]
                );
                if (selection != null) {
                    orderController.execute(selection, false);
                }
            }
        }));

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("goalNames".equals(evt.getPropertyName())) {
            updateGoals(viewModel.getGoalNames());
        }
    }

    private void updateGoals(List<String> goals) {
        SwingUtilities.invokeLater(() -> {
            goalList.setListData(goals.toArray(new String[0]));
        });
    }
}