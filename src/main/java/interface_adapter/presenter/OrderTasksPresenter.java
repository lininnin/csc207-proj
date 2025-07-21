package interface_adapter.presenter;

import use_case.orderTask.OrderTasksOutputBoundary;
import use_case.orderTask.OrderTasksOutputData;
import view.OrderTasksViewModel;

/**
 * Presenter for ordering tasks.
 * Implements the output boundary and updates the view model.
 */
public class OrderTasksPresenter implements OrderTasksOutputBoundary {
    private final OrderTasksViewModel orderTasksViewModel;

    public OrderTasksPresenter(OrderTasksViewModel orderTasksViewModel) {
        this.orderTasksViewModel = orderTasksViewModel;
    }

    @Override
    public void present(OrderTasksOutputData outputData) {
        // Update view model with sorted tasks and criteria
        orderTasksViewModel.setOrderedTasks(outputData.getOrderedTasks());
        orderTasksViewModel.setMessage("Tasks ordered by " + outputData.getSortCriteria() + ".");
    }

    @Override
    public void presentError(String error) {
        // Update view model with error message
        orderTasksViewModel.setMessage("Order Error: " + error);
    }
}
