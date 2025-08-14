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
        updateGoals(viewModel.getGoalNames());
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 300));

        // Goal list
        goalList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(goalList);
        this.add(scrollPane, BorderLayout.CENTER);

        // Remove button panel and all buttons
        // JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        // this.add(buttonPanel, BorderLayout.SOUTH);
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