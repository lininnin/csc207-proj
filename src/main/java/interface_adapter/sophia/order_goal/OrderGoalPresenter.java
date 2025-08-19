package interface_adapter.sophia.order_goal;

import entity.Sophia.Goal;
import use_case.goalManage.order_goal.OrderGoalsOutputBoundary;
import use_case.goalManage.order_goal.OrderGoalsOutputData;

/**
 * Presenter for displaying ordered goals.
 */
public class OrderGoalPresenter implements OrderGoalsOutputBoundary {
    /**
     * Presents the ordered goals to the view.
     * @param outputData Contains the ordered list of goals
     */
    @Override
    public void present(OrderGoalsOutputData outputData) {
        for (Goal goal : outputData.getOrderedGoals()) {
            System.out.println(goal.getInfo().getName());
        }
    }
}
