package interface_adapter.Sophia.order_goal;

import entity.Sophia.Goal;
import use_case.goalManage.order_goal.OrderGoalsOutputBoundary;
import use_case.goalManage.order_goal.OrderGoalsOutputData;

public class OrderGoalPresenter implements OrderGoalsOutputBoundary {
    @Override
    public void present(OrderGoalsOutputData outputData) {
        for (Goal goal : outputData.getOrderedGoals()) {
            System.out.println(goal.getInfo().getName()); // You can connect to view model later
        }
    }
}
