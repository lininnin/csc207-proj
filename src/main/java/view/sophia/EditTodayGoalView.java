package view.Sophia;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;

import interface_adapter.sophia.edit_todays_goal.EditTodaysGoalViewModel;
import interface_adapter.sophia.delete_goal.DeleteGoalController;
import interface_adapter.sophia.edit_todays_goal.EditTodaysGoalController;
import interface_adapter.sophia.today_goal.TodayGoalController;
import interface_adapter.sophia.order_goal.OrderGoalController;

/**
 * A Swing view that displays and manages today's goals.
 * <p>
 * This panel observes changes in the {@link EditTodaysGoalViewModel}
 * and updates the goal list accordingly. It also holds references
 * to controllers for goal-related operations such as deleting,
 * editing, reordering, and marking today's goals.
 */
public class EditTodayGoalView extends JPanel implements PropertyChangeListener {
    private final EditTodaysGoalViewModel viewModel;
    private final JList<String> goalList = new JList<>();
    private final DeleteGoalController deleteController;
    private final EditTodaysGoalController editController;
    private final TodayGoalController todayController;
    private final OrderGoalController orderController;

    /**
     * Constructs a new {@code EditTodayGoalView}.
     *
     * @param viewModel        the view model containing the current goal data
     * @param deleteController the controller for deleting goals
     * @param editController   the controller for editing goals
     * @param todayController  the controller for marking today's goals
     * @param orderController  the controller for reordering goals
     */
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
    /**
     * Initializes the layout and UI components of this panel.
     */
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


    /**
     * Called when the observed {@link EditTodaysGoalViewModel} fires a property change event.
     * Updates the displayed list of goals when "goalNames" changes.
     *
     * @param evt the property change event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("goalNames".equals(evt.getPropertyName())) {
            updateGoals(viewModel.getGoalNames());
        }
    }

    /**
     * Updates the goal list with the given goals.
     *
     * @param goals the list of goal names to display
     */
    private void updateGoals(List<String> goals) {
        SwingUtilities.invokeLater(() -> {
            goalList.setListData(goals.toArray(new String[0]));
        });
    }
}
