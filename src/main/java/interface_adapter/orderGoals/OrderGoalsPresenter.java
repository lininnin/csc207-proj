package interface_adapter.orderGoals;

import entity.Sophia.Goal;
import use_case.orderGoal.OrderGoalsOutputBoundary;
import use_case.orderGoal.OrderGoalsOutputData;

public class OrderGoalsPresenter implements OrderGoalsOutputBoundary {
    @Override
    public void present(OrderGoalsOutputData outputData) {
        for (Goal goal : outputData.getOrderedGoals()) {
            System.out.println(goal.getInfo().getName()); // You can connect to view model later
        }
    }
}
